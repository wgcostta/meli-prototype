import { renderHook, waitFor, act } from '@testing-library/react'
import { useProductApi } from '../useProductApi'

// Mock global fetch
const mockFetch = jest.fn()
global.fetch = mockFetch

// Mock console.error to reduce noise
const consoleSpy = jest.spyOn(console, 'error').mockImplementation(() => {})

const mockProduct = {
  id: 'test-product-1',
  title: 'Test Product',
  description: 'Test description',
  shortDescription: 'Short description',
  brand: 'Test Brand',
  sku: 'TEST-001',
  images: [],
  price: {
    current: 100,
    original: 120,
    currency: 'BRL',
    discount: 17
  },
  rating: {
    average: 4.5,
    count: 100,
    distribution: { 5: 50, 4: 30, 3: 15, 2: 3, 1: 2 }
  },
  stock: {
    available: 10,
    total: 100,
    isAvailable: true
  },
  shipping: {
    free: true,
    cost: 0,
    estimatedDays: 3,
    description: 'Free shipping'
  },
  seller: {
    id: 'seller-1',
    name: 'Test Seller',
    reputation: 4.8,
    location: 'Test City',
    isOfficial: true,
    positiveRating: 98,
    yearsOnPlatform: 5
  },
  paymentMethods: [],
  features: [],
  specifications: {},
  warranty: '1 year',
  category: {
    id: 'test-category',
    name: 'Test Category',
    path: ['Test', 'Category']
  },
  createdAt: '2024-01-01T00:00:00Z',
  updatedAt: '2024-01-01T00:00:00Z'
}

describe('useProductApi', () => {
  beforeEach(() => {
    mockFetch.mockClear()
    consoleSpy.mockClear()
  })

  afterAll(() => {
    consoleSpy.mockRestore()
  })

  it('should fetch product successfully', async () => {
    mockFetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({
        success: true,
        data: mockProduct,
        timestamp: new Date().toISOString()
      })
    })

    const { result } = renderHook(() => useProductApi('test-product-1'))

    // Initial state
    expect(result.current.loading).toBe(true)
    expect(result.current.product).toBe(null)

    await waitFor(() => {
      expect(result.current.loading).toBe(false)
    })

    expect(result.current.product).toEqual(mockProduct)
    expect(result.current.error).toBe(null)
    expect(result.current.retryCount).toBe(0)
  })

  it('should handle null productId', () => {
    const { result } = renderHook(() => useProductApi(null))

    expect(result.current.loading).toBe(false)
    expect(result.current.product).toBe(null)
    expect(result.current.error).toBe(null)
    expect(result.current.retryCount).toBe(0)
    expect(mockFetch).not.toHaveBeenCalled()
  })

  it('should stop retrying after max attempts', async () => {
    mockFetch.mockRejectedValue(new Error('Network error'))

    const { result } = renderHook(() => 
      useProductApi('test-product-1', { retryAttempts: 3, retryDelay: 100 })
    )

    // Wait for all retries to complete and loading to be false
    await waitFor(() => {
      expect(result.current.loading).toBe(false)
      expect(result.current.retryCount).toBe(3)
    }, { timeout: 5000 })

    // After all retries exhausted, should show final error message
    expect(result.current.error?.message).toBe('Network error')
    expect(result.current.product).toBe(null)
  })

  it('should handle HTTP error responses', async () => {
    mockFetch.mockResolvedValueOnce({
      ok: false,
      status: 404,
      json: async () => ({})
    })

    const { result } = renderHook(() => useProductApi('test-product-1'))

    await waitFor(() => {
      expect(result.current.loading).toBe(false)
    })

    expect(result.current.error?.message).toContain('HTTP error! status: 404')
    expect(result.current.product).toBe(null)
  })

  it('should handle API response with success: false', async () => {
    mockFetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({
        success: false,
        message: 'Product not found'
      })
    })

    const { result } = renderHook(() => useProductApi('test-product-1'))

    await waitFor(() => {
      expect(result.current.loading).toBe(false)
    })

    expect(result.current.error?.message).toBe('Product not found')
    expect(result.current.product).toBe(null)
  })

  it('should handle timeout', async () => {
    mockFetch.mockImplementation(() => 
      new Promise((_, reject) => {
        setTimeout(() => reject(new Error('AbortError: The operation was aborted')), 50)
      })
    )

    const { result } = renderHook(() => 
      useProductApi('test-product-1', { timeout: 100, retryDelay: 50, retryAttempts: 1 })
    )

    await waitFor(() => {
      expect(result.current.error?.code).toBe('TIMEOUT_ERROR')
    }, { timeout: 3000 })
  })

  it('should retry manually when retry function is called', async () => {
    // First call fails
    mockFetch.mockRejectedValueOnce(new Error('Network error'))
    
    const { result } = renderHook(() => useProductApi('test-product-1'))

    await waitFor(() => {
      expect(result.current.error).toBeTruthy()
    })

    // Second call succeeds
    mockFetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({
        success: true,
        data: mockProduct,
        timestamp: new Date().toISOString()
      })
    })

    await act(async () => {
      result.current.retry()
    })

    await waitFor(() => {
      expect(result.current.loading).toBe(false)
    })

    expect(result.current.product).toBeTruthy()
    expect(result.current.error).toBe(null)
  })

  it('should refetch when productId changes', async () => {
    mockFetch.mockResolvedValue({
      ok: true,
      json: async () => ({
        success: true,
        data: mockProduct,
        timestamp: new Date().toISOString()
      })
    })

    const { result, rerender } = renderHook(
      ({ productId }) => useProductApi(productId),
      { initialProps: { productId: 'product-1' } }
    )

    await waitFor(() => {
      expect(result.current.loading).toBe(false)
    })

    expect(mockFetch).toHaveBeenCalledWith(
      '/api/v1/products/product-1',
      expect.any(Object)
    )

    // Change productId
    rerender({ productId: 'product-2' })

    await waitFor(() => {
      expect(mockFetch).toHaveBeenCalledWith(
        '/api/v1/products/product-2',
        expect.any(Object)
      )
    })
  })

  it('should set correct request headers', async () => {
    mockFetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({
        success: true,
        data: mockProduct,
        timestamp: new Date().toISOString()
      })
    })

    renderHook(() => useProductApi('test-product-1'))

    await waitFor(() => {
      expect(mockFetch).toHaveBeenCalledWith(
        expect.any(String),
        expect.objectContaining({
          method: 'GET',
          headers: expect.objectContaining({
            'Content-Type': 'application/json',
            'Accept': 'application/json'
          })
        })
      )
    })
  })

  it('should reset error state on successful retry', async () => {
    // First call fails
    mockFetch.mockRejectedValueOnce(new Error('Network error'))
    
    const { result } = renderHook(() => useProductApi('test-product-1'))

    await waitFor(() => {
      expect(result.current.error).toBeTruthy()
    })

    // Second call succeeds
    mockFetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({
        success: true,
        data: mockProduct,
        timestamp: new Date().toISOString()
      })
    })

    await act(async () => {
      result.current.retry()
    })

    await waitFor(() => {
      expect(result.current.error).toBe(null)
      expect(result.current.product).toBeTruthy()
    })
  })

  it('should handle custom retry options', async () => {
    mockFetch.mockRejectedValue(new Error('Network error'))

    const { result } = renderHook(() => 
      useProductApi('test-product-1', { 
        retryAttempts: 2, 
        retryDelay: 50,
        timeout: 5000 
      })
    )

    // Wait for all retries to complete
    await waitFor(() => {
      expect(result.current.loading).toBe(false)
      expect(result.current.retryCount).toBe(2)
    }, { timeout: 3000 })

    // After 2 attempts, should be finished with final error
    expect(result.current.error?.message).toBe('Network error')
  })

  it('should handle network errors with retry', async () => {
    // Mock network error followed by success
    mockFetch
      .mockRejectedValueOnce(new Error('Failed to fetch'))
      .mockResolvedValueOnce({
        ok: true,
        json: async () => ({
          success: true,
          data: mockProduct,
          timestamp: new Date().toISOString()
        })
      })

    const { result } = renderHook(() => 
      useProductApi('test-product-1', { retryDelay: 100 })
    )

    // Should show retry message initially
    await waitFor(() => {
      expect(result.current.error?.message).toContain('Tentando novamente')
    })

    // Should eventually succeed
    await waitFor(() => {
      expect(result.current.product).toBeTruthy()
      expect(result.current.error).toBe(null)
    }, { timeout: 3000 })
  })

  it('should handle empty product ID', () => {
    const { result } = renderHook(() => useProductApi(''))

    expect(result.current.loading).toBe(false)
    expect(result.current.product).toBe(null)
    expect(result.current.error).toBe(null)
    expect(mockFetch).not.toHaveBeenCalled()
  })

  it('should show isRetrying state correctly', async () => {
    mockFetch.mockRejectedValue(new Error('Network error'))

    const { result } = renderHook(() => 
      useProductApi('test-product-1', { retryDelay: 100 })
    )

    await waitFor(() => {
      expect(result.current.error).toBeTruthy()
    })

    expect(result.current.isRetrying).toBe(false)
    expect(result.current.retryCount).toBeGreaterThan(0)
  })

  it('should handle malformed JSON response', async () => {
    mockFetch.mockResolvedValueOnce({
      ok: true,
      json: async () => {
        throw new Error('Invalid JSON')
      }
    })

    const { result } = renderHook(() => useProductApi('test-product-1'))

    await waitFor(() => {
      expect(result.current.error).toBeTruthy()
    })

    expect(result.current.error?.message).toContain('Invalid JSON')
    expect(result.current.product).toBe(null)
  })
})