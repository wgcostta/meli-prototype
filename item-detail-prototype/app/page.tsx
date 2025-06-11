'use client';

import Image from 'next/image';
import { useState } from 'react';
import { Heart, Share2, Star, Shield, Truck, CreditCard, MapPin } from 'lucide-react';

// Componente de Galeria de Imagens
function ImageGallery({ images }: { images: string[] }) {
  const [selectedImage, setSelectedImage] = useState(images[0]);

  return (
    <div className="flex flex-col gap-4">
      {/* Imagem Principal */}
      <div className="relative w-full h-[500px] bg-white rounded-lg overflow-hidden border">
        <Image
          src={selectedImage}
          alt="Produto"
          fill
          className="object-contain p-4"
          priority
        />
      </div>
      {/* Miniaturas */}
      <div className="flex gap-2 overflow-x-auto pb-2">
        {images.map((image, index) => (
          <button
            key={index}
            onClick={() => setSelectedImage(image)}
            className={`relative w-16 h-16 rounded-md overflow-hidden border-2 flex-shrink-0 ${
              selectedImage === image ? 'border-blue-500' : 'border-gray-200'
            }`}
          >
            <Image
              src={image}
              alt={`Miniatura ${index + 1}`}
              fill
              className="object-contain p-1"
            />
          </button>
        ))}
      </div>
    </div>
  );
}

