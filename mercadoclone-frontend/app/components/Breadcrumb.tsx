"use client"

import React from 'react';
import { ChevronRight } from 'lucide-react';
import { Product } from '@/types/product';

interface BreadcrumbProps {
  product?: Product;
}

export default function Breadcrumb({ product }: BreadcrumbProps) {
  // Breadcrumb padrão quando produto não está carregado
  const defaultBreadcrumb = [
    { label: 'Início', href: '/' },
    { label: 'Eletrônicos', href: '/eletronicos' },
    { label: 'Áudio', href: '/eletronicos/audio' },
    { label: 'Fones de Ouvido', href: '/eletronicos/audio/fones' },
    { label: 'Bluetooth', href: '/eletronicos/audio/fones/bluetooth' }
  ];

  // Construir breadcrumb a partir dos dados do produto
  const buildBreadcrumbFromProduct = (product: Product) => {
    const breadcrumbItems = [
      { label: 'Início', href: '/' }
    ];

    // Adicionar categoria e subcategorias
    if (product.category.path && product.category.path.length > 0) {
      product.category.path.forEach((categoryName, index) => {
        const href = `/${product.category.path.slice(0, index + 1).join('/')}`;
        breadcrumbItems.push({
          label: categoryName,
          href: href.toLowerCase()
        });
      });
    }

    // Adicionar produto atual (sem link)
    breadcrumbItems.push({
      label: product.title.length > 50 
        ? product.title.substring(0, 50) + '...' 
        : product.title,
      href: '#'
    });

    return breadcrumbItems;
  };

  const breadcrumbItems = product 
    ? buildBreadcrumbFromProduct(product) 
    : defaultBreadcrumb;

  return (
    <nav 
      aria-label="Breadcrumb" 
      className="max-w-6xl mx-auto px-4 py-3 text-sm text-gray-600 bg-white border-b"
    >
      <div className="flex items-center space-x-2 flex-wrap">
        {breadcrumbItems.map((item, index) => (
          <React.Fragment key={index}>
            {index === breadcrumbItems.length - 1 ? (
              // Último item (produto atual) - sem link
              <span className="text-gray-900 font-medium">
                {item.label}
              </span>
            ) : (
              <a 
                href={item.href}
                className="hover:text-blue-600 transition-colors"
              >
                {item.label}
              </a>
            )}
            {index < breadcrumbItems.length - 1 && (
              <ChevronRight size={14} className="text-gray-400 flex-shrink-0" />
            )}
          </React.Fragment>
        ))}
      </div>
    </nav>
  );
}