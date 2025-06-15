import { renderHook, waitFor } from '@testing-library/react'
import { useProductApi } from '../useProductApi'

// Mock fetch globalmente
global.fetch = jest.fn()

// Mock timers for testing delays
jest.useFakeTimers()

// Criar testUtils se não existir
const testUtils = global.testUtils || {
  mockProduct: {
    id: 'test-product-1',
    title: 'Test Product',
    description: 'Test description',
    brand: 'Test Brand',
    price: {
      current: 299.99,
      original: 399.99,
      currency: 'BRL'
    },
    rating: {
      average: 4.5,
      count: 150,
      distribution: { 5: 80, 4: 45, 3: 15, 2: 7, 1: 3 }
    },
    stock: {
      available: 10,
      total: 50,
      isAvailable: true
    },
    images: [],
    shipping: {
      free: true,
      cost: 0,
      estimatedDays: 3
    },
    seller: {
      id: 'seller-1',
      name: 'Test Store',
      isOfficial: true,
      reputation: 4.8,
      positiveRating: 98,
      yearsOnPlatform: 5,
      location: 'Test Location'
    },
    category: {
      id: 'test',
      name: 'Test Category',
      path: []
    }
  },
  mockApiResponse: {
    success: true,
    data: {
      id: 'test-product-1',
      title: 'Test Product'
    }
  },
  createMockResponse: (data: any, ok = true, status = 200) => ({
    ok,
    status,
    statusText: ok ? 'OK' : 'Error',
    json: jest.fn().mockResolvedValue(data),
    text: jest.fn().mockResolvedValue(JSON.stringify(data)),
    headers: new Headers({
      'content-type': 'application/json',
    }),
  } as Response)
}

