"use client"

import { useState, useEffect, useCallback } from 'react';
import { Product, ApiResponse, ApiError, ProductState } from '@/types/product';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'https://meli-backend-33b27898349d.herokuapp.com';
const MAX_RETRY_ATTEMPTS = 3;
const RETRY_DELAY = 2000; // 2 segundos

export function useProductApi(productId: string) {
  const [state, setState] = useState<ProductState>({
    product: null,
    loading: true,
    error: null,
    retryCount: 0,
  });

  // Função para fazer delay entre tentativas
  const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

  // Função para calcular delay com backoff exponencial
  const getRetryDelay = (attempt: number) => {
    return RETRY_DELAY * Math.pow(2, attempt - 1); // 2s, 4s, 8s
  };

  // Função principal para buscar produto
  const fetchProduct = useCallback(async (retryAttempt = 0): Promise<void> => {
    try {
      setState(prev => ({ 
        ...prev, 
        loading: true, 
        error: null,
        retryCount: retryAttempt 
      }));

      const response = await fetch(`${API_BASE_URL}/api/v1/products/${productId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
        // Timeout de 10 segundos
        signal: AbortSignal.timeout(10000),
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result: ApiResponse<Product> = await response.json();

      if (!result.success) {
        throw new Error(result.message || 'Erro ao buscar produto');
      }

      setState({
        product: result.data,
        loading: false,
        error: null,
        retryCount: 0,
      });

    } catch (error) {
      console.error('Erro ao buscar produto:', error);

      const apiError: ApiError = {
        message: error instanceof Error ? error.message : 'Erro desconhecido',
        code: 'FETCH_ERROR',
        details: { attempt: retryAttempt + 1, productId }
      };

      // Verificar se deve tentar novamente
      if (retryAttempt < MAX_RETRY_ATTEMPTS) {
        const delayTime = getRetryDelay(retryAttempt + 1);
        
        setState(prev => ({
          ...prev,
          loading: true,
          error: {
            ...apiError,
            message: `Erro na conexão. Tentando novamente em ${delayTime / 1000}s... (${retryAttempt + 1}/${MAX_RETRY_ATTEMPTS})`
          },
          retryCount: retryAttempt + 1,
        }));

        await delay(delayTime);
        return fetchProduct(retryAttempt + 1);
      } else {
        // Todas as tentativas falharam
        setState({
          product: null,
          loading: false,
          error: {
            ...apiError,
            message: 'Não foi possível carregar o produto. Verifique sua conexão e tente novamente.'
          },
          retryCount: retryAttempt + 1,
        });
      }
    }
  }, [productId]);

  // Função para retry manual
  const retry = useCallback(() => {
    fetchProduct(0);
  }, [fetchProduct]);

  // Função para verificar conectividade
  const checkConnectivity = useCallback(async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/actuator/health`, {
        method: 'HEAD',
        signal: AbortSignal.timeout(5000),
      });
      return response.ok;
    } catch {
      return false;
    }
  }, []);

  // Buscar produto quando o ID mudar
  useEffect(() => {
    if (productId) {
      fetchProduct(0);
    }
  }, [productId, fetchProduct]);

  // Verificar conectividade quando há erro
  useEffect(() => {
    if (state.error && !state.loading) {
      const intervalId = setInterval(async () => {
        const isOnline = await checkConnectivity();
        if (isOnline && state.error) {
          clearInterval(intervalId);
          retry();
        }
      }, 5000); // Verificar a cada 5 segundos

      return () => clearInterval(intervalId);
    }
  }, [state.error, state.loading, checkConnectivity, retry]);

  return {
    product: state.product,
    loading: state.loading,
    error: state.error,
    retryCount: state.retryCount,
    retry,
    isRetrying: state.loading && state.retryCount > 0,
  };
}

// Hook para buscar reviews (opcional)
export function useProductReviews(productId: string, page = 1, limit = 10) {
  const [reviews, setReviews] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<ApiError | null>(null);

  const fetchReviews = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);

      const response = await fetch(
        `${API_BASE_URL}/api/v1/products/${productId}/reviews?page=${page}&limit=${limit}`,
        {
          headers: { 'Content-Type': 'application/json' },
          signal: AbortSignal.timeout(10000),
        }
      );

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();
      setReviews(result.data);
    } catch (error) {
      setError({
        message: error instanceof Error ? error.message : 'Erro ao carregar reviews',
        code: 'REVIEWS_ERROR',
      });
    } finally {
      setLoading(false);
    }
  }, [productId, page, limit]);

  useEffect(() => {
    if (productId) {
      fetchReviews();
    }
  }, [fetchReviews]);

  return { reviews, loading, error, refetch: fetchReviews };
}