// Componente Principal
export default function ProductPage() {
  const [selectedColor, setSelectedColor] = useState('Azul escuro');
  const [selectedStorage, setSelectedStorage] = useState('256 GB');
  const [quantity, setQuantity] = useState(1);
  const [isFavorite, setIsFavorite] = useState(false);

  // Dados do produto
  const product = {
    title: 'Samsung Galaxy A55 5G Dual SIM 256 GB azul escuro 8 GB RAM',
    price: 1899.99,
    originalPrice: 2299.99,
    discount: 17,
    rating: 4.5,
    reviews: 2847,
    seller: 'Samsung',
    isOfficialStore: true,
    shipping: 'Frete grátis',
    images: [
      'https://http2.mlstatic.com/D_NQ_NP_2X_678234-MLU74551621196_022024-F.webp',
      'https://http2.mlstatic.com/D_NQ_NP_2X_985656-MLU74551621200_022024-F.webp',
      'https://http2.mlstatic.com/D_NQ_NP_2X_951442-MLU74551621202_022024-F.webp',
      'https://http2.mlstatic.com/D_NQ_NP_2X_727727-MLU74551621204_022024-F.webp'
    ]
  };

  const colors = [
    { name: 'Azul escuro', color: '#1a365d' },
    { name: 'Violeta', color: '#6b46c1' },
    { name: 'Lima', color: '#84cc16' }
  ];

  const storageOptions = ['128 GB', '256 GB'];

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header simplificado */}
      <header className="bg-yellow-400 p-4">
        <div className="max-w-7xl mx-auto">
          <h1 className="text-2xl font-bold text-gray-800">MercadoLibre</h1>
        </div>
      </header>

      {/* Breadcrumb */}
      <nav className="bg-white border-b">
        <div className="max-w-7xl mx-auto px-4 py-3">
          <div className="text-sm text-gray-600">
            <span>Celulares e Telefones</span> &gt; 
            <span className="ml-1">Celulares e Smartphones</span> &gt; 
            <span className="ml-1">Samsung</span>
          </div>
        </div>
      </nav>

      {/* Conteúdo Principal */}
      <div className="max-w-7xl mx-auto px-4 py-6">
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* Galeria de Imagens */}
          <div className="space-y-4">
            <ImageGallery images={product.images} />
            
            {/* Botões de ação */}
            <div className="flex gap-4">
              <button
                onClick={() => setIsFavorite(!isFavorite)}
                className={`flex items-center gap-2 px-4 py-2 rounded-lg border ${
                  isFavorite ? 'bg-red-50 border-red-200 text-red-600' : 'bg-white border-gray-200'
                }`}
              >
                <Heart className={`w-5 h-5 ${isFavorite ? 'fill-current' : ''}`} />
                Favoritos
              </button>
              <button className="flex items-center gap-2 px-4 py-2 rounded-lg border bg-white border-gray-200">
                <Share2 className="w-5 h-5" />
                Compartilhar
              </button>
            </div>
          </div>

          {/* Informações do Produto */}
          <div className="space-y-6">
            {/* Título e Avaliações */}
            <div>
              <h1 className="text-2xl font-normal text-gray-800 mb-2">
                {product.title}
              </h1>
              <div className="flex items-center gap-2 mb-4">
                <div className="flex items-center gap-1">
                  {[...Array(5)].map((_, i) => (
                    <Star
                      key={i}
                      className={`w-4 h-4 ${
                        i < Math.floor(product.rating) ? 'text-yellow-400 fill-current' : 'text-gray-300'
                      }`}
                    />
                  ))}
                </div>
                <span className="text-sm text-gray-600">
                  {product.rating} ({product.reviews.toLocaleString()} opiniões)
                </span>
              </div>
            </div>

            {/* Preço */}
            <div className="space-y-2">
              <div className="flex items-center gap-2">
                <span className="text-sm bg-green-100 text-green-800 px-2 py-1 rounded">
                  {product.discount}% OFF
                </span>
              </div>
              <div className="flex items-baseline gap-2">
                <span className="text-sm text-gray-500 line-through">
                  R$ {product.originalPrice.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}
                </span>
              </div>
              <div className="text-3xl font-light">
                R$ {product.price.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}
              </div>
              <div className="text-sm text-green-600">
                em 12x R$ {(product.price / 12).toLocaleString('pt-BR', { minimumFractionDigits: 2 })} sem juros
              </div>
            </div>

            {/* Vendedor */}
            <div className="flex items-center gap-2 text-sm">
              <span className="text-gray-600">Vendido por</span>
              <span className="font-semibold text-blue-600">{product.seller}</span>
              {product.isOfficialStore && (
                <span className="bg-blue-100 text-blue-800 px-2 py-1 rounded text-xs">
                  Loja oficial
                </span>
              )}
            </div>

            {/* Opções de Cor */}
            <div className="space-y-3">
              <h3 className="font-semibold">Cor: {selectedColor}</h3>
              <div className="flex gap-2">
                {colors.map((color) => (
                  <button
                    key={color.name}
                    onClick={() => setSelectedColor(color.name)}
                    className={`w-12 h-12 rounded-full border-2 ${
                      selectedColor === color.name ? 'border-blue-500' : 'border-gray-300'
                    }`}
                    style={{ backgroundColor: color.color }}
                    title={color.name}
                  />
                ))}
              </div>
            </div>

            {/* Opções de Armazenamento */}
            <div className="space-y-3">
              <h3 className="font-semibold">Armazenamento interno:</h3>
              <div className="flex gap-2">
                {storageOptions.map((storage) => (
                  <button
                    key={storage}
                    onClick={() => setSelectedStorage(storage)}
                    className={`px-4 py-2 rounded-lg border ${
                      selectedStorage === storage
                        ? 'border-blue-500 bg-blue-50 text-blue-600'
                        : 'border-gray-300 bg-white'
                    }`}
                  >
                    {storage}
                  </button>
                ))}
              </div>
            </div>

            {/* Quantidade */}
            <div className="space-y-3">
              <h3 className="font-semibold">Quantidade:</h3>
              <div className="flex items-center gap-2">
                <button
                  onClick={() => setQuantity(Math.max(1, quantity - 1))}
                  className="w-8 h-8 rounded border border-gray-300 flex items-center justify-center hover:bg-gray-50"
                >
                  -
                </button>
                <span className="w-12 text-center">{quantity}</span>
                <button
                  onClick={() => setQuantity(quantity + 1)}
                  className="w-8 h-8 rounded border border-gray-300 flex items-center justify-center hover:bg-gray-50"
                >
                  +
                </button>
                <span className="text-sm text-gray-600 ml-2">
                  (10 disponíveis)
                </span>
              </div>
            </div>

            {/* Botões de Compra */}
            <div className="space-y-3">
              <button className="w-full bg-blue-500 text-white py-3 rounded-lg font-semibold hover:bg-blue-600 transition-colors">
                Comprar agora
              </button>
              <button className="w-full bg-blue-50 text-blue-600 py-3 rounded-lg font-semibold hover:bg-blue-100 transition-colors">
                Adicionar ao carrinho
              </button>
            </div>

            {/* Informações de Entrega */}
            <div className="border-t pt-6 space-y-4">
              <div className="flex items-center gap-3">
                <Truck className="w-5 h-5 text-green-600" />
                <div>
                  <div className="font-semibold text-green-600">{product.shipping}</div>
                  <div className="text-sm text-gray-600">Chegará amanhã</div>
                </div>
              </div>
              
              <div className="flex items-center gap-3">
                <MapPin className="w-5 h-5 text-gray-400" />
                <div className="text-sm text-gray-600">
                  Calcular prazo de entrega
                </div>
              </div>

              <div className="flex items-center gap-3">
                <Shield className="w-5 h-5 text-blue-600" />
                <div>
                  <div className="font-semibold text-blue-600">Compra Garantida</div>
                  <div className="text-sm text-gray-600">Receba o produto ou devolvemos o dinheiro</div>
                </div>
              </div>

              <div className="flex items-center gap-3">
                <CreditCard className="w-5 h-5 text-gray-400" />
                <div className="text-sm text-gray-600">
                  Pague com cartão em até 12x sem juros
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Especificações */}
        <div className="mt-12 bg-white rounded-lg p-6">
          <h2 className="text-xl font-semibold mb-6">Especificações principais</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="flex justify-between py-2 border-b border-gray-100">
              <span className="text-gray-600">Marca</span>
              <span className="font-medium">Samsung</span>
            </div>
            <div className="flex justify-between py-2 border-b border-gray-100">
              <span className="text-gray-600">Modelo</span>
              <span className="font-medium">Galaxy A55 5G</span>
            </div>
            <div className="flex justify-between py-2 border-b border-gray-100">
              <span className="text-gray-600">Memória RAM</span>
              <span className="font-medium">8 GB</span>
            </div>
            <div className="flex justify-between py-2 border-b border-gray-100">
              <span className="text-gray-600">Armazenamento interno</span>
              <span className="font-medium">256 GB</span>
            </div>
            <div className="flex justify-between py-2 border-b border-gray-100">
              <span className="text-gray-600">Tamanho da tela</span>
              <span className="font-medium">6.6&quot;</span>
            </div>
            <div className="flex justify-between py-2 border-b border-gray-100">
              <span className="text-gray-600">Resolução da tela</span>
              <span className="font-medium">2340x1080</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}