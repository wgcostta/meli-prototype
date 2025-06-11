"use client"

import React from 'react';
import { useProductApi } from './hooks/useProductApi';
import Header from './components/Header';
import Breadcrumb from './components/Breadcrumb';
import ProductGallery from './components/ProductGallery';
import ProductInfo from './components/ProductInfo';
import ProductDescription from './components/ProductDescription';
import { 
  ProductGallerySkeleton, 
  ProductInfoSkeleton, 
  ErrorState, 
  RetryLoadingState 
} from './components/LoadingAndError';

interface ProductPageProps {
  params: {
    id: string;
  };
}

export default function ProductPage({ params }: ProductPageProps) {
  const productId = params?.id || 'default-product-id';
  
  const { 
    product, 
    loading, 
    error, 
    retryCount, 
    retry, 
    isRetrying 
  } = useProductApi(productId);

  // Estado de loading com retry
  if (isRetrying) {
    return (
      <RetryLoadingState 
        message="Tentando reconectar com o servidor..."
        retryCount={retryCount}
      />
    );
  }

  // Estado de erro
  if (error && !loading) {
    return (
      <ErrorState
        error={error}
        onRetry={retry}
        retryCount={retryCount}
        isRetrying={isRetrying}
      />
    );
  }

  // Estado de loading inicial
  if (loading && !product) {
    return (
      <div className="min-h-screen bg-gray-50">
        <Header />
        <Breadcrumb />
        
        <div className="max-w-6xl mx-auto px-4 py-6">
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            <ProductGallerySkeleton />
            <ProductInfoSkeleton />
          </div>
          
          {/* Skeleton para descrição */}
          <div className="mt-8 bg-white rounded-lg p-6 shadow-sm">
            <div className="h-6 bg-gray-200 rounded animate-pulse mb-4 w-48" />
            <div className="space-y-2">
              {[...Array(5)].map((_, i) => (
                <div key={i} className="h-4 bg-gray-200 rounded animate-pulse" />
              ))}
            </div>
          </div>
        </div>
      </div>
    );
  }

  // Produto carregado com sucesso
  if (product) {
    return (
      <div className="min-h-screen bg-gray-50">
        <Header />
        <Breadcrumb product={product} />
        
        <div className="max-w-6xl mx-auto px-4 py-6">
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            <ProductGallery product={product} />
            <ProductInfo product={product} />
          </div>
          <ProductDescription product={product} />
        </div>
      </div>
    );
  }

  // Fallback (não deveria chegar aqui)
  return null;
}