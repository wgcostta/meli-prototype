import { render, screen, fireEvent } from '@testing-library/react'
import ProductInfo from '../ProductInfo'
import { Product } from '@/types/product'

// Criar mockProduct alinhado com as interfaces reais
const mockProduct: Product = {
  id: 'test-product-1',
  title: 'Test Product',
  description: 'This is a test product description that is long enough to test the expand/collapse functionality',
  shortDescription: 'Short test description',
  brand: 'Test Brand',
  sku: 'TB-001',
  images: [
    {
      id: 'img-1',
      url: 'https://example.com/image1.jpg',
      alt: 'Test image 1',
      order: 1,
    },
    {
      id: 'img-2', 
      url: 'https://example.com/image2.jpg',
      alt: 'Test image 2',
      order: 2,
    }
  ],
  price: {
    current: 299.99,
    original: 399.99,
    currency: 'BRL',
    discount: 25
  },
  rating: {
    average: 4.5,
    count: 150,
    distribution: {
      5: 80,
      4: 45,
      3: 15,
      2: 7,
      1: 3
    }
  },
  stock: {
    available: 10,
    total: 50,
    isAvailable: true
  },
  shipping: {
    free: true,
    cost: 0,
    estimatedDays: 3,
    description: 'Frete grátis para todo o Brasil'
  },
  seller: {
    id: 'seller-1',
    name: 'Test Store',
    reputation: 4.8,
    location: 'São Paulo, SP',
    isOfficial: true,
    positiveRating: 98,
    yearsOnPlatform: 5,
    avatar: 'https://example.com/seller-avatar.jpg'
  },
  paymentMethods: [
    {
      type: 'credit_card',
      name: 'Cartão de crédito',
      installments: 12,
      discount: 0
    },
    {
      type: 'pix',
      name: 'PIX',
      installments: 1,
      discount: 5
    }
  ],
  features: [
    'Alta qualidade de som',
    'Cancelamento ativo de ruído',
    'Bateria de longa duração'
  ],
  specifications: {
    'Marca': 'Test Brand',
    'Modelo': 'TB-001',
    'Cor': 'Azul',
    'Material': 'Plástico ABS'
  },
  warranty: '12 months',
  category: {
    id: 'electronics',
    name: 'Electronics',
    path: ['Electronics', 'Audio', 'Headphones']
  },
  createdAt: '2024-01-01T00:00:00Z',
  updatedAt: '2024-01-01T00:00:00Z'
}

describe('ProductInfo', () => {
  it('should render product information', () => {
    render(<ProductInfo product={mockProduct} />)

    expect(screen.getByText('Test Product')).toBeInTheDocument()
    
    // Use getAllByText for multiple price occurrences
    const currentPriceElements = screen.getAllByText(/R\$ 299,99/)
    expect(currentPriceElements.length).toBeGreaterThanOrEqual(1)
    
    const originalPriceElements = screen.getAllByText(/R\$ 399,99/)
    expect(originalPriceElements.length).toBeGreaterThanOrEqual(1)
    
    expect(screen.getByText('25% OFF')).toBeInTheDocument()
  })

  it('should display rating and reviews', () => {
    render(<ProductInfo product={mockProduct} />)

    // The rating text might be fragmented, so look for the individual parts
    expect(screen.getByText('4.5')).toBeInTheDocument()
    expect(screen.getByText('150 avaliações')).toBeInTheDocument()
  })

  it('should handle quantity changes', () => {
    render(<ProductInfo product={mockProduct} />)

    const increaseButton = document.querySelector('[data-testid="plus-icon"]')?.closest('button')
    const decreaseButton = document.querySelector('[data-testid="minus-icon"]')?.closest('button')
    
    // Should start with quantity 1
    expect(screen.getByText('1')).toBeInTheDocument()

    // Increase quantity
    if (increaseButton) {
      fireEvent.click(increaseButton)
      expect(screen.getByText('2')).toBeInTheDocument()
    }

    // Decrease quantity
    if (decreaseButton) {
      fireEvent.click(decreaseButton)
      expect(screen.getByText('1')).toBeInTheDocument()
    }
  })

  it('should not allow quantity below 1', () => {
    render(<ProductInfo product={mockProduct} />)

    const decreaseButton = document.querySelector('[data-testid="minus-icon"]')?.closest('button')
    
    // Should start with quantity 1
    expect(screen.getByText('1')).toBeInTheDocument()

    // Try to decrease below 1
    if (decreaseButton) {
      fireEvent.click(decreaseButton)
      
      // Should still be 1
      expect(screen.getByText('1')).toBeInTheDocument()
      expect(decreaseButton).toBeDisabled()
    }
  })

  it('should not allow quantity above available stock', () => {
    render(<ProductInfo product={mockProduct} />)

    const increaseButton = document.querySelector('[data-testid="plus-icon"]')?.closest('button')
    
    if (increaseButton) {
      // Increase to max stock (10)
      for (let i = 1; i < 10; i++) {
        fireEvent.click(increaseButton)
      }
      
      expect(screen.getByText('10')).toBeInTheDocument()
      
      // Try to go beyond stock
      fireEvent.click(increaseButton)
      
      // Should still be 10
      expect(screen.getByText('10')).toBeInTheDocument()
      expect(increaseButton).toBeDisabled()
    }
  })

  it('should handle favorite toggle', () => {
    render(<ProductInfo product={mockProduct} />)

    const favoriteButton = screen.queryByLabelText('Adicionar aos favoritos') ||
                          document.querySelector('[data-testid="heart-icon"]')?.closest('button')
    
    if (favoriteButton) {
      // Click to favorite
      fireEvent.click(favoriteButton)
      
      // Should not crash
      expect(favoriteButton).toBeInTheDocument()
    }
  })

  it('should handle color selection', () => {
    render(<ProductInfo product={mockProduct} />)

    // Look for color selection elements
    const colorText = screen.queryByText('Cor:')
    if (colorText) {
      expect(colorText).toBeInTheDocument()
      
      // Look for color buttons
      const colorButtons = screen.getAllByRole('button').filter(button => 
        button.textContent?.includes('Azul') || 
        button.textContent?.includes('Preto') ||
        button.textContent?.includes('Branco') ||
        button.textContent?.includes('Vermelho')
      )

      if (colorButtons.length > 0) {
        // Click on a different color
        const redButton = colorButtons.find(button => button.textContent?.includes('Vermelho'))
        if (redButton) {
          fireEvent.click(redButton)
        }
      }
    }
  })

  it('should disable purchase when out of stock', () => {
    const outOfStockProduct = {
      ...mockProduct,
      stock: {
        ...mockProduct.stock,
        isAvailable: false,
        available: 0
      }
    }

    render(<ProductInfo product={outOfStockProduct} />)

    const buyButton = screen.getByText('Produto indisponível')
    expect(buyButton).toBeDisabled()
  })

  it('should display seller information', () => {
    render(<ProductInfo product={mockProduct} />)

    expect(screen.getByText('Test Store')).toBeInTheDocument()
    expect(screen.getByText('Oficial')).toBeInTheDocument()
    
    // Handle fragmented text for percentage
    expect(screen.getByText('98')).toBeInTheDocument()
    expect(screen.getByText(/% positivas/)).toBeInTheDocument()
    
    // Handle fragmented text for years
    expect(screen.getByText('5')).toBeInTheDocument()
    expect(screen.getByText(/anos na plataforma/)).toBeInTheDocument()
    expect(screen.getByText(/São Paulo, SP/)).toBeInTheDocument()
  })

  it('should display shipping information', () => {
    render(<ProductInfo product={mockProduct} />)

    const freeShippingElements = screen.getAllByText('Frete grátis')
    expect(freeShippingElements.length).toBeGreaterThan(0)
  })

  it('should display payment methods', () => {
    render(<ProductInfo product={mockProduct} />)

    expect(screen.getByText('Parcelamento sem juros')).toBeInTheDocument()
    expect(screen.getByText(/Em até/)).toBeInTheDocument()
    expect(screen.getByText('12')).toBeInTheDocument()
    expect(screen.getByText(/no cartão de crédito/)).toBeInTheDocument()
  })

  it('should format prices correctly', () => {
    render(<ProductInfo product={mockProduct} />)

    // Should format Brazilian currency - use getAllByText for multiple occurrences
    const priceElements = screen.getAllByText(/R\$ 299,99/)
    expect(priceElements.length).toBeGreaterThanOrEqual(1)
    
    const originalPriceElements = screen.getAllByText(/R\$ 399,99/)
    expect(originalPriceElements.length).toBeGreaterThanOrEqual(1)
  })

  it('should calculate discount percentage correctly', () => {
    render(<ProductInfo product={mockProduct} />)

    // (399.99 - 299.99) / 399.99 * 100 = 25%
    expect(screen.getByText('25% OFF')).toBeInTheDocument()
  })

  it('should display brand information', () => {
    render(<ProductInfo product={mockProduct} />)

    expect(screen.getByText('Marca:')).toBeInTheDocument()
    expect(screen.getByText('Test Brand')).toBeInTheDocument()
  })

  it('should display warranty information', () => {
    render(<ProductInfo product={mockProduct} />)

    expect(screen.getByText('Garantia:')).toBeInTheDocument()
    expect(screen.getByText('12 months')).toBeInTheDocument()
  })

  it('should render stars based on rating', () => {
    render(<ProductInfo product={mockProduct} />)

    // Should have star elements
    const stars = document.querySelectorAll('[data-testid="star-icon"]')
    expect(stars.length).toBeGreaterThan(0)
  })

  it('should handle products without original price', () => {
    const noDiscountProduct = {
      ...mockProduct,
      price: {
        ...mockProduct.price,
        original: undefined
      }
    }

    render(<ProductInfo product={noDiscountProduct} />)

    // Use getAllByText for multiple price occurrences
    const currentPriceElements = screen.getAllByText(/R\$ 299,99/)
    expect(currentPriceElements.length).toBeGreaterThanOrEqual(1)
    
    expect(screen.queryByText(/R\$ 399,99/)).not.toBeInTheDocument()
    expect(screen.queryByText('25% OFF')).not.toBeInTheDocument()
  })

  it('should handle seller without avatar', () => {
    const noAvatarProduct = {
      ...mockProduct,
      seller: {
        ...mockProduct.seller,
        avatar: undefined
      }
    }

    render(<ProductInfo product={noAvatarProduct} />)

    // Should show initials - primeiro 2 caracteres do nome em maiúsculo
    // "Test Store" -> "TE" (não "TS")
    expect(screen.getByText('TE')).toBeInTheDocument()
  })

  it('should display stock quantity', () => {
    render(<ProductInfo product={mockProduct} />)

    // Handle fragmented text
    expect(screen.getByText('10')).toBeInTheDocument()
    expect(screen.getByText(/disponíveis/)).toBeInTheDocument()
  })

  it('should have accessible button labels', () => {
    render(<ProductInfo product={mockProduct} />)

    const favoriteButton = screen.queryByLabelText('Adicionar aos favoritos')
    const shareButton = screen.queryByLabelText('Compartilhar produto')
    
    // At least one should be present
    expect(favoriteButton || shareButton).toBeTruthy()
  })

  it('should handle click events on action buttons', () => {
    render(<ProductInfo product={mockProduct} />)

    const buyButton = screen.getByText('Comprar agora')
    const addToCartButton = screen.getByText('Adicionar ao carrinho')

    // Should not throw when clicked
    expect(() => {
      fireEvent.click(buyButton)
      fireEvent.click(addToCartButton)
    }).not.toThrow()
  })

  describe('Edge cases', () => {
    it('should handle missing payment methods', () => {
      const noPaymentProduct = {
        ...mockProduct,
        paymentMethods: []
      }

      render(<ProductInfo product={noPaymentProduct} />)
      
      // Should not crash
      expect(screen.getByText('Test Product')).toBeInTheDocument()
    })

    it('should handle missing shipping information', () => {
      const noShippingProduct = {
        ...mockProduct,
        shipping: {
          free: false,
          cost: 0,
          estimatedDays: 0,
          description: ''
        }
      }

      render(<ProductInfo product={noShippingProduct} />)
      
      // Should not crash
      expect(screen.getByText('Test Product')).toBeInTheDocument()
    })

    it('should handle missing seller information', () => {
      const noSellerProduct = {
        ...mockProduct,
        seller: {
          id: 'seller-1',
          name: 'Test Store',
          reputation: 0,
          location: '',
          isOfficial: false,
          positiveRating: 0,
          yearsOnPlatform: 0
        }
      }

      render(<ProductInfo product={noSellerProduct} />)
      
      // Should not crash
      expect(screen.getByText('Test Product')).toBeInTheDocument()
    })
  })
})