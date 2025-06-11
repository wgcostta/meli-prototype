"use client"

import React, { useState } from 'react';
import { Heart, Share2, Star, Truck, Shield, CreditCard, Plus, Minus } from 'lucide-react';

export default function ProductInfo() {
  const [quantity, setQuantity] = useState(1);
  const [selectedColor, setSelectedColor] = useState('Azul');
  const [isFavorite, setIsFavorite] = useState(false);

  const colors = [
    { name: 'Azul', value: '#3B82F6' },
    { name: 'Preto', value: '#1F2937' },
    { name: 'Branco', value: '#F9FAFB' },
    { name: 'Vermelho', value: '#EF4444' }
  ];

  const handleQuantityChange = (action: 'increase' | 'decrease') => {
    if (action === 'increase') {
      setQuantity(prev => Math.min(prev + 1, 15));
    } else {
      setQuantity(prev => Math.max(prev - 1, 1));
    }
  };

  return (
    <div className="space-y-6">
      {/* Informações Principais */}
      <div className="bg-white rounded-lg p-6 shadow-sm">
        <div className="flex items-start justify-between mb-4">
          <h1 className="text-xl font-semibold text-gray-900 leading-tight">
            Fone de Ouvido Bluetooth Premium - Som de Alta Qualidade com Cancelamento de Ruído Ativo
          </h1>
          <div className="flex space-x-2 ml-4">
            <button 
              onClick={() => setIsFavorite(!isFavorite)}
              className="p-2 hover:bg-gray-100 rounded-full transition-colors"
            >
              <Heart 
                size={20} 
                className={isFavorite ? 'text-red-500 fill-current' : 'text-gray-400'} 
              />
            </button>
            <button className="p-2 hover:bg-gray-100 rounded-full transition-colors">
              <Share2 size={20} className="text-gray-400" />
            </button>
          </div>
        </div>

        {/* Avaliações */}
        <div className="flex items-center space-x-2 mb-4">
          <div className="flex text-yellow-400">
            {[...Array(5)].map((_, i) => (
              <Star key={i} size={16} fill="currentColor" />
            ))}
          </div>
          <span className="text-sm text-gray-600">(4.8)</span>
          <span className="text-sm text-blue-600 hover:underline cursor-pointer">
            2.341 avaliações
          </span>
          <span className="text-gray-300">|</span>
          <span className="text-sm text-gray-600">+1000 vendidos</span>
        </div>

        {/* Preço */}
        <div className="mb-6">
          <div className="flex items-baseline space-x-2 mb-2">
            <span className="text-3xl font-light text-gray-900">R$ 299</span>
            <span className="text-lg text-gray-500 line-through">R$ 399</span>
            <span className="bg-green-100 text-green-800 px-2 py-1 rounded text-sm font-medium">
              25% OFF
            </span>
          </div>
          <p className="text-sm text-gray-600">em 12x R$ 24,92 sem juros</p>
          <p className="text-xs text-gray-500 mt-1">Preço à vista no Pix: R$ 284,05</p>
        </div>

        {/* Variações de Cor */}
        <div className="mb-6">
          <h3 className="font-medium mb-3">Cor: <span className="font-normal">{selectedColor}</span></h3>
          <div className="flex space-x-2">
            {colors.map((color) => (
              <button
                key={color.name}
                onClick={() => setSelectedColor(color.name)}
                className={`flex items-center space-x-2 px-4 py-2 border rounded-md text-sm transition-all ${
                  selectedColor === color.name
                    ? 'border-blue-500 bg-blue-50 text-blue-700'
                    : 'border-gray-300 hover:border-gray-400'
                }`}
              >
                <div 
                  className="w-4 h-4 rounded-full border border-gray-300"
                  style={{ backgroundColor: color.value }}
                />
                <span>{color.name}</span>
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
                disabled={quantity >= 15}
                className="p-2 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <Plus size={16} />
              </button>
            </div>
            <span className="text-sm text-gray-600">(15 disponíveis)</span>
          </div>
        </div>

        {/* Botões de Ação */}
        <div className="space-y-3 mb-6">
          <button className="w-full bg-blue-500 text-white py-3 px-6 rounded-md font-medium hover:bg-blue-600 transition-colors">
            Comprar agora
          </button>
          <button className="w-full bg-blue-100 text-blue-600 py-3 px-6 rounded-md font-medium hover:bg-blue-200 transition-colors">
            Adicionar ao carrinho
          </button>
        </div>

        {/* Informações de Entrega e Garantias */}
        <div className="space-y-4 pt-4 border-t border-gray-200">
          <div className="flex items-start space-x-3">
            <Truck size={20} className="text-green-600 mt-0.5" />
            <div>
              <p className="font-medium text-green-600">Frete grátis</p>
              <p className="text-sm text-gray-600">Chega amanhã se você comprar nas próximas 2h 15min</p>
              <button className="text-xs text-blue-600 hover:underline mt-1">
                Ver formas de entrega
              </button>
            </div>
          </div>
          
          <div className="flex items-start space-x-3">
            <Shield size={20} className="text-blue-600 mt-0.5" />
            <div>
              <p className="font-medium">Compra Protegida</p>
              <p className="text-sm text-gray-600">Receba o produto que está esperando ou devolvemos o dinheiro</p>
            </div>
          </div>

          <div className="flex items-start space-x-3">
            <CreditCard size={20} className="text-purple-600 mt-0.5" />
            <div>
              <p className="font-medium">Parcelamento sem juros</p>
              <p className="text-sm text-gray-600">Em até 12x no cartão de crédito</p>
              <button className="text-xs text-blue-600 hover:underline mt-1">
                Ver mais opções de pagamento
              </button>
            </div>
          </div>
        </div>
      </div>

      {/* Informações do Vendedor */}
      <div className="bg-white rounded-lg p-6 shadow-sm">
        <h3 className="font-medium mb-4">Vendido por</h3>
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-3">
            <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center">
              <span className="text-blue-600 font-semibold text-lg">TS</span>
            </div>
            <div>
              <p className="font-medium text-blue-600">TechStore Oficial</p>
              <div className="flex items-center space-x-2 mt-1">
                <div className="flex text-yellow-400">
                  {[...Array(5)].map((_, i) => (
                    <Star key={i} size={12} fill="currentColor" />
                  ))}
                </div>
                <span className="text-xs text-gray-600">(98% positivas)</span>
              </div>
              <p className="text-xs text-gray-500 mt-1">+5 anos no MercadoClone</p>
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