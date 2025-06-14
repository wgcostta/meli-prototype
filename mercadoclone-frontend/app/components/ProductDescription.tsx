"use client"

import React, { useState } from 'react';
import { ChevronDown, ChevronUp } from 'lucide-react';
import { Product } from '@/types/product';

interface ProductDescriptionProps {
  product: Product;
}

export default function ProductDescription({ product }: ProductDescriptionProps) {
  const [activeTab, setActiveTab] = useState('description');
  const [showFullDescription, setShowFullDescription] = useState(false);

  const tabs = [
    { id: 'description', label: 'Descrição' },
    { id: 'specifications', label: 'Especificações' },
    { id: 'reviews', label: 'Avaliações' },
    { id: 'questions', label: 'Perguntas' }
  ];

  // Usar especificações do produto ou valores padrão
  const specifications = Object.entries(product.specifications || {}).map(([key, value]) => ({
    label: key,
    value: value
  }));

  // Mock de reviews (mantido fixo pois não está na estrutura atual)
  const reviews = [
    {
      name: 'João Silva',
      rating: 5,
      date: '2024-06-01',
      comment: 'Excelente produto! Superou minhas expectativas.'
    },
    {
      name: 'Maria Santos',
      rating: 4,
      date: '2024-05-28',
      comment: 'Muito bom, recomendo!'
    },
    {
      name: 'Pedro Oliveira',
      rating: 5,
      date: '2024-05-25',
      comment: 'Ótima qualidade, chegou rápido.'
    }
  ];

  return (
    <div className="mt-8 bg-white rounded-lg shadow-sm">
      {/* Tabs */}
      <div className="border-b border-gray-200">
        <nav className="flex space-x-8 px-6">
          {tabs.map((tab) => (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id)}
              className={`py-4 px-1 border-b-2 font-medium text-sm transition-colors ${
                activeTab === tab.id
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              {tab.label}
            </button>
          ))}
        </nav>
      </div>

      {/* Tab Content */}
      <div className="p-6">
        {activeTab === 'description' && (
          <div>
            <h2 className="text-xl font-semibold mb-4">Descrição do produto</h2>
            <div className="prose max-w-none text-gray-700">
              <div className="mb-4">
                {product.shortDescription ? (
                  <p>{product.shortDescription}</p>
                ) : (
                  <p>
                    {product.description.length > 200 && !showFullDescription
                      ? `${product.description.substring(0, 200)}...`
                      : product.description
                    }
                  </p>
                )}
              </div>
              
              {product.description.length > 200 && (
                <button
                  onClick={() => setShowFullDescription(!showFullDescription)}
                  className="flex items-center space-x-2 text-blue-600 hover:text-blue-700 font-medium mb-6"
                >
                  <span>{showFullDescription ? 'Ver menos' : 'Ver mais'}</span>
                  {showFullDescription ? <ChevronUp size={16} /> : <ChevronDown size={16} />}
                </button>
              )}

              {showFullDescription && product.description.length > 200 && (
                <div className="mb-6">
                  <p>{product.description}</p>
                </div>
              )}

              {product.features && product.features.length > 0 && (
                <>
                  <h3 className="font-semibold mt-6 mb-3">Características principais:</h3>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                    {product.features.map((feature, index) => (
                      <div key={index} className="flex items-center space-x-2">
                        <span className="w-2 h-2 bg-blue-500 rounded-full flex-shrink-0"></span>
                        <span className="text-sm">{feature}</span>
                      </div>
                    ))}
                  </div>
                </>
              )}
            </div>
          </div>
        )}

        {activeTab === 'specifications' && (
          <div>
            <h2 className="text-xl font-semibold mb-4">Especificações técnicas</h2>
            {specifications.length > 0 ? (
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                {specifications.map((spec, index) => (
                  <div key={index} className="flex justify-between py-2 border-b border-gray-100">
                    <span className="font-medium text-gray-600">{spec.label}:</span>
                    <span className="text-gray-900">{spec.value}</span>
                  </div>
                ))}
              </div>
            ) : (
              <div className="text-gray-500 text-center py-8">
                Especificações técnicas não disponíveis para este produto.
              </div>
            )}
          </div>
        )}

        {activeTab === 'reviews' && (
          <div>
            <h2 className="text-xl font-semibold mb-4">Avaliações dos compradores</h2>
            
            {/* Resumo das avaliações usando dados do produto */}
            <div className="bg-gray-50 p-4 rounded-lg mb-6">
              <div className="flex items-center space-x-4">
                <div className="text-center">
                  <div className="text-3xl font-bold text-gray-900">
                    {product.rating.average.toFixed(1)}
                  </div>
                  <div className="flex text-yellow-400 justify-center">
                    {[...Array(5)].map((_, i) => (
                      <span key={i} className={i < Math.floor(product.rating.average) ? 'text-yellow-400' : 'text-gray-300'}>
                        ★
                      </span>
                    ))}
                  </div>
                  <div className="text-sm text-gray-600 mt-1">
                    {product.rating.count.toLocaleString('pt-BR')} avaliações
                  </div>
                </div>
                
                <div className="flex-1">
                  {[5, 4, 3, 2, 1].map((stars) => (
                    <div key={stars} className="flex items-center space-x-2 mb-1">
                      <span className="text-sm w-8">{stars}★</span>
                      <div className="flex-1 bg-gray-200 rounded-full h-2">
                        <div 
                          className="bg-yellow-400 h-2 rounded-full" 
                          style={{ 
                            width: `${(product.rating.distribution[stars as keyof typeof product.rating.distribution] / product.rating.count) * 100}%` 
                          }}
                        />
                      </div>
                      <span className="text-sm text-gray-600 w-8">
                        {product.rating.distribution[stars as keyof typeof product.rating.distribution]}
                      </span>
                    </div>
                  ))}
                </div>
              </div>
            </div>

            <div className="space-y-6">
              {reviews.map((review, index) => (
                <div key={index} className="border-b border-gray-100 pb-6 last:border-b-0">
                  <div className="flex items-center justify-between mb-2">
                    <div className="flex items-center space-x-3">
                      <div className="w-10 h-10 bg-gray-200 rounded-full flex items-center justify-center">
                        <span className="text-gray-600 font-medium text-sm">
                          {review.name.split(' ').map(n => n[0]).join('')}
                        </span>
                      </div>
                      <div>
                        <p className="font-medium">{review.name}</p>
                        <div className="flex items-center space-x-2">
                          <div className="flex text-yellow-400">
                            {[...Array(5)].map((_, i) => (
                              <span key={i} className={i < review.rating ? 'text-yellow-400' : 'text-gray-300'}>
                                ★
                              </span>
                            ))}
                          </div>
                          <span className="text-sm text-gray-500">
                            {new Date(review.date).toLocaleDateString('pt-BR')}
                          </span>
                        </div>
                      </div>
                    </div>
                  </div>
                  <p className="text-gray-700 ml-13">{review.comment}</p>
                </div>
              ))}
            </div>
            
            <div className="mt-6 text-center">
              <button className="bg-blue-100 text-blue-600 px-6 py-2 rounded-md hover:bg-blue-200 transition-colors">
                Ver todas as {product.rating.count.toLocaleString('pt-BR')} avaliações
              </button>
            </div>
          </div>
        )}

        {activeTab === 'questions' && (
          <div>
            <h2 className="text-xl font-semibold mb-4">Perguntas e respostas</h2>
            <div className="space-y-4">
              <div className="bg-gray-50 p-4 rounded-lg">
                <p className="font-medium text-gray-900 mb-2">
                  P: Este produto possui garantia?
                </p>
                <p className="text-gray-700">
                  R: Sim, {product.warranty || 'este produto possui garantia conforme legislação'}.
                </p>
                <span className="text-xs text-gray-500 mt-2 block">Respondido pelo vendedor</span>
              </div>
              
              <div className="bg-gray-50 p-4 rounded-lg">
                <p className="font-medium text-gray-900 mb-2">
                  P: Qual a marca do produto?
                </p>
                <p className="text-gray-700">
                  R: A marca é {product.brand}.
                </p>
                <span className="text-xs text-gray-500 mt-2 block">Respondido pelo vendedor</span>
              </div>
              
              <div className="bg-gray-50 p-4 rounded-lg">
                <p className="font-medium text-gray-900 mb-2">
                  P: Qual o prazo de entrega?
                </p>
                <p className="text-gray-700">
                  R: {product.shipping.description || `O produto chegará em até ${product.shipping.estimatedDays} dias úteis`}.
                </p>
                <span className="text-xs text-gray-500 mt-2 block">Respondido pelo vendedor</span>
              </div>
            </div>
            
            <div className="mt-6">
              <h3 className="font-medium mb-3">Faça uma pergunta</h3>
              <div className="flex space-x-3">
                <input
                  type="text"
                  placeholder={`Digite sua pergunta sobre ${product.title}...`}
                  className="flex-1 px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
                <button className="bg-blue-500 text-white px-6 py-2 rounded-md hover:bg-blue-600 transition-colors">
                  Perguntar
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}