"use client"

import React, { useState } from 'react';
import { ChevronDown, ChevronUp } from 'lucide-react';

export default function ProductDescription() {
  const [activeTab, setActiveTab] = useState('description');
  const [showFullDescription, setShowFullDescription] = useState(false);

  const tabs = [
    { id: 'description', label: 'Descrição' },
    { id: 'specifications', label: 'Especificações' },
    { id: 'reviews', label: 'Avaliações' },
    { id: 'questions', label: 'Perguntas' }
  ];

  const specifications = [
    { label: 'Marca', value: 'TechSound' },
    { label: 'Modelo', value: 'Premium BT-2024' },
    { label: 'Tipo', value: 'Over-ear' },
    { label: 'Conectividade', value: 'Bluetooth 5.0' },
    { label: 'Drivers', value: '40mm neodímio' },
    { label: 'Resposta de frequência', value: '20Hz - 20kHz' },
    { label: 'Impedância', value: '32 ohms' },
    { label: 'Bateria', value: 'Li-ion 800mAh' },
    { label: 'Autonomia', value: 'Até 30 horas' },
    { label: 'Tempo de carregamento', value: '2 horas' },
    { label: 'Peso', value: '250g' },
    { label: 'Alcance Bluetooth', value: 'Até 10 metros' },
    { label: 'Cancelamento de ruído', value: 'Ativo (ANC)' },
    { label: 'Compatibilidade', value: 'iOS, Android, Windows' }
  ];

  const reviews = [
    {
      name: 'João Silva',
      rating: 5,
      date: '2024-06-01',
      comment: 'Excelente fone! Som muito claro e o cancelamento de ruído funciona perfeitamente.'
    },
    {
      name: 'Maria Santos',
      rating: 4,
      date: '2024-05-28',
      comment: 'Muito bom, bateria dura bastante. Só achei um pouco pesado para usar por muito tempo.'
    },
    {
      name: 'Pedro Oliveira',
      rating: 5,
      date: '2024-05-25',
      comment: 'Superou minhas expectativas! Recomendo muito.'
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
              <p className="mb-4">
                Experimente a qualidade de áudio superior com nosso Fone de Ouvido Bluetooth Premium. 
                Desenvolvido com tecnologia de ponta, oferece cancelamento ativo de ruído e som cristalino 
                para uma experiência auditiva incomparável.
              </p>
              
              {showFullDescription && (
                <>
                  <p className="mb-4">
                    Com design ergonômico e materiais premium, este fone foi criado para proporcionar 
                    conforto durante longas sessões de uso. Os drivers de 40mm garantem reprodução 
                    fiel em todas as frequências, desde graves profundos até agudos cristalinos.
                  </p>
                  
                  <p className="mb-4">
                    A tecnologia de cancelamento ativo de ruído (ANC) elimina até 95% dos ruídos 
                    externos, permitindo que você se concentre completamente na sua música, podcasts 
                    ou chamadas. Perfeito para viagens, trabalho ou relaxamento em casa.
                  </p>
                </>
              )}

              <button
                onClick={() => setShowFullDescription(!showFullDescription)}
                className="flex items-center space-x-2 text-blue-600 hover:text-blue-700 font-medium"
              >
                <span>{showFullDescription ? 'Ver menos' : 'Ver mais'}</span>
                {showFullDescription ? <ChevronUp size={16} /> : <ChevronDown size={16} />}
              </button>

              <h3 className="font-semibold mt-6 mb-3">Características principais:</h3>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                {[
                  'Cancelamento ativo de ruído (ANC)',
                  'Bluetooth 5.0 com conexão estável',
                  'Bateria de longa duração - até 30 horas',
                  'Carregamento rápido - 15 min = 3 horas de uso',
                  'Controles touch intuitivos',
                  'Compatível com assistentes de voz',
                  'Design ergonômico e confortável',
                  'Microfone integrado para chamadas'
                ].map((feature, index) => (
                  <div key={index} className="flex items-center space-x-2">
                    <span className="w-2 h-2 bg-blue-500 rounded-full"></span>
                    <span className="text-sm">{feature}</span>
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}

        {activeTab === 'specifications' && (
          <div>
            <h2 className="text-xl font-semibold mb-4">Especificações técnicas</h2>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {specifications.map((spec, index) => (
                <div key={index} className="flex justify-between py-2 border-b border-gray-100">
                  <span className="font-medium text-gray-600">{spec.label}:</span>
                  <span className="text-gray-900">{spec.value}</span>
                </div>
              ))}
            </div>
          </div>
        )}

        {activeTab === 'reviews' && (
          <div>
            <h2 className="text-xl font-semibold mb-4">Avaliações dos compradores</h2>
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
                Ver todas as avaliações
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
                  P: O fone funciona com iPhone?
                </p>
                <p className="text-gray-700">
                  R: Sim, funciona perfeitamente com iPhone e todos os dispositivos iOS via Bluetooth.
                </p>
                <span className="text-xs text-gray-500 mt-2 block">Respondido pelo vendedor</span>
              </div>
              
              <div className="bg-gray-50 p-4 rounded-lg">
                <p className="font-medium text-gray-900 mb-2">
                  P: A bateria realmente dura 30 horas?
                </p>
                <p className="text-gray-700">
                  R: Sim, com o ANC desligado pode chegar até 30 horas. Com ANC ligado, dura cerca de 20 horas.
                </p>
                <span className="text-xs text-gray-500 mt-2 block">Respondido pelo vendedor</span>
              </div>
              
              <div className="bg-gray-50 p-4 rounded-lg">
                <p className="font-medium text-gray-900 mb-2">
                  P: Vem com cabo USB-C?
                </p>
                <p className="text-gray-700">
                  R: Sim, acompanha cabo USB-C para carregamento e cabo auxiliar P2 para uso com fio.
                </p>
                <span className="text-xs text-gray-500 mt-2 block">Respondido pelo vendedor</span>
              </div>
            </div>
            
            <div className="mt-6">
              <h3 className="font-medium mb-3">Faça uma pergunta</h3>
              <div className="flex space-x-3">
                <input
                  type="text"
                  placeholder="Digite sua pergunta sobre o produto..."
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