"use client"

import React from 'react';
import { Search, ShoppingCart, User } from 'lucide-react';

export default function Header() {
  return (
    <header className="bg-yellow-400 px-4 py-3">
      <div className="max-w-6xl mx-auto flex items-center justify-between">
        <div className="flex items-center space-x-4">
          <h1 className="text-xl font-bold text-blue-900">MercadoClone</h1>
          <div className="relative">
            <input 
              type="text" 
              placeholder="Buscar produtos, marcas e muito mais..."
              className="w-96 px-4 py-2 pr-12 rounded-sm border-none outline-none text-gray-700"
            />
            <button className="absolute right-3 top-1/2 transform -translate-y-1/2">
              <Search size={20} className="text-gray-500" />
            </button>
          </div>
        </div>
        
        <div className="flex items-center space-x-6">
          <div className="flex items-center space-x-2 text-blue-900 hover:text-blue-700 cursor-pointer">
            <User size={20} />
            <span className="text-sm">Minha conta</span>
          </div>
          <div className="flex items-center space-x-2 text-blue-900 hover:text-blue-700 cursor-pointer">
            <ShoppingCart size={20} />
            <span className="text-sm">Carrinho</span>
          </div>
        </div>
      </div>
    </header>
  );
}