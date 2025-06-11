"use client"

import React from 'react';
import Header from './components/Header';
import Breadcrumb from './components/Breadcrumb';
import ProductGallery from './components/ProductGallery';
import ProductInfo from './components/ProductInfo';
import ProductDescription from './components/ProductDescription';

export default function ProductPage() {
  return (
    <div className="min-h-screen bg-gray-50">
      <Header />
      <Breadcrumb />
      
      <div className="max-w-6xl mx-auto px-4 py-6">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          <ProductGallery />
          <ProductInfo />
        </div>
        <ProductDescription />
      </div>
    </div>
  );
}