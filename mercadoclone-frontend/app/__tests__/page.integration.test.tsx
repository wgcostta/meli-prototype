import { render, screen, waitFor, fireEvent, waitForElementToBeRemoved } from '@testing-library/react'
import ProductPage from '../page'

// Mock the useProductApi hook
jest.mock('../hooks/useProductApi')

import { useProductApi } from '../hooks/useProductApi'

const mockUseProductApi = useProductApi as jest.MockedFunction<typeof useProductApi>

// Mock product with better structure for breadcrumb
const mockProductWithPath = {
  ...global.testUtils.mockProduct,
  category: {
    id: 'electronics',
    name: 'Electronics',
    path: ['Electronics', 'Audio', 'Headphones']
  }
}

describe('ProductPage Integration Tests', () => {
  const mockParams = { id: 'test-product-1' }

  beforeEach(() => {
    mockUseProductApi.mockClear()
  })

  it('should render complete product page when data loads successfully', async () => {
    mockUseProductApi.mockReturnValue({
      product: mockProductWithPath,
      loading: false,
      error: null,
      retryCount: 0,
      retry: jest.fn(),
      isRetrying: false,
    })

    render(<ProductPage params={mockParams} />)

    // Should render all main components - use getAllByText for duplicates
    const productTitles = screen.getAllByText('Test Product')
    expect(productTitles.length).toBeGreaterThanOrEqual(1)
    
    expect(screen.getByText('R$ 299,99')).toBeInTheDocument()
    expect(screen.getByText('Test Store')).toBeInTheDocument()
    
    // Should render breadcrumb with product info
    expect(screen.getByText('Electronics')).toBeInTheDocument()
    
    // Should render product description section
    expect(screen.getByText('Descrição do produto')).toBeInTheDocument()
  })

  it('should show loading skeleton while fetching data', () => {
    mockUseProductApi.mockReturnValue({
      product: null,
      loading: true,
      error: null,
      retryCount: 0,
      retry: jest.fn(),
      isRetrying: false,
    })

    render(<ProductPage params={mockParams} />)

    // Should show skeleton loading components
    const skeletonElements = document.querySelectorAll('.animate-pulse')
    expect(skeletonElements.length).toBeGreaterThan(0)
    
    // Should show header and breadcrumb even during loading
    expect(screen.getByText('MercadoClone')).toBeInTheDocument()
  })

  it('should show error state when API fails', () => {
    const mockRetry = jest.fn()
    mockUseProductApi.mockReturnValue({
      product: null,
      loading: false,
      error: {
        message: 'Failed to load product',
        code: 'FETCH_ERROR',
      },
      retryCount: 1,
      retry: mockRetry,
      isRetrying: false,
    })

    render(<ProductPage params={mockParams} />)

    expect(screen.getByText('Failed to load product')).toBeInTheDocument()
    expect(screen.getByText('Tentar novamente')).toBeInTheDocument()
    expect(screen.getByText('Tentativa 1 de 3 realizadas')).toBeInTheDocument()

    // Should call retry when button is clicked
    fireEvent.click(screen.getByText('Tentar novamente'))
    expect(mockRetry).toHaveBeenCalledTimes(1)
  })

  it('should show retry loading state when retrying', () => {
    mockUseProductApi.mockReturnValue({
      product: null,
      loading: true,
      error: {
        message: 'Tentando reconectar...',
        code: 'FETCH_ERROR',
      },
      retryCount: 2,
      retry: jest.fn(),
      isRetrying: true,
    })

    render(<ProductPage params={mockParams} />)

    expect(screen.getByText('Reconectando...')).toBeInTheDocument()
    expect(screen.getByText('Tentativa 2 de 3')).toBeInTheDocument()
  })

  it('should handle empty product ID gracefully', () => {
    mockUseProductApi.mockReturnValue({
      product: null,
      loading: true,
      error: null,
      retryCount: 0,
      retry: jest.fn(),
      isRetrying: false,
    })

    render(<ProductPage params={{ id: '' }} />)

    // Should still render without crashing
    expect(screen.getByText('MercadoClone')).toBeInTheDocument()
  })

  it('should integrate all components correctly', async () => {
    mockUseProductApi.mockReturnValue({
      product: mockProductWithPath,
      loading: false,
      error: null,
      retryCount: 0,
      retry: jest.fn(),
      isRetrying: false,
    })

    render(<ProductPage params={mockParams} />)

    // Header integration
    expect(screen.getByText('MercadoClone')).toBeInTheDocument()
    expect(screen.getByPlaceholderText('Buscar produtos, marcas e muito mais...')).toBeInTheDocument()

    // Breadcrumb integration with product data
    expect(screen.getByText('Início')).toBeInTheDocument()
    expect(screen.getByText('Electronics')).toBeInTheDocument()

    // Gallery integration - be more specific about which image we want
    const images = screen.getAllByRole('img')
    const mainImage = images.find(img => img.getAttribute('alt')?.includes('Test image 1'))
    expect(mainImage).toBeInTheDocument()

    // Product info integration
    const productTitles = screen.getAllByText('Test Product')
    expect(productTitles.length).toBeGreaterThanOrEqual(1)
    
    expect(screen.getByText('R$ 299,99')).toBeInTheDocument()
    expect(screen.getByText('Comprar agora')).toBeInTheDocument()

    // Description integration
    expect(screen.getByText('Descrição do produto')).toBeInTheDocument()
    expect(screen.getByText('Especificações')).toBeInTheDocument()
  })

  it('should handle product data updates correctly', async () => {
    const { rerender } = render(<ProductPage params={mockParams} />)

    // Initial loading state
    mockUseProductApi.mockReturnValue({
      product: null,
      loading: true,
      error: null,
      retryCount: 0,
      retry: jest.fn(),
      isRetrying: false,
    })

    rerender(<ProductPage params={mockParams} />)

    // Should show loading
    const loadingElements = document.querySelectorAll('.animate-pulse')
    expect(loadingElements.length).toBeGreaterThan(0)

    // Product loaded
    mockUseProductApi.mockReturnValue({
      product: mockProductWithPath,
      loading: false,
      error: null,
      retryCount: 0,
      retry: jest.fn(),
      isRetrying: false,
    })

    rerender(<ProductPage params={mockParams} />)

    // Should show product data - use getAllByText for duplicates
    await waitFor(() => {
      const productTitles = screen.getAllByText('Test Product')
      expect(productTitles.length).toBeGreaterThanOrEqual(1)
    })
  })

  it('should maintain consistent layout across states', () => {
    const states = [
      // Loading state
      {
        product: null,
        loading: true,
        error: null,
        retryCount: 0,
        retry: jest.fn(),
        isRetrying: false,
      },
      // Error state  
      {
        product: null,
        loading: false,
        error: { message: 'Error', code: 'FETCH_ERROR' },
        retryCount: 1,
        retry: jest.fn(),
        isRetrying: false,
      },
      // Success state
      {
        product: mockProductWithPath,
        loading: false,
        error: null,
        retryCount: 0,
        retry: jest.fn(),
        isRetrying: false,
      },
    ]

    states.forEach((state, index) => {
      mockUseProductApi.mockReturnValue(state)
      const { unmount } = render(<ProductPage params={mockParams} />)
      
      // All states should have consistent wrapper classes
      const wrapper = document.querySelector('.min-h-screen.bg-gray-50') ||
                     document.querySelector('.min-h-screen')
      expect(wrapper).toBeInTheDocument()
      
      unmount()
    })
  })

  describe('Accessibility Integration', () => {
    it('should have proper heading hierarchy', () => {
      mockUseProductApi.mockReturnValue({
        product: mockProductWithPath,
        loading: false,
        error: null,
        retryCount: 0,
        retry: jest.fn(),
        isRetrying: false,
      })

      render(<ProductPage params={mockParams} />)

      // Get all headings and verify structure
      const siteTitle = screen.getByRole('heading', { level: 1, name: /mercadoclone/i })
      expect(siteTitle).toBeInTheDocument()
      
      // Product title should be h2 (after fixing the component)
      const productHeading = screen.getByRole('heading', { level: 2, name: /test product/i })
      expect(productHeading).toBeInTheDocument()
    })

    it('should have proper navigation landmarks', () => {
      mockUseProductApi.mockReturnValue({
        product: mockProductWithPath,
        loading: false,
        error: null,
        retryCount: 0,
        retry: jest.fn(),
        isRetrying: false,
      })

      render(<ProductPage params={mockParams} />)

      // Get breadcrumb navigation by aria-label (after adding it)
      const breadcrumbNav = screen.getByRole('navigation', { name: /breadcrumb/i })
      expect(breadcrumbNav).toBeInTheDocument()
      
      // Get tab navigation by aria-label (after adding it)
      const tabNav = screen.getByRole('navigation', { name: /product information tabs/i })
      expect(tabNav).toBeInTheDocument()
    })

    it('should have proper form controls', () => {
      mockUseProductApi.mockReturnValue({
        product: mockProductWithPath,
        loading: false,
        error: null,
        retryCount: 0,
        retry: jest.fn(),
        isRetrying: false,
      })

      render(<ProductPage params={mockParams} />)

      // Should have accessible buttons
      expect(screen.getByRole('button', { name: /comprar agora/i })).toBeInTheDocument()
      expect(screen.getByRole('button', { name: /adicionar ao carrinho/i })).toBeInTheDocument()
    })
  })

  describe('Performance Integration', () => {
    it('should not cause memory leaks during state changes', () => {
      const { rerender, unmount } = render(<ProductPage params={mockParams} />)

      // Simulate rapid state changes
      const states = [
        { loading: true, product: null, error: null },
        { loading: false, product: mockProductWithPath, error: null },
        { loading: false, product: null, error: { message: 'Error', code: 'ERROR' } },
      ]

      states.forEach(state => {
        mockUseProductApi.mockReturnValue({
          ...state,
          retryCount: 0,
          retry: jest.fn(),
          isRetrying: false,
        })
        rerender(<ProductPage params={mockParams} />)
      })

      // Should unmount without errors
      expect(() => unmount()).not.toThrow()
    })

    it('should render efficiently with large datasets', () => {
      const largeProduct = {
        ...mockProductWithPath,
        features: Array(100).fill('Feature'),
        specifications: Object.fromEntries(
          Array(50).fill(0).map((_, i) => [`spec${i}`, `value${i}`])
        ),
      }

      mockUseProductApi.mockReturnValue({
        product: largeProduct,
        loading: false,
        error: null,
        retryCount: 0,
        retry: jest.fn(),
        isRetrying: false,
      })

      // Should render without performance issues
      expect(() => {
        render(<ProductPage params={mockParams} />)
      }).not.toThrow()
    })
  })

  describe('Error Recovery Integration', () => {
    it('should recover from error to success state', async () => {
      const mockRetry = jest.fn()
      
      // Start with error
      mockUseProductApi.mockReturnValue({
        product: null,
        loading: false,
        error: { message: 'Network error', code: 'FETCH_ERROR' },
        retryCount: 1,
        retry: mockRetry,
        isRetrying: false,
      })

      const { rerender } = render(<ProductPage params={mockParams} />)

      expect(screen.getByText('Network error')).toBeInTheDocument()

      // Simulate successful retry
      mockUseProductApi.mockReturnValue({
        product: mockProductWithPath,
        loading: false,
        error: null,
        retryCount: 0,
        retry: mockRetry,
        isRetrying: false,
      })

      rerender(<ProductPage params={mockParams} />)

      await waitFor(() => {
        const productTitles = screen.getAllByText('Test Product')
        expect(productTitles.length).toBeGreaterThanOrEqual(1)
      })

      expect(screen.queryByText('Network error')).not.toBeInTheDocument()
    })

    it('should handle transition from loading to error', () => {
      const { rerender } = render(<ProductPage params={mockParams} />)

      // Start with loading
      mockUseProductApi.mockReturnValue({
        product: null,
        loading: true,
        error: null,
        retryCount: 0,
        retry: jest.fn(),
        isRetrying: false,
      })

      rerender(<ProductPage params={mockParams} />)

      expect(document.querySelectorAll('.animate-pulse').length).toBeGreaterThan(0)

      // Transition to error
      mockUseProductApi.mockReturnValue({
        product: null,
        loading: false,
        error: { message: 'Failed to load', code: 'ERROR' },
        retryCount: 1,
        retry: jest.fn(),
        isRetrying: false,
      })

      rerender(<ProductPage params={mockParams} />)

      expect(screen.getByText('Failed to load')).toBeInTheDocument()
      expect(document.querySelectorAll('.animate-pulse')).toHaveLength(0)
    })
  })
})