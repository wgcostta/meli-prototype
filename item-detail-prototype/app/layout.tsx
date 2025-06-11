import type { Metadata } from 'next'
import './global.css'

export const metadata: Metadata = {
  title: 'MercadoClone - Fone de Ouvido Bluetooth Premium',
  description: 'Fone de Ouvido Bluetooth Premium com cancelamento ativo de ruído. Som de alta qualidade, bateria de longa duração e design confortável.',
  keywords: ['fone de ouvido', 'bluetooth', 'cancelamento de ruído', 'wireless', 'premium'],
  openGraph: {
    title: 'Fone de Ouvido Bluetooth Premium - MercadoClone',
    description: 'Som de alta qualidade com cancelamento ativo de ruído',
    images: ['https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=1200&h=630&fit=crop'],
  },
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="pt-BR">
      <body className="antialiased">
        {children}
      </body>
    </html>
  )
}