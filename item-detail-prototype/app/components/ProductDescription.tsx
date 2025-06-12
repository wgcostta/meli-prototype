import React, { useState } from 'react';
import { Product } from '@/types/product';
import { ChevronDown, ChevronUp } from 'lucide-react';

interface ProductDescriptionProps {
  product: Product;
}

export default function ProductDescription({ product }: ProductDescriptionProps) {
  const [activeTab, setActiveTab] = useState<'description' | 'specifications' | 'reviews'>('description');
  const [isDescriptionExpanded, setIsDescriptionExpanded] = useState(false);

  const tabs = [
    { id: 'description', label: 'Descrição', count: null },
    { id: 'specifications', label: 'Especificações', count: Object.keys(product.specifications).length },
    { id: 'reviews', label: 'Avaliações', count: product.rating.count },
  ] as const;

  const renderStars = (rating: number) => {
    return Array.from({ length: 5 }, (_, i) => (
      <div
        key={i}
        className={`w-4 h-4 ${
          i < Math.floor(rating) ? 'text-yellow-400 fill-current' : 'text-gray-300'
        }`}
      >
        ★
      </div>
    ));
  };

  const getPercentage = (value: number, total: number) => {
    return total > 0 ? Math.round((value / total) * 100) : 0;
  };

  return (
    <div className="mt-8 bg-white rounded-lg shadow-sm">
      {/* Tabs */}
      <div className="border-b border-gray-200">
        <div className="flex">
          {tabs.map((tab) => (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id)}
              className={`px-6 py-4 text-sm font-medium border-b-2 transition-colors ${
                activeTab === tab.id
                  ? 'border-blue-600 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              {tab.label}
              {tab.count && (
                <span className="ml-1 text-xs bg-gray-100 text-gray-600 px-2 py-1 rounded-full">
                  {tab.count}
                </span>
              )}
            </button>
          ))}
        </div>
      </div>

      {/* Tab Content */}
      <div className="p-6">
        {activeTab === 'description' && (
          <div>
            <h3 className="text-lg font-semibold mb-4">Sobre este produto</h3>
            
            {/* Características principais */}
            {product.features && product.features.length > 0 && (
              <div className="mb-6">
                <h4 className="font-medium mb-3">Características principais:</h4>
                <ul className="list-disc list-inside space-y-1">
                  {product.features.map((feature, index) => (
                    <li key={index} className="text-gray-700">{feature}</li>
                  ))}
                </ul>
              </div>
            )}

            {/* Descrição */}
            <div>
              <h4 className="font-medium mb-3">Descrição:</h4>
              <div className="text-gray-700 leading-relaxed">
                {product.shortDescription && (
                  <p className="mb-4 font-medium">{product.shortDescription}</p>
                )}
                
                <div className={`${!isDescriptionExpanded ? 'line-clamp-4' : ''}`}>
                  {product.description.split('\n').map((paragraph, index) => (
                    <p key={index} className="mb-2">
                      {paragraph}
                    </p>
                  ))}
                </div>

                {product.description.length > 200 && (
                  <button
                    onClick={() => setIsDescriptionExpanded(!isDescriptionExpanded)}
                    className="flex items-center gap-1 text-blue-600 hover:text-blue-700 text-sm mt-2"
                  >
                    {isDescriptionExpanded ? (
                      <>
                        Ver menos <ChevronUp className="w-4 h-4" />
                      </>
                    ) : (
                      <>
                        Ver mais <ChevronDown className="w-4 h-4" />
                      </>
                    )}
                  </button>
                )}
              </div>
            </div>
          </div>
        )}

        {activeTab === 'specifications' && (
          <div>
            <h3 className="text-lg font-semibold mb-4">Especificações técnicas</h3>
            <div className="grid gap-4">
              {Object.entries(product.specifications).map(([key, value]) => (
                <div key={key} className="flex py-2 border-b border-gray-100 last:border-0">
                  <div className="font-medium text-gray-900 w-1/3">{key}:</div>
                  <div className="text-gray-700 w-2/3">{value}</div>
                </div>
              ))}
            </div>

            {/* Informações adicionais */}
            <div className="mt-6 pt-6 border-t border-gray-200">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <h4 className="font-medium mb-2">Informações do produto</h4>
                  <div className="text-sm text-gray-600 space-y-1">
                    <div>Marca: {product.brand}</div>
                    <div>SKU: {product.sku}</div>
                    <div>Garantia: {product.warranty}</div>
                  </div>
                </div>
                <div>
                  <h4 className="font-medium mb-2">Vendedor</h4>
                  <div className="text-sm text-gray-600 space-y-1">
                    <div>{product.seller.name}</div>
                    <div>{product.seller.location}</div>
                    <div>{product.seller.positiveRating}% de avaliações positivas</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}

        {activeTab === 'reviews' && (
          <div>
            <h3 className="text-lg font-semibold mb-4">Avaliações dos clientes</h3>
            
            {/* Resumo das avaliações */}
            <div className="bg-gray-50 rounded-lg p-4 mb-6">
              <div className="flex items-center gap-4 mb-4">
                <div className="text-3xl font-bold">{product.rating.average.toFixed(1)}</div>
                <div>
                  <div className="flex items-center mb-1">
                    {renderStars(product.rating.average)}
                  </div>
                  <div className="text-sm text-gray-600">
                    {product.rating.count} avaliações
                  </div>
                </div>
              </div>

              {/* Distribuição das estrelas */}
              <div className="space-y-2">
                {[5, 4, 3, 2, 1].map((stars) => (
                  <div key={stars} className="flex items-center gap-2">
                    <div className="text-sm w-8">{stars}★</div>
                    <div className="flex-1 bg-gray-200 rounded-full h-2">
                      <div
                        className="bg-yellow-400 h-2 rounded-full"
                        style={{
                          width: `${getPercentage(
                            product.rating.distribution[stars as keyof typeof product.rating.distribution],
                            product.rating.count
                          )}%`,
                        }}
                      />
                    </div>
                    <div className="text-sm text-gray-600 w-12">
                      {getPercentage(
                        product.rating.distribution[stars as keyof typeof product.rating.distribution],
                        product.rating.count
                      )}%
                    </div>
                  </div>
                ))}
              </div>
            </div>

            {/* Placeholder para avaliações individuais */}
            <div className="text-center py-8 text-gray-500">
              <p>As avaliações individuais dos clientes serão exibidas aqui.</p>
              <p className="text-sm mt-2">Esta funcionalidade será implementada em breve.</p>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}