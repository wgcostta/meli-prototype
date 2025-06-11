"use client"

import React from 'react';
import { ChevronRight } from 'lucide-react';

export default function Breadcrumb() {
  const breadcrumbItems = [
    { label: 'Início', href: '/' },
    { label: 'Eletrônicos', href: '/eletronicos' },
    { label: 'Áudio', href: '/eletronicos/audio' },
    { label: 'Fones de Ouvido', href: '/eletronicos/audio/fones' },
    { label: 'Bluetooth', href: '/eletronicos/audio/fones/bluetooth' }
  ];

  return (
    <nav className="max-w-6xl mx-auto px-4 py-3 text-sm text-gray-600 bg-white border-b">
      <div className="flex items-center space-x-2">
        {breadcrumbItems.map((item, index) => (
          <React.Fragment key={index}>
            <a 
              href={item.href}
              className="hover:text-blue-600 transition-colors"
            >
              {item.label}
            </a>
            {index < breadcrumbItems.length - 1 && (
              <ChevronRight size={14} className="text-gray-400" />
            )}
          </React.Fragment>
        ))}
      </div>
    </nav>
  );
}