describe('useProductApi', () => {
  const mockProductId = 'test-product-1'
  const mockFetch = fetch as jest.MockedFunction<typeof fetch>

  beforeEach(() => {
    mockFetch.mockClear()
    jest.clearAllTimers()
  })

  afterEach(() => {
    jest.runOnlyPendingTimers()
    jest.useRealTimers()
    jest.useFakeTimers()
  })

  it('should fetch product successfully', async () => {
    mockFetch.mockResolvedValueOnce(
      testUtils.createMockResponse(testUtils.mockApiResponse)
    )

    const { result } = renderHook(() => useProductApi(mockProductId))

    expect(result.current.loading).toBe(true)
    expect(result.current.product).toBe(null)
    expect(result.current.error).toBe(null)

    await waitFor(() => {
      expect(result.current.loading).toBe(false)
    })

    expect(result.current.product).toBeTruthy()
    expect(result.current.error).toBe(null)
    expect(result.current.retryCount).toBe(0)
  })

  it('should handle fetch error with retry', async () => {
    mockFetch.mockRejectedValue(new Error('Network error'))

    const { result } = renderHook(() => useProductApi(mockProductId))

    expect(result.current.loading).toBe(true)

    // Wait for first attempt to fail
    await waitFor(() => {
      expect(result.current.error).toBeTruthy()
    })

    expect(result.current.retryCount).toBe(1)
    expect(result.current.error?.message).toContain('Tentando novamente')

    // Fast forward time for retry delay
    jest.advanceTimersByTime(2000)

    await waitFor(() => {
      expect(result.current.retryCount).toBe(2)
    })
  })

  it('should stop retrying after max attempts', async () => {
    mockFetch.mockRejectedValue(new Error('Persistent error'))

    const { result } = renderHook(() => useProductApi(mockProductId))

    // Wait for all retries to complete
    for (let i = 0; i < 3; i++) {
      jest.advanceTimersByTime(10000) // Advance past all possible delays
      await waitFor(() => {}, { timeout: 100 })
    }

    await waitFor(() => {
      expect(result.current.loading).toBe(false)
    })

    expect(result.current.error?.message).toContain('Não foi possível carregar')
    expect(result.current.retryCount).toBe(3)
    expect(result.current.product).toBe(null)
  })

  it('should handle HTTP error responses', async () => {
    mockFetch.mockResolvedValueOnce(
      testUtils.createMockResponse(
        { error: 'Not found' },
        false,
        404
      )
    )

    const { result } = renderHook(() => useProductApi(mockProductId))

    await waitFor(() => {
      expect(result.current.loading).toBe(false)
    })

    expect(result.current.error?.message).toContain('HTTP error! status: 404')
    expect(result.current.product).toBe(null)
  })

  it('should handle API response with success: false', async () => {
    const errorResponse = {
      ...testUtils.mockApiResponse,
      success: false,
      message: 'Product not found',
    }

    mockFetch.mockResolvedValueOnce(
      testUtils.createMockResponse(errorResponse)
    )

    const { result } = renderHook(() => useProductApi(mockProductId))

    await waitFor(() => {
      expect(result.current.loading).toBe(false)
    })

    expect(result.current.error?.message).toBe('Product not found')
    expect(result.current.product).toBe(null)
  })

  it('should handle timeout', async () => {
    mockFetch.mockImplementation(() => 
      new Promise((_, reject) => {
        setTimeout(() => reject(new Error('AbortError')), 15000)
      })
    )

    const { result } = renderHook(() => useProductApi(mockProductId))

    // Fast forward past timeout
    jest.advanceTimersByTime(15000)

    await waitFor(() => {
      expect(result.current.error).toBeTruthy()
    })

    expect(result.current.error?.message).toContain('AbortError')
  })

  it('should retry manually when retry function is called', async () => {
    mockFetch
      .mockRejectedValueOnce(new Error('First error'))
      .mockResolvedValueOnce(testUtils.createMockResponse(testUtils.mockApiResponse))

    const { result } = renderHook(() => useProductApi(mockProductId))

    // Wait for initial error
    await waitFor(() => {
      expect(result.current.error).toBeTruthy()
    })

    expect(result.current.retryCount).toBe(1)

    // Call manual retry
    result.current.retry()

    await waitFor(() => {
      expect(result.current.loading).toBe(false)
    })

    expect(result.current.product).toBeTruthy()
    expect(result.current.error).toBe(null)
    expect(result.current.retryCount).toBe(0)
  })

  it('should refetch when productId changes', async () => {
    mockFetch.mockResolvedValue(
      testUtils.createMockResponse(testUtils.mockApiResponse)
    )

    const { result, rerender } = renderHook(
      ({ id }) => useProductApi(id),
      { initialProps: { id: 'product-1' } }
    )

    await waitFor(() => {
      expect(result.current.loading).toBe(false)
    })

    expect(mockFetch).toHaveBeenCalledWith(
      'http://localhost:3001/api/products/product-1',
      expect.any(Object)
    )

    // Change product ID
    rerender({ id: 'product-2' })

    await waitFor(() => {
      expect(mockFetch).toHaveBeenCalledWith(
        'http://localhost:3001/api/products/product-2',
        expect.any(Object)
      )
    })
  })

  it('should not fetch if productId is empty', () => {
    const { result } = renderHook(() => useProductApi(''))

    expect(result.current.loading).toBe(true)
    expect(mockFetch).not.toHaveBeenCalled()
  })

  it('should calculate retry delay with exponential backoff', async () => {
    mockFetch.mockRejectedValue(new Error('Network error'))

    const { result } = renderHook(() => useProductApi(mockProductId))

    // First retry after 2 seconds
    jest.advanceTimersByTime(2000)
    await waitFor(() => {
      expect(result.current.retryCount).toBe(2)
    })

    // Second retry after 4 seconds  
    jest.advanceTimersByTime(4000)
    await waitFor(() => {
      expect(result.current.retryCount).toBe(3)
    })
  })

  it('should set correct request headers', async () => {
    mockFetch.mockResolvedValueOnce(
      testUtils.createMockResponse(testUtils.mockApiResponse)
    )

    renderHook(() => useProductApi(mockProductId))

    await waitFor(() => {
      expect(mockFetch).toHaveBeenCalledWith(
        expect.any(String),
        expect.objectContaining({
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
          },
        })
      )
    })
  })

  it('should handle network connectivity issues', async () => {
    mockFetch.mockRejectedValue(new Error('fetch failed'))

    const { result } = renderHook(() => useProductApi(mockProductId))

    await waitFor(() => {
      expect(result.current.error).toBeTruthy()
    })

    expect(result.current.error?.code).toBe('FETCH_ERROR')
  })

  it('should reset error state on successful retry', async () => {
    mockFetch
      .mockRejectedValueOnce(new Error('Network error'))
      .mockResolvedValueOnce(testUtils.createMockResponse(testUtils.mockApiResponse))

    const { result } = renderHook(() => useProductApi(mockProductId))

    // Wait for error
    await waitFor(() => {
      expect(result.current.error).toBeTruthy()
    })

    // Manual retry
    result.current.retry()

    await waitFor(() => {
      expect(result.current.error).toBe(null)
      expect(result.current.product).toBeTruthy()
    })
  })

  it('should handle malformed JSON response', async () => {
    mockFetch.mockResolvedValueOnce({
      ok: true,
      status: 200,
      json: jest.fn().mockRejectedValue(new Error('Invalid JSON')),
      text: jest.fn().mockResolvedValue('Invalid response'),
    } as any)

    const { result } = renderHook(() => useProductApi(mockProductId))

    await waitFor(() => {
      expect(result.current.error).toBeTruthy()
    })

    // O hook trata erros de JSON como erros de conexão e faz retry
    expect(result.current.error?.message).toContain('Tentando novamente')
    expect(result.current.retryCount).toBeGreaterThan(0)
  })
})