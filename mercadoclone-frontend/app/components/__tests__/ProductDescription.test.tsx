import React from 'react'
import { render, screen, fireEvent } from '@testing-library/react'
import '@testing-library/jest-dom'
import ProductDescription from '../ProductDescription'
import { Product } from '@/types/product'

// Mock para os ícones do lucide-react
jest.mock('lucide-react', () => ({
  ChevronDown: () => <div data-testid="chevron-down-icon">ChevronDown</div>,
  ChevronUp: () => <div data-testid="chevron-up-icon">ChevronUp</div>
}))

const createMockProduct = (overrides: Partial<Product> = {}): Product => ({
  id: 'test-product-1',
  title: 'Test Product',
  description: 'Esta é uma descrição muito longa do produto que deve ter mais de 200 caracteres para testar a funcionalidade de expandir/colapsar. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
  shortDescription: 'Descrição curta do produto',
  brand: 'Test Brand',
  sku: 'TB-001',
  images: [
    {
      id: 'img-1',
      url: 'https://example.com/image1.jpg',
      alt: 'Test image 1',
      order: 1,
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
    count: 1250,
    distribution: {
      5: 800,
      4: 300,
      3: 100,
      2: 35,
      1: 15
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
    yearsOnPlatform: 5
  },
  paymentMethods: [
    {
      type: 'credit_card',
      name: 'Cartão de crédito',
      installments: 12,
      discount: 0
    }
  ],
  features: [
    'Alta qualidade de som',
    'Cancelamento ativo de ruído',
    'Bateria de longa duração',
    'Design ergonômico'
  ],
  specifications: {
    'Marca': 'Test Brand',
    'Modelo': 'TB-001',
    'Cor': 'Azul',
    'Material': 'Plástico ABS',
    'Peso': '250g',
    'Dimensões': '20x15x5cm'
  },
  warranty: '12 meses de garantia',
  category: {
    id: 'electronics',
    name: 'Electronics',
    path: ['Electronics', 'Audio', 'Headphones']
  },
  createdAt: '2024-01-01T00:00:00Z',
  updatedAt: '2024-01-01T00:00:00Z',
  ...overrides
})

describe('ProductDescription', () => {
  let mockProduct: Product

  beforeEach(() => {
    mockProduct = createMockProduct()
  })

  describe('Component Structure', () => {
    it('should render the component with all tab options', () => {
      render(<ProductDescription product={mockProduct} />)

      // Verificar se todos os tabs estão presentes
      expect(screen.getByText('Descrição')).toBeInTheDocument()
      expect(screen.getByText('Especificações')).toBeInTheDocument()
      expect(screen.getByText('Avaliações')).toBeInTheDocument()
      expect(screen.getByText('Perguntas')).toBeInTheDocument()
    })

    it('should have proper navigation structure with accessibility attributes', () => {
      render(<ProductDescription product={mockProduct} />)

      const nav = screen.getByLabelText('Product information tabs')
      expect(nav).toBeInTheDocument()
      expect(nav).toHaveAttribute('aria-label', 'Product information tabs')
    })

    it('should render with description tab active by default', () => {
      render(<ProductDescription product={mockProduct} />)

      const descriptionTab = screen.getByRole('tab', { name: 'Descrição' })
      expect(descriptionTab).toHaveAttribute('aria-selected', 'true')
      expect(descriptionTab).toHaveClass('border-blue-500')
    })
  })

  describe('Tab Navigation', () => {
    it('should switch to specifications tab when clicked', () => {
      render(<ProductDescription product={mockProduct} />)

      const specsTab = screen.getByRole('tab', { name: 'Especificações' })
      fireEvent.click(specsTab)

      expect(specsTab).toHaveAttribute('aria-selected', 'true')
      expect(specsTab).toHaveClass('border-blue-500')
      expect(screen.getByText('Especificações técnicas')).toBeInTheDocument()
    })

    it('should switch to reviews tab when clicked', () => {
      render(<ProductDescription product={mockProduct} />)

      const reviewsTab = screen.getByRole('tab', { name: 'Avaliações' })
      fireEvent.click(reviewsTab)

      expect(reviewsTab).toHaveAttribute('aria-selected', 'true')
      expect(screen.getByText('Avaliações dos compradores')).toBeInTheDocument()
    })

    it('should switch to questions tab when clicked', () => {
      render(<ProductDescription product={mockProduct} />)

      const questionsTab = screen.getByRole('tab', { name: 'Perguntas' })
      fireEvent.click(questionsTab)

      expect(questionsTab).toHaveAttribute('aria-selected', 'true')
      expect(screen.getByText('Perguntas e respostas')).toBeInTheDocument()
    })

    it('should maintain proper tabpanel structure', () => {
      render(<ProductDescription product={mockProduct} />)

      const tabpanel = screen.getByRole('tabpanel')
      expect(tabpanel).toHaveAttribute('id', 'tabpanel-description')
      expect(tabpanel).toHaveAttribute('aria-labelledby', 'tab-description')
    })
  })

  describe('Description Tab', () => {
    it('should display short description when available', () => {
      render(<ProductDescription product={mockProduct} />)

      expect(screen.getByText('Descrição curta do produto')).toBeInTheDocument()
    })

    it('should display truncated description when no short description and long description', () => {
      const productWithoutShortDesc = createMockProduct({
        shortDescription: undefined
      })

      render(<ProductDescription product={productWithoutShortDesc} />)

      // Deve mostrar versão truncada
      const content = document.body.textContent || ''
      expect(content).toContain('Esta é uma descrição muito longa')
      expect(screen.getByText('Ver mais')).toBeInTheDocument()
    })

    it('should expand/collapse description when toggle button is clicked', () => {
      const productWithoutShortDesc = createMockProduct({
        shortDescription: undefined
      })

      render(<ProductDescription product={productWithoutShortDesc} />)

      const expandButton = screen.getByText('Ver mais')
      expect(expandButton).toBeInTheDocument()

      // Clicar para expandir
      fireEvent.click(expandButton)
      expect(screen.getByText('Ver menos')).toBeInTheDocument()
      expect(screen.getByTestId('chevron-up-icon')).toBeInTheDocument()

      // Clicar para colapsar
      const collapseButton = screen.getByText('Ver menos')
      fireEvent.click(collapseButton)
      expect(screen.getByText('Ver mais')).toBeInTheDocument()
      expect(screen.getByTestId('chevron-down-icon')).toBeInTheDocument()
    })

    it('should not show expand/collapse for short descriptions', () => {
      const shortDescProduct = createMockProduct({
        description: 'Descrição curta'
      })

      render(<ProductDescription product={shortDescProduct} />)

      expect(screen.queryByText('Ver mais')).not.toBeInTheDocument()
      expect(screen.queryByText('Ver menos')).not.toBeInTheDocument()
    })

    it('should display product features when available', () => {
      render(<ProductDescription product={mockProduct} />)

      expect(screen.getByText('Características principais:')).toBeInTheDocument()
      expect(screen.getByText('Alta qualidade de som')).toBeInTheDocument()
      expect(screen.getByText('Cancelamento ativo de ruído')).toBeInTheDocument()
      expect(screen.getByText('Bateria de longa duração')).toBeInTheDocument()
      expect(screen.getByText('Design ergonômico')).toBeInTheDocument()
    })

    it('should not display features section when no features available', () => {
      const noFeaturesProduct = createMockProduct({
        features: []
      })

      render(<ProductDescription product={noFeaturesProduct} />)

      expect(screen.queryByText('Características principais:')).not.toBeInTheDocument()
    })

    it('should render features with bullet points', () => {
      render(<ProductDescription product={mockProduct} />)

      // Verificar se os bullet points estão presentes
      const bulletPoints = document.querySelectorAll('.w-2.h-2.bg-blue-500.rounded-full')
      expect(bulletPoints.length).toBe(mockProduct.features!.length)
    })
  })

  describe('Specifications Tab', () => {
    it('should display all specifications', () => {
      render(<ProductDescription product={mockProduct} />)

      const specsTab = screen.getByRole('tab', { name: 'Especificações' })
      fireEvent.click(specsTab)

      expect(screen.getByText('Especificações técnicas')).toBeInTheDocument()
      expect(screen.getByText('Marca:')).toBeInTheDocument()
      expect(screen.getByText('Test Brand')).toBeInTheDocument()
      expect(screen.getByText('Modelo:')).toBeInTheDocument()
      expect(screen.getByText('TB-001')).toBeInTheDocument()
      expect(screen.getByText('Cor:')).toBeInTheDocument()
      expect(screen.getByText('Azul')).toBeInTheDocument()
    })

    it('should show message when no specifications available', () => {
      const noSpecsProduct = createMockProduct({
        specifications: {}
      })

      render(<ProductDescription product={noSpecsProduct} />)

      const specsTab = screen.getByRole('tab', { name: 'Especificações' })
      fireEvent.click(specsTab)

      expect(screen.getByText('Especificações técnicas não disponíveis para este produto.')).toBeInTheDocument()
    })

    it('should format specifications in a grid layout', () => {
      render(<ProductDescription product={mockProduct} />)

      const specsTab = screen.getByRole('tab', { name: 'Especificações' })
      fireEvent.click(specsTab)

      const grid = document.querySelector('.grid.grid-cols-1.md\\:grid-cols-2')
      expect(grid).toBeInTheDocument()
    })
  })

  describe('Reviews Tab', () => {
    it('should display rating summary', () => {
      render(<ProductDescription product={mockProduct} />)

      const reviewsTab = screen.getByRole('tab', { name: 'Avaliações' })
      fireEvent.click(reviewsTab)

      expect(screen.getByText('Avaliações dos compradores')).toBeInTheDocument()
      expect(screen.getByText('4.5')).toBeInTheDocument()
      expect(screen.getByText('1.250 avaliações')).toBeInTheDocument()
    })

    it('should display rating distribution bars', () => {
      render(<ProductDescription product={mockProduct} />)

      const reviewsTab = screen.getByRole('tab', { name: 'Avaliações' })
      fireEvent.click(reviewsTab)

      // Verificar se as barras de distribuição estão presentes
      const distributionBars = document.querySelectorAll('.bg-yellow-400.h-2.rounded-full')
      expect(distributionBars.length).toBe(5) // Para cada estrela (5, 4, 3, 2, 1)

      // Verificar se os números de cada distribuição estão presentes
      expect(screen.getByText('800')).toBeInTheDocument() // 5 estrelas
      expect(screen.getByText('300')).toBeInTheDocument() // 4 estrelas
      expect(screen.getByText('100')).toBeInTheDocument() // 3 estrelas
    })

    it('should display individual reviews', () => {
      render(<ProductDescription product={mockProduct} />)

      const reviewsTab = screen.getByRole('tab', { name: 'Avaliações' })
      fireEvent.click(reviewsTab)

      expect(screen.getByText('João Silva')).toBeInTheDocument()
      expect(screen.getByText('Maria Santos')).toBeInTheDocument()
      expect(screen.getByText('Pedro Oliveira')).toBeInTheDocument()
      expect(screen.getByText('Excelente produto! Superou minhas expectativas.')).toBeInTheDocument()
    })

    it('should display star ratings for each review', () => {
      render(<ProductDescription product={mockProduct} />)

      const reviewsTab = screen.getByRole('tab', { name: 'Avaliações' })
      fireEvent.click(reviewsTab)

      // Verificar se as estrelas estão presentes (★)
      const content = document.body.textContent || ''
      expect(content).toContain('★')
    })

    it('should format review dates correctly', () => {
      render(<ProductDescription product={mockProduct} />)

      const reviewsTab = screen.getByRole('tab', { name: 'Avaliações' })
      fireEvent.click(reviewsTab)

      // Verificar se as datas estão formatadas em pt-BR (as datas reais que aparecem no componente)
      expect(screen.getByText('31/05/2024')).toBeInTheDocument()
      expect(screen.getByText('27/05/2024')).toBeInTheDocument()
      expect(screen.getByText('24/05/2024')).toBeInTheDocument()
    })

    it('should display "view all reviews" button', () => {
      render(<ProductDescription product={mockProduct} />)

      const reviewsTab = screen.getByRole('tab', { name: 'Avaliações' })
      fireEvent.click(reviewsTab)

      expect(screen.getByText('Ver todas as 1.250 avaliações')).toBeInTheDocument()
    })

    it('should display reviewer initials in avatar', () => {
      render(<ProductDescription product={mockProduct} />)

      const reviewsTab = screen.getByRole('tab', { name: 'Avaliações' })
      fireEvent.click(reviewsTab)

      expect(screen.getByText('JS')).toBeInTheDocument() // João Silva
      expect(screen.getByText('MS')).toBeInTheDocument() // Maria Santos
      expect(screen.getByText('PO')).toBeInTheDocument() // Pedro Oliveira
    })
  })

  describe('Questions Tab', () => {
    it('should display pre-defined questions and answers', () => {
      render(<ProductDescription product={mockProduct} />)

      const questionsTab = screen.getByRole('tab', { name: 'Perguntas' })
      fireEvent.click(questionsTab)

      expect(screen.getByText('Perguntas e respostas')).toBeInTheDocument()
      expect(screen.getByText('P: Este produto possui garantia?')).toBeInTheDocument()
      expect(screen.getByText('P: Qual a marca do produto?')).toBeInTheDocument()
      expect(screen.getByText('P: Qual o prazo de entrega?')).toBeInTheDocument()
    })

    it('should display answers with product information', () => {
      render(<ProductDescription product={mockProduct} />)

      const questionsTab = screen.getByRole('tab', { name: 'Perguntas' })
      fireEvent.click(questionsTab)

      expect(screen.getByText('R: Sim, 12 meses de garantia.')).toBeInTheDocument()
      expect(screen.getByText('R: A marca é Test Brand.')).toBeInTheDocument()
      expect(screen.getByText('R: Frete grátis para todo o Brasil.')).toBeInTheDocument()
    })

    it('should handle warranty fallback', () => {
      const noWarrantyProduct = createMockProduct({
        warranty: undefined
      })

      render(<ProductDescription product={noWarrantyProduct} />)

      const questionsTab = screen.getByRole('tab', { name: 'Perguntas' })
      fireEvent.click(questionsTab)

      expect(screen.getByText('R: Sim, este produto possui garantia conforme legislação.')).toBeInTheDocument()
    })

    it('should handle shipping description fallback', () => {
      const noShippingDescProduct = createMockProduct({
        shipping: {
          ...mockProduct.shipping,
          description: ""
        }
      })

      render(<ProductDescription product={noShippingDescProduct} />)

      const questionsTab = screen.getByRole('tab', { name: 'Perguntas' })
      fireEvent.click(questionsTab)

      expect(screen.getByText('R: O produto chegará em até 3 dias úteis.')).toBeInTheDocument()
    })

    it('should display question input form', () => {
      render(<ProductDescription product={mockProduct} />)

      const questionsTab = screen.getByRole('tab', { name: 'Perguntas' })
      fireEvent.click(questionsTab)

      expect(screen.getByText('Faça uma pergunta')).toBeInTheDocument()
      
      const input = screen.getByPlaceholderText('Digite sua pergunta sobre Test Product...')
      expect(input).toBeInTheDocument()
      
      const askButton = screen.getByText('Perguntar')
      expect(askButton).toBeInTheDocument()
    })

    it('should handle input typing', () => {
      render(<ProductDescription product={mockProduct} />)

      const questionsTab = screen.getByRole('tab', { name: 'Perguntas' })
      fireEvent.click(questionsTab)

      const input = screen.getByPlaceholderText('Digite sua pergunta sobre Test Product...')
      fireEvent.change(input, { target: { value: 'Esta é minha pergunta' } })
      
      expect(input).toHaveValue('Esta é minha pergunta')
    })

    it('should handle ask button click', () => {
      render(<ProductDescription product={mockProduct} />)

      const questionsTab = screen.getByRole('tab', { name: 'Perguntas' })
      fireEvent.click(questionsTab)

      const askButton = screen.getByText('Perguntar')
      
      expect(() => {
        fireEvent.click(askButton)
      }).not.toThrow()
    })
  })

  describe('Accessibility', () => {
    it('should have proper ARIA attributes for tabs', () => {
      render(<ProductDescription product={mockProduct} />)

      const tabs = screen.getAllByRole('tab')
      tabs.forEach((tab, index) => {
        expect(tab).toHaveAttribute('role', 'tab')
        expect(tab).toHaveAttribute('aria-selected')
        expect(tab).toHaveAttribute('aria-controls')
      })
    })

    it('should have proper tabpanel attributes', () => {
      render(<ProductDescription product={mockProduct} />)

      const tabpanel = screen.getByRole('tabpanel')
      expect(tabpanel).toHaveAttribute('role', 'tabpanel')
      expect(tabpanel).toHaveAttribute('id')
      expect(tabpanel).toHaveAttribute('aria-labelledby')
    })

    it('should update aria-selected when switching tabs', () => {
      render(<ProductDescription product={mockProduct} />)

      const descriptionTab = screen.getByRole('tab', { name: 'Descrição' })
      const specsTab = screen.getByRole('tab', { name: 'Especificações' })

      expect(descriptionTab).toHaveAttribute('aria-selected', 'true')
      expect(specsTab).toHaveAttribute('aria-selected', 'false')

      fireEvent.click(specsTab)

      expect(descriptionTab).toHaveAttribute('aria-selected', 'false')
      expect(specsTab).toHaveAttribute('aria-selected', 'true')
    })
  })

  describe('Edge Cases', () => {
    it('should handle product with minimal data', () => {
      const minimalProduct = createMockProduct({
        features: undefined,
        specifications: undefined,
        shortDescription: undefined,
        description: 'Descrição simples'
      })

      expect(() => {
        render(<ProductDescription product={minimalProduct} />)
      }).not.toThrow()

      expect(screen.getByText('Descrição simples')).toBeInTheDocument()
    })

    it('should handle empty arrays gracefully', () => {
      const emptyDataProduct = createMockProduct({
        features: [],
        specifications: {}
      })

      render(<ProductDescription product={emptyDataProduct} />)

      // Switch to specs tab
      const specsTab = screen.getByRole('tab', { name: 'Especificações' })
      fireEvent.click(specsTab)

      expect(screen.getByText('Especificações técnicas não disponíveis para este produto.')).toBeInTheDocument()
    })

    it('should handle very long product title in question placeholder', () => {
      const longTitleProduct = createMockProduct({
        title: 'Este é um título muito longo para o produto que pode causar problemas de layout'
      })

      render(<ProductDescription product={longTitleProduct} />)

      const questionsTab = screen.getByRole('tab', { name: 'Perguntas' })
      fireEvent.click(questionsTab)

      const input = screen.getByPlaceholderText(/Digite sua pergunta sobre Este é um título muito longo/)
      expect(input).toBeInTheDocument()
    })

    it('should handle zero rating count', () => {
      const noRatingsProduct = createMockProduct({
        rating: {
          average: 0,
          count: 0,
          distribution: { 5: 0, 4: 0, 3: 0, 2: 0, 1: 0 }
        }
      })

      render(<ProductDescription product={noRatingsProduct} />)

      const reviewsTab = screen.getByRole('tab', { name: 'Avaliações' })
      fireEvent.click(reviewsTab)

      expect(screen.getByText('0 avaliações')).toBeInTheDocument()
    })
  })

  describe('Content Formatting', () => {
    it('should format large numbers correctly', () => {
      const highRatingProduct = createMockProduct({
        rating: {
          average: 4.7,
          count: 12580,
          distribution: { 5: 8000, 4: 3000, 3: 1000, 2: 400, 1: 180 }
        }
      })

      render(<ProductDescription product={highRatingProduct} />)

      const reviewsTab = screen.getByRole('tab', { name: 'Avaliações' })
      fireEvent.click(reviewsTab)

      expect(screen.getByText('12.580 avaliações')).toBeInTheDocument()
    })

    it('should maintain proper text hierarchy', () => {
      render(<ProductDescription product={mockProduct} />)

      // Verificar se os títulos têm as classes corretas
      const mainTitle = screen.getByText('Descrição do produto')
      expect(mainTitle).toHaveClass('text-xl')
    })
  })

  describe('Interactive Elements', () => {
    it('should have hover effects on tabs', () => {
      render(<ProductDescription product={mockProduct} />)

      const specsTab = screen.getByRole('tab', { name: 'Especificações' })
      expect(specsTab).toHaveClass('hover:text-gray-700')
    })

    it('should have hover effects on buttons', () => {
      render(<ProductDescription product={mockProduct} />)

      const reviewsTab = screen.getByRole('tab', { name: 'Avaliações' })
      fireEvent.click(reviewsTab)

      const viewAllButton = screen.getByText(/Ver todas as/)
      expect(viewAllButton).toHaveClass('hover:bg-blue-200')
    })

    it('should handle rapid tab switching', () => {
      render(<ProductDescription product={mockProduct} />)

      const tabs = screen.getAllByRole('tab')
      
      // Rapidamente alternar entre todas as abas
      tabs.forEach(tab => {
        fireEvent.click(tab)
      })

      // Verificar se não houve crash e o último tab está ativo
      const questionsTab = screen.getByRole('tab', { name: 'Perguntas' })
      expect(questionsTab).toHaveAttribute('aria-selected', 'true')
    })
  })
})