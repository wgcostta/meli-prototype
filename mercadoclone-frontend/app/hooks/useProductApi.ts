import { useState, useEffect, useCallback } from 'react'
import { Product, ApiResponse, ProductState } from '@/types/product'

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'https://meli-backend-33b27898349d.herokuapp.com';

interface UseProductApiOptions {
  retryAttempts?: number
  retryDelay?: number
  timeout?: number
}

interface UseProductApiResult extends ProductState {
  retry: () => void
  isRetrying: boolean
}

// Função para criar timeout signal compatível com Jest
const createTimeoutController = (timeoutMs: number) => {
  const controller = new AbortController()
  const timeoutId = setTimeout(() => {
    controller.abort()
  }, timeoutMs)
  
  return { controller, timeoutId }
}

export function useProductApi(
  productId: string | null,
  options: UseProductApiOptions = {}
): UseProductApiResult {
  const {
    retryAttempts = 3,
    retryDelay = 2000,
    timeout: timeoutMs = 10000
  } = options

  const [state, setState] = useState<ProductState>({
    product: null,
    loading: false,
    error: null,
    retryCount: 0
  })

  const sleep = (ms: number) => new Promise(resolve => setTimeout(resolve, ms))

  const createErrorMessage = (attempt: number, maxAttempts: number): string => {
    if (attempt >= maxAttempts) {
      return 'Não foi possível carregar o produto. Verifique sua conexão.'
    }
    return `Erro na conexão. Tentando novamente em ${retryDelay / 1000}s... (${attempt}/${maxAttempts})`
  }

  const fetchProduct = useCallback(async (attempt: number = 1): Promise<void> => {
    if (!productId) return

    try {
      setState(prev => ({ 
        ...prev, 
        loading: true,
        error: attempt === 1 ? null : prev.error
      }))

      const { controller, timeoutId } = createTimeoutController(timeoutMs)

      const response = await fetch(`${API_BASE_URL}/api/v1/products/${productId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        },
        signal: controller.signal
      })

      clearTimeout(timeoutId)

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`)
      }

      const data: ApiResponse<Product> = await response.json()

      if (!data.success) {
        throw new Error(data.message || 'Product not found')
      }

      setState({
        product: data.data,
        loading: false,
        error: null,
        retryCount: 0
      })

    } catch (error) {
      console.error('Erro ao buscar produto:', error)
      
      const errorMessage = error instanceof Error ? error.message : 'Unknown error'
      const shouldRetry = attempt < retryAttempts && 
                         (errorMessage.includes('Failed to fetch') || 
                          errorMessage.includes('NetworkError') ||
                          errorMessage.includes('Network error') ||
                          errorMessage.includes('timeout') ||
                          errorMessage.includes('AbortError'))

      if (shouldRetry) {
        setState(prev => ({
          ...prev,
          loading: false,
          error: {
            message: createErrorMessage(attempt, retryAttempts),
            code: 'FETCH_ERROR',
            details: { productId, attempt }
          },
          retryCount: attempt
        }))

        await sleep(retryDelay)
        return fetchProduct(attempt + 1)
      } else {
        // Final error state - no more retries
        setState({
          product: null,
          loading: false,
          error: {
            message: errorMessage,
            code: errorMessage.includes('timeout') || errorMessage.includes('AbortError') ? 
              'TIMEOUT_ERROR' : 
              errorMessage.includes('HTTP error') ? 'HTTP_ERROR' : 'FETCH_ERROR',
            details: { productId, attempt }
          },
          retryCount: attempt
        })
      }
    }
  }, [productId, retryAttempts, retryDelay, timeoutMs])

  const retry = useCallback(() => {
    setState(prev => ({ 
      ...prev, 
      error: null,
      retryCount: 0 
    }))
    fetchProduct(1)
  }, [fetchProduct])

  useEffect(() => {
    if (productId) {
      fetchProduct(1)
    } else {
      setState({
        product: null,
        loading: false,
        error: null,
        retryCount: 0
      })
    }
  }, [productId, fetchProduct])

  return {
    ...state,
    retry,
    isRetrying: state.loading && state.retryCount > 0
  }
}