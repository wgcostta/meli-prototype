import React, { useState } from 'react';
import Image from 'next/image';
import { Product } from '@/types/product';

interface ProductGalleryProps {
  product: Product;
}

export default function ProductGallery({ product }: ProductGalleryProps) {
  const [selectedImageIndex, setSelectedImageIndex] = useState(0);
  const [isImageLoading, setIsImageLoading] = useState(true);

  const handleImageLoad = () => {
    setIsImageLoading(false);
  };

  if (!product.images || product.images.length === 0) {
    return (
      <div className="lg:col-span-2">
        <div className="bg-white rounded-lg p-6 shadow-sm">
          <div className="aspect-square bg-gray-200 rounded-lg flex items-center justify-center">
            <span className="text-gray-400">Sem imagem disponível</span>
          </div>
        </div>
      </div>
    );
  }

  const selectedImage = product.images[selectedImageIndex];

  return (
    <div className="lg:col-span-2">
      <div className="bg-white rounded-lg p-6 shadow-sm">
        {/* Imagem principal */}
        <div className="relative aspect-square mb-4 bg-gray-100 rounded-lg overflow-hidden">
          {isImageLoading && (
            <div className="absolute inset-0 flex items-center justify-center">
              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
            </div>
          )}
          <Image
            src={selectedImage.url}
            alt={selectedImage.alt}
            fill
            className="object-cover"
            onLoad={handleImageLoad}
            priority={selectedImageIndex === 0}
          />
        </div>

        {/* Thumbnails */}
        {product.images.length > 1 && (
          <div className="flex gap-2 overflow-x-auto pb-2">
            {product.images.map((image, index) => (
              <button
                key={image.id}
                onClick={() => {
                  setSelectedImageIndex(index);
                  setIsImageLoading(true);
                }}
                className={`relative flex-shrink-0 w-16 h-16 rounded-md overflow-hidden border-2 transition-colors ${
                  index === selectedImageIndex
                    ? 'border-blue-600'
                    : 'border-gray-200 hover:border-gray-300'
                }`}
              >
                <Image
                  src={image.url}
                  alt={image.alt}
                  fill
                  className="object-cover"
                />
              </button>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}