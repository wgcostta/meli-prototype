"use client"

import React from 'react';
import { RefreshCw, AlertCircle, Wifi, WifiOff } from 'lucide-react';

// Skeleton Loading para a galeria de imagens
export function ProductGallerySkeleton() {
  return (
    <div className="lg:col-span-2">
      <div className="bg-white rounded-lg p-6 shadow-sm">
        {/* Imagem principal skeleton */}
        <div className="relative mb-4">
          <div 
            className="w-full h-96 bg-gray-200 rounded-lg animate-pulse" 
            data-testid="main-image-skeleton"
          />
        </div>

        {/* Miniaturas skeleton */}
        <div className="flex space-x-2">
          {Array.from({ length: 4 }).map((_, index) => (
            <div
              key={index}
              className="w-16 h-16 bg-gray-200 rounded border animate-pulse"
              data-testid={`thumbnail-skeleton-${index}`}
            />
          ))}
        </div>
      </div>
    </div>
  );
}

// Skeleton Loading para informações do produto
export function ProductInfoSkeleton() {
  return (
    <div className="space-y-6">
      <div className="bg-white rounded-lg p-6 shadow-sm">
        {/* Título skeleton */}
        <div className="h-6 bg-gray-200 rounded animate-pulse mb-4" />
        <div className="h-4 bg-gray-200 rounded animate-pulse mb-6 w-3/4" />

        {/* Avaliações skeleton */}
        <div className="flex items-center space-x-2 mb-4">
          <div className="flex space-x-1">
            {[...Array(5)].map((_, i) => (
              <div key={i} className="w-4 h-4 bg-gray-200 rounded animate-pulse" />
            ))}
          </div>
          <div className="h-4 bg-gray-200 rounded animate-pulse w-24" />
        </div>

        {/* Preço skeleton */}
        <div className="mb-6">
          <div className="h-8 bg-gray-200 rounded animate-pulse w-32 mb-2" />
          <div className="h-4 bg-gray-200 rounded animate-pulse w-48" />
        </div>

        {/* Variações skeleton */}
        <div className="mb-6">
          <div className="h-4 bg-gray-200 rounded animate-pulse w-16 mb-3" />
          <div className="flex space-x-2">
            {[...Array(4)].map((_, i) => (
              <div key={i} className="h-10 bg-gray-200 rounded animate-pulse w-20" />
            ))}
          </div>
        </div>

        {/* Botões skeleton */}
        <div className="space-y-3">
          <div className="h-12 bg-gray-200 rounded animate-pulse w-full" />
          <div className="h-12 bg-gray-200 rounded animate-pulse w-full" />
        </div>
      </div>
    </div>
  );
}

// Componente de erro com retry
interface ErrorStateProps {
  error: {
    message: string;
    code: string;
  };
  onRetry: () => void;
  retryCount: number;
  isRetrying: boolean;
}

export function ErrorState({ error, onRetry, retryCount, isRetrying }: ErrorStateProps) {
  const isConnectionError = error.code === 'FETCH_ERROR' || error.message.includes('conexão');

  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center px-4">
      <div className="max-w-md w-full bg-white rounded-lg shadow-sm p-8 text-center">
        <div className="mb-6">
          {isConnectionError ? (
            <WifiOff 
              className="mx-auto h-16 w-16 text-red-500" 
              data-testid="wifi-off-icon"
            />
          ) : (
            <AlertCircle 
              className="mx-auto h-16 w-16 text-yellow-500"
              data-testid="alert-circle-icon"
            />
          )}
        </div>

        <h2 className="text-xl font-semibold text-gray-900 mb-4">
          {isConnectionError ? 'Problema de Conexão' : 'Ops! Algo deu errado'}
        </h2>

        <p className="text-gray-600 mb-6">
          {error.message}
        </p>

        {retryCount > 0 && (
          <div className="bg-blue-50 border border-blue-200 rounded-lg p-3 mb-6">
            <p className="text-sm text-blue-800">
              Tentativa {retryCount} de 3 realizadas
            </p>
          </div>
        )}

        <button
          onClick={onRetry}
          disabled={isRetrying}
          className={`w-full flex items-center justify-center space-x-2 py-3 px-4 rounded-md font-medium transition-colors ${
            isRetrying
              ? 'bg-gray-100 text-gray-400 cursor-not-allowed'
              : 'bg-blue-500 text-white hover:bg-blue-600'
          }`}
        >
          <RefreshCw 
            className={`h-5 w-5 ${isRetrying ? 'animate-spin' : ''}`}
            data-testid="refresh-icon"
          />
          <span>
            {isRetrying ? 'Tentando novamente...' : 'Tentar novamente'}
          </span>
        </button>

        <div className="mt-6 text-sm text-gray-500">
          <p>Se o problema persistir:</p>
          <ul className="mt-2 space-y-1">
            <li>• Verifique sua conexão com a internet</li>
            <li>• Recarregue a página</li>
            <li>• Tente novamente em alguns minutos</li>
          </ul>
        </div>
      </div>
    </div>
  );
}

// Loading state para retry
export function RetryLoadingState({ message, retryCount }: { message: string; retryCount: number }) {
  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center px-4">
      <div className="max-w-md w-full bg-white rounded-lg shadow-sm p-8 text-center">
        <div className="mb-6">
          <Wifi 
            className="mx-auto h-16 w-16 text-blue-500 animate-pulse" 
            data-testid="wifi-icon"
          />
        </div>

        <h2 className="text-xl font-semibold text-gray-900 mb-4">
          Reconectando...
        </h2>

        <p className="text-gray-600 mb-4">
          {message}
        </p>

        <div className="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-6">
          <div className="flex items-center justify-center space-x-2">
            <RefreshCw 
              className="h-5 w-5 text-blue-600 animate-spin" 
              data-testid="refresh-icon"
            />
            <span className="text-sm text-blue-800">
              Tentativa {retryCount} de 3
            </span>
          </div>
        </div>

        <div className="w-full bg-gray-200 rounded-full h-2">
          <div 
            className="bg-blue-500 h-2 rounded-full transition-all duration-1000 animate-pulse"
            style={{ width: `${(retryCount / 3) * 100}%` }}
          />
        </div>
      </div>
    </div>
  );
}

// Componente para notificação de reconexão
export function ReconnectionNotice({ onDismiss }: { onDismiss: () => void }) {
  return (
    <div className="fixed top-4 right-4 bg-green-500 text-white px-6 py-3 rounded-lg shadow-lg z-50 animate-fade-in">
      <div className="flex items-center space-x-2">
        <Wifi className="h-5 w-5" data-testid="wifi-icon" />
        <span>Conectado! Carregando dados...</span>
        <button 
          onClick={onDismiss}
          className="ml-2 text-green-200 hover:text-white"
          aria-label="Dismiss notification"
        >
          ×
        </button>
      </div>
    </div>
  );
}