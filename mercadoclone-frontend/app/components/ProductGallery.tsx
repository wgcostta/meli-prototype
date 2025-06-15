"use client"

import React, { useState } from 'react';
import { ChevronLeft, ChevronRight, ZoomIn } from 'lucide-react';
import { Product } from '@/types/product';

interface ProductGalleryProps {
  product: Product;
}

export default function ProductGallery({ product }: ProductGalleryProps) {
  const [selectedImage, setSelectedImage] = useState(0);

  // Handle case when no images are available
  if (!product.images || product.images.length === 0) {
    return (
      <div className="lg:col-span-2">
        <div className="bg-white rounded-lg p-6 shadow-sm">
          <div className="flex items-center justify-center bg-gray-50 rounded-lg min-h-[400px]">
            <div className="text-center text-gray-500">
              <div className="text-lg mb-2">📷</div>
              <p>No images available</p>
            </div>
          </div>
        </div>
      </div>
    );
  }

  // Sort images by order
  const sortedImages = product.images.sort((a, b) => a.order - b.order);

  const nextImage = () => {
    setSelectedImage((prev) => (prev + 1) % sortedImages.length);
  };

  const prevImage = () => {
    setSelectedImage((prev) => (prev - 1 + sortedImages.length) % sortedImages.length);
  };

  return (
    <div className="lg:col-span-2">
      <div className="bg-white rounded-lg p-6 shadow-sm">
        <div className="flex gap-4">
          {/* Vertical Thumbnails */}
          <div className="flex flex-col space-y-2 max-h-96 overflow-y-auto">
            {sortedImages.map((img, index) => (
              <button
                key={img.id} // Use unique image id as key
                onClick={() => setSelectedImage(index)}
                className={`flex-shrink-0 w-16 h-16 rounded border-2 overflow-hidden transition-all ${
                  selectedImage === index 
                    ? 'border-blue-500 ring-2 ring-blue-200' 
                    : 'border-gray-200 hover:border-gray-300'
                }`}
              >
                <img 
                  src={img.url} 
                  alt={img.alt || `${product.title} ${index + 1}`} 
                  className="w-full h-full object-cover" 
                />
              </button>
            ))}
          </div>

          {/* Main Image */}
          <div className="relative flex-1 group" data-testid="main-image-container">
            <div className="flex items-center justify-center bg-gray-50 rounded-lg min-h-[400px] max-h-[600px]">
              <img 
                src={sortedImages[selectedImage].url} 
                alt={sortedImages[selectedImage].alt || product.title}
                className="max-w-full max-h-[600px] object-contain rounded-lg cursor-zoom-in"
              />
            </div>
            
            {/* Navigation Controls */}
            <button 
              onClick={prevImage}
              className="absolute left-4 top-1/2 transform -translate-y-1/2 bg-white p-2 rounded-full shadow-lg opacity-0 group-hover:opacity-100 transition-opacity hover:bg-gray-50"
              aria-label="Previous image"
            >
              <ChevronLeft size={20} data-testid="chevron-left-icon" />
            </button>
            
            <button 
              onClick={nextImage}
              className="absolute right-4 top-1/2 transform -translate-y-1/2 bg-white p-2 rounded-full shadow-lg opacity-0 group-hover:opacity-100 transition-opacity hover:bg-gray-50"
              aria-label="Next image"
            >
              <ChevronRight size={20} data-testid="chevron-right-icon" />
            </button>
            
            {/* Zoom Button */}
            <button 
              className="absolute top-4 right-4 bg-white p-2 rounded-full shadow-lg opacity-0 group-hover:opacity-100 transition-opacity hover:bg-gray-50"
              aria-label="Zoom image"
            >
              <ZoomIn size={20} data-testid="zoom-icon" />
            </button>
            
            {/* Position Indicator */}
            <div className="absolute bottom-4 left-1/2 transform -translate-x-1/2 bg-black bg-opacity-50 text-white px-3 py-1 rounded-full text-sm">
              {selectedImage + 1} / {sortedImages.length}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}