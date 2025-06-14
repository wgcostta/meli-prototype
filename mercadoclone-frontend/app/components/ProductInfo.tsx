"use client"

import React, { useState } from 'react';
import { Heart, Share2, Star, Truck, Shield, CreditCard, Plus, Minus } from 'lucide-react';
import { Product } from '@/types/product';

interface ProductInfoProps {
  product: Product;
}

export default function ProductInfo({ product }: ProductInfoProps) {
  const [quantity, setQuantity] = useState(1);
  const [selectedColor, setSelectedColor] = useState('Azul'); // Mantido fixo como solicitado
  const [isFavorite, setIsFavorite] = useState(false);

  // Cores fixas (mantidas como no código original)
  const colors = [
    { name: 'Azul', value: '#3B82F6' },
    { name: 'Preto', value: '#1F2937' },
    { name: 'Branco', value: '#F9FAFB' },
    { name: 'Vermelho', value: '#EF4444' }
  ];

  const handleQuantityChange = (action: 'increase' | 'decrease') => {
    if (action === 'increase') {
      setQuantity(prev => Math.min(prev + 1, product.stock.available));
    } else {
      setQuantity(prev => Math.max(prev - 1, 1));
    }
  };

  // Formatação de preço
  const formatPrice = (price: number, currency: string = 'BRL') => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: currency === 'USD' ? 'BRL' : currency, // Converter USD para BRL para exibição
    }).format(price);
  };

  // Calcular desconto
  const discountPercentage = product.price.original 
    ? Math.round(((product.price.original - product.price.current) / product.price.original) * 100)
    : product.price.discount || 0;

  return (
    <div className="space-y-6">
      {/* Informações Principais */}
      <div className="bg-white rounded-lg p-6 shadow-sm">
        <div className="flex items-start justify-between mb-4">
          <h1 className="text-xl font-semibold text-gray-900 leading-tight">
            {product.title}
          </h1>
          <div className="flex space-x-2 ml-4">
            <button 
              onClick={() => setIsFavorite(!isFavorite)}
              className="p-2 hover:bg-gray-100 rounded-full transition-colors"
              aria-label="Adicionar aos favoritos"
            >
              <Heart 
                size={20} 
                className={isFavorite ? 'text-red-500 fill-current' : 'text-gray-400'} 
              />
            </button>
            <button 
              className="p-2 hover:bg-gray-100 rounded-full transition-colors"
              aria-label="Compartilhar produto"
            >
              <Share2 size={20} className="text-gray-400" />
            </button>
          </div>
        </div>

        {/* Avaliações */}
        <div className="flex items-center space-x-2 mb-4">
          <div className="flex text-yellow-400">
            {[...Array(5)].map((_, i) => (
              <Star 
                key={i} 
                size={16} 
                fill={i < Math.floor(product.rating.average) ? "currentColor" : "none"} 
              />
            ))}
          </div>
          <span className="text-sm text-gray-600">
            ({product.rating.average.toFixed(1)})
          </span>
          <span className="text-sm text-blue-600 hover:underline cursor-pointer">
            {product.rating.count.toLocaleString('pt-BR')} avaliações
          </span>
          <span className="text-gray-300">|</span>
          <span className="text-sm text-gray-600">
            {product.stock.total > 100 ? '+100' : product.stock.total} vendidos
          </span>
        </div>

        {/* Marca */}
        {product.brand && (
          <div className="mb-4">
            <span className="text-sm text-gray-600">Marca: </span>
            <span className="text-sm font-medium text-blue-600 hover:underline cursor-pointer">
              {product.brand}
            </span>
          </div>
        )}

        {/* Preço */}
        <div className="mb-6">
          <div className="flex items-baseline space-x-2 mb-2">
            <span className="text-3xl font-light text-gray-900">
              {formatPrice(product.price.current)}
            </span>
            {product.price.original && product.price.original > product.price.current && (
              <>
                <span className="text-lg text-gray-500 line-through">
                  {formatPrice(product.price.original)}
                </span>
                <span className="bg-green-100 text-green-800 px-2 py-1 rounded text-sm font-medium">
                  {discountPercentage}% OFF
                </span>
              </>
            )}
          </div>
          
          {/* Métodos de pagamento */}
          {product.paymentMethods && product.paymentMethods.length > 0 && (
            <div className="space-y-1">
              {product.paymentMethods.map((method, index) => (
                <p key={index} className="text-sm text-gray-600">
                  {method.installments 
                    ? `em ${method.installments}x ${formatPrice(product.price.current / method.installments)} sem juros`
                    : method.name
                  }
                </p>
              ))}
            </div>
          )}
        </div>

        {/* Variações de Cor */}
        <div className="mb-6">
          <h3 className="font-medium mb-3">Cor: <span className="font-normal">{selectedColor}</span></h3>
          <div className="grid grid-cols-2 gap-2">
            {colors.map((color) => (
              <button
                key={color.name}
                onClick={() => setSelectedColor(color.name)}
                className={`flex items-center space-x-2 px-3 py-2 border rounded-md text-sm transition-all ${
                  selectedColor === color.name
                    ? 'border-blue-500 bg-blue-50 text-blue-700'
                    : 'border-gray-300 hover:border-gray-400'
                }`}
              >
                <div 
                  className="w-4 h-4 rounded-full border border-gray-300 flex-shrink-0"
                  style={{ backgroundColor: color.value }}
                />
                <span className="truncate">{color.name}</span>
              </button>
            ))}
          </div>
        </div>

        {/* Quantidade */}
        <div className="mb-6">
          <h3 className="font-medium mb-3">Quantidade:</h3>
          <div className="flex items-center space-x-3">
            <div className="flex items-center border border-gray-300 rounded-md">
              <button 
                onClick={() => handleQuantityChange('decrease')}
                disabled={quantity <= 1}
                className="p-2 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <Minus size={16} />
              </button>
              <span className="px-4 py-2 min-w-[3rem] text-center">{quantity}</span>
              <button 
                onClick={() => handleQuantityChange('increase')}
                disabled={quantity >= product.stock.available}
                className="p-2 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <Plus size={16} />
              </button>
            </div>
            <span className="text-sm text-gray-600">
              ({product.stock.available} disponíveis)
            </span>
          </div>
        </div>

        {/* Botões de Ação */}
        <div className="space-y-3 mb-6">
          <button 
            className="w-full bg-blue-500 text-white py-3 px-6 rounded-md font-medium hover:bg-blue-600 transition-colors disabled:bg-gray-300 disabled:cursor-not-allowed"
            disabled={!product.stock.isAvailable}
          >
            {product.stock.isAvailable ? 'Comprar agora' : 'Produto indisponível'}
          </button>
          <button 
            className="w-full bg-blue-100 text-blue-600 py-3 px-6 rounded-md font-medium hover:bg-blue-200 transition-colors disabled:bg-gray-100 disabled:text-gray-400 disabled:cursor-not-allowed"
            disabled={!product.stock.isAvailable}
          >
            Adicionar ao carrinho
          </button>
        </div>

        {/* Informações de Entrega e Garantias */}
        <div className="space-y-4 pt-4 border-t border-gray-200">
          {/* Frete */}
          {product.shipping && (
            <div className="flex items-start space-x-3">
              <Truck size={20} className={`mt-0.5 ${product.shipping.free ? 'text-green-600' : 'text-gray-600'}`} />
              <div>
                <p className={`font-medium ${product.shipping.free ? 'text-green-600' : 'text-gray-900'}`}>
                  {product.shipping.free ? 'Frete grátis' : `Frete: ${formatPrice(product.shipping.cost)}`}
                </p>
                <p className="text-sm text-gray-600">
                  {product.shipping.description || `Chega em ${product.shipping.estimatedDays} dias úteis`}
                </p>
              </div>
            </div>
          )}
          
          <div className="flex items-start space-x-3">
            <Shield size={20} className="text-blue-600 mt-0.5" />
            <div>
              <p className="font-medium">Compra Protegida</p>
              <p className="text-sm text-gray-600">
                Receba o produto que está esperando ou devolvemos o dinheiro
              </p>
            </div>
          </div>

          {product.paymentMethods && product.paymentMethods.some(method => method.installments) && (
            <div className="flex items-start space-x-3">
              <CreditCard size={20} className="text-purple-600 mt-0.5" />
              <div>
                <p className="font-medium">Parcelamento sem juros</p>
                <p className="text-sm text-gray-600">
                  Em até {Math.max(...product.paymentMethods.filter(m => m.installments).map(m => m.installments!))}x no cartão de crédito
                </p>
              </div>
            </div>
          )}

          {/* Garantia */}
          {product.warranty && (
            <div className="text-sm text-gray-600">
              <strong>Garantia:</strong> {product.warranty}
            </div>
          )}
        </div>
      </div>

      {/* Informações do Vendedor */}
      <div className="bg-white rounded-lg p-6 shadow-sm">
        <h3 className="font-medium mb-4">Vendido por</h3>
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-3">
            <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center">
              {product.seller.avatar ? (
                <img 
                  src={product.seller.avatar} 
                  alt={product.seller.name}
                  className="w-12 h-12 rounded-full object-cover"
                />
              ) : (
                <span className="text-blue-600 font-semibold text-lg">
                  {product.seller.name.substring(0, 2).toUpperCase()}
                </span>
              )}
            </div>
            <div>
              <p className={`font-medium ${product.seller.isOfficial ? 'text-blue-600' : 'text-gray-900'}`}>
                {product.seller.name}
                {product.seller.isOfficial && (
                  <span className="ml-2 bg-blue-100 text-blue-800 px-2 py-1 rounded text-xs font-medium">
                    Oficial
                  </span>
                )}
              </p>
              <div className="flex items-center space-x-2 mt-1">
                <div className="flex text-yellow-400">
                  {[...Array(5)].map((_, i) => (
                    <Star 
                      key={i} 
                      size={12} 
                      fill={i < Math.floor(product.seller.reputation) ? "currentColor" : "none"} 
                    />
                  ))}
                </div>
                <span className="text-xs text-gray-600">
                  ({product.seller.positiveRating}% positivas)
                </span>
              </div>
              <p className="text-xs text-gray-500 mt-1">
                +{product.seller.yearsOnPlatform} anos na plataforma • {product.seller.location}
              </p>
            </div>
          </div>
          <button className="text-blue-600 text-sm hover:underline">
            Ver mais produtos
          </button>
        </div>
      </div>
    </div>
  );
}