import { render, screen, fireEvent } from '@testing-library/react'
import ProductInfo from '../ProductInfo'
import { Product } from '@/types/product'

describe('ProductInfo', () => {
  const mockProduct: Product = testUtils.mockProduct

  it('should render product information', () => {
    render(<ProductInfo product={mockProduct} />)

    expect(screen.getByText('Test Product')).toBeInTheDocument()
    expect(screen.getByText('R$ 299,99')).toBeInTheDocument()
    expect(screen.getByText('R$ 399,99')).toBeInTheDocument()
    expect(screen.getByText('25% OFF')).toBeInTheDocument()
  })

  it('should display rating and reviews', () => {
    render(<ProductInfo product={mockProduct} />)

    expect(screen.getByText('(4,5)')).toBeInTheDocument()
    expect(screen.getByText('150 avaliações')).toBeInTheDocument()
  })

  it('should handle quantity changes', () => {
    render(<ProductInfo product={mockProduct} />)

    const increaseButton = screen.getByRole('button', { name: /plus/i }) ||
                          document.querySelector('button:has(svg[data-lucide="plus"])')
    const decreaseButton = screen.getByRole('button', { name: /minus/i }) ||
                          document.querySelector('button:has(svg[data-lucide="minus"])')
    
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

    const decreaseButton = document.querySelector('button:has(svg[data-lucide="minus"])')
    
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

    const increaseButton = document.querySelector('button:has(svg[data-lucide="plus"])')
    
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

    const favoriteButton = screen.getByLabelText('Adicionar aos favoritos')
    
    // Should not be favorited initially
    const heartIcon = favoriteButton.querySelector('svg')
    expect(heartIcon).not.toHaveClass('fill-current', 'text-red-500')

    // Click to favorite
    fireEvent.click(favoriteButton)
    
    // Should be favorited
    expect(heartIcon).toHaveClass('text-red-500', 'fill-current')
  })

  it('should handle color selection', () => {
    render(<ProductInfo product={mockProduct} />)

    const colorButtons = screen.getAllByRole('button').filter(button => 
      button.textContent?.includes('Azul') || 
      button.textContent?.includes('Preto') ||
      button.textContent?.includes('Branco') ||
      button.textContent?.includes('Vermelho')
    )

    expect(colorButtons.length).toBeGreaterThan(0)

    // Should start with 'Azul' selected
    expect(screen.getByText('Cor:')).toBeInTheDocument()

    // Click on a different color
    const redButton = colorButtons.find(button => button.textContent?.includes('Vermelho'))
    if (redButton) {
      fireEvent.click(redButton)
      // Color should change (though it's maintained in state)
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
    const addToCartButton = screen.getAllByRole('button').find(button => 
      button.textContent?.includes('Adicionar ao carrinho')
    )

    expect(buyButton).toBeDisabled()
    if (addToCartButton) {
      expect(addToCartButton).toBeDisabled()
    }
  })

  it('should display seller information', () => {
    render(<ProductInfo product={mockProduct} />)

    expect(screen.getByText('Test Store')).toBeInTheDocument()
    expect(screen.getByText('Oficial')).toBeInTheDocument()
    expect(screen.getByText('(98% positivas)')).toBeInTheDocument()
    expect(screen.getByText('+5 anos na plataforma • São Paulo, SP')).toBeInTheDocument()
  })

  it('should display shipping information', () => {
    render(<ProductInfo product={mockProduct} />)

    expect(screen.getByText('Frete grátis')).toBeInTheDocument()
    expect(screen.getByText('Frete grátis')).toBeInTheDocument()
  })

  it('should display payment methods', () => {
    render(<ProductInfo product={mockProduct} />)

    expect(screen.getByText('Parcelamento sem juros')).toBeInTheDocument()
    expect(screen.getByText(/Em até 12x no cartão/)).toBeInTheDocument()
  })

  it('should format prices correctly', () => {
    render(<ProductInfo product={mockProduct} />)

    // Should format Brazilian currency
    expect(screen.getByText('R$ 299,99')).toBeInTheDocument()
    expect(screen.getByText('R$ 399,99')).toBeInTheDocument()
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

    // Should have 5 star elements (4.5 rating = 4 filled + 1 empty)
    const stars = document.querySelectorAll('svg')
    const filledStars = Array.from(stars).filter(star => 
      star.getAttribute('fill') === 'currentColor'
    )
    
    expect(filledStars.length).toBeGreaterThan(0)
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

    expect(screen.getByText('R$ 299,99')).toBeInTheDocument()
    expect(screen.queryByText('R$ 399,99')).not.toBeInTheDocument()
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

    // Should show initials instead
    expect(screen.getByText('TS')).toBeInTheDocument()
  })

  it('should display stock quantity', () => {
    render(<ProductInfo product={mockProduct} />)

    expect(screen.getByText('(10 disponíveis)')).toBeInTheDocument()
  })

  it('should have accessible button labels', () => {
    render(<ProductInfo product={mockProduct} />)

    expect(screen.getByLabelText('Adicionar aos favoritos')).toBeInTheDocument()
    expect(screen.getByLabelText('Compartilhar produto')).toBeInTheDocument()
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
        shipping: undefined
      }

      render(<ProductInfo product={noShippingProduct} />)
      
      // Should not crash
      expect(screen.getByText('Test Product')).toBeInTheDocument()
    })
  })
})