"use client"

import React, { useState } from 'react';
import { ChevronLeft, ChevronRight, ZoomIn } from 'lucide-react';

export default function ProductGallery() {
  const [selectedImage, setSelectedImage] = useState(0);

  const images = [
    'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500&h=500&fit=crop',
    'https://images.unsplash.com/photo-1484704849700-f032a568e944?w=500&h=500&fit=crop',
    'https://images.unsplash.com/photo-1560472354-b33ff0c44a43?w=500&h=500&fit=crop',
    'https://images.unsplash.com/photo-1572635196237-14b3f281503f?w=500&h=500&fit=crop',
    'https://images.unsplash.com/photo-1583394838336-acd977736f90?w=500&h=500&fit=crop'
  ];

  const nextImage = () => {
    setSelectedImage((prev) => (prev + 1) % images.length);
  };

  const prevImage = () => {
    setSelectedImage((prev) => (prev - 1 + images.length) % images.length);
  };

  return (
    <div className="lg:col-span-2">
      <div className="bg-white rounded-lg p-6 shadow-sm">
        {/* Imagem Principal */}
        <div className="relative mb-4 group">
          <img 
            src={images[selectedImage]} 
            alt="Produto"
            className="w-full h-96 object-cover rounded-lg cursor-zoom-in"
          />
          
          {/* Controles de navegação */}
          <button 
            onClick={prevImage}
            className="absolute left-4 top-1/2 transform -translate-y-1/2 bg-white p-2 rounded-full shadow-lg opacity-0 group-hover:opacity-100 transition-opacity hover:bg-gray-50"
          >
            <ChevronLeft size={20} />
          </button>
          
          <button 
            onClick={nextImage}
            className="absolute right-4 top-1/2 transform -translate-y-1/2 bg-white p-2 rounded-full shadow-lg opacity-0 group-hover:opacity-100 transition-opacity hover:bg-gray-50"
          >
            <ChevronRight size={20} />
          </button>
          
          {/* Botão de zoom */}
          <button className="absolute top-4 right-4 bg-white p-2 rounded-full shadow-lg opacity-0 group-hover:opacity-100 transition-opacity hover:bg-gray-50">
            <ZoomIn size={20} />
          </button>
          
          {/* Indicador de posição */}
          <div className="absolute bottom-4 left-1/2 transform -translate-x-1/2 bg-black bg-opacity-50 text-white px-3 py-1 rounded-full text-sm">
            {selectedImage + 1} / {images.length}
          </div>
        </div>

        {/* Miniaturas */}
        <div className="flex space-x-2 overflow-x-auto pb-2">
          {images.map((img, index) => (
            <button
              key={index}
              onClick={() => setSelectedImage(index)}
              className={`flex-shrink-0 w-16 h-16 rounded border-2 overflow-hidden transition-all ${
                selectedImage === index 
                  ? 'border-blue-500 ring-2 ring-blue-200' 
                  : 'border-gray-200 hover:border-gray-300'
              }`}
            >
              <img 
                src={img} 
                alt={`Produto ${index + 1}`} 
                className="w-full h-full object-cover" 
              />
            </button>
          ))}
        </div>
      </div>
    </div>
  );
}