import { NextResponse } from 'next/server';

const products = [
  {
    id: 1,
    title: "Smartphone XYZ 256GB - Preto",
    description: "Um smartphone poderoso com câmera de alta resolução e desempenho excepcional.",
    price: 2999.99,
    images: [
      "https://via.placeholder.com/400x400?text=Produto+1",
      "https://via.placeholder.com/400x400?text=Produto+2",
    ],
    paymentMethods: ["Cartão de Crédito", "Pix", "Boleto"],
    seller: {
      name: "Loja Confiável",
      rating: 4.8,
    },
    stock: 15,
    reviews: [
      { id: 1, comment: "Ótimo produto!", rating: 5 },
      { id: 2, comment: "Entrega rápida!", rating: 4 },
    ],
  },
];

export async function GET(request: Request) {
  const { searchParams } = new URL(request.url);
  const id = searchParams.get('id');
  const product = products.find(p => p.id === Number(id)) || products[0];
  return NextResponse.json(product);
}