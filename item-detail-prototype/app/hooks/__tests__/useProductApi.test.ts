import { renderHook, waitFor } from '@testing-library/react'
import { useProductApi } from '../useProductApi'

// Mock timers for testing delays
jest.useFakeTimers()

describe('useProductApi', () => {
  const mockProductId = 'test-product-1'

  beforeEach(() => {
    fetch.mockClear()
    jest.clearAllTimers()
  })

  afterEach(() => {
    jest.runOnlyPendingTimers()
    jest.useRealTimers()
    jest.useFakeTimers()
  })

  it('should fetch product successfully', async () => {
    fetch.mockResolvedValueOnce(
      testUtils.createMockResponse(testUtils.mockApiResponse)
    )

    const { result } = renderHook(() => useProductApi(mockProductId))

    expect(result.current.loading).toBe(true)
    expect(result.current.product).toBe(null)
    expect(result.current.error).toBe(null)

    await waitFor(() => {
      expect(result.current.loading).toBe(false)
    })

    expect(result.current.product).toEqual(testUtils.mockProduct)
    expect(result.current.error).toBe(null)
    expect(result.current.retryCount).toBe(0)
  })

  it('should handle fetch error with retry', async () => {
    fetch.mockRejectedValue(new Error('Network error'))

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
    fetch.mockRejectedValue(new Error('Persistent error'))

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
    fetch.mockResolvedValueOnce(
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

    fetch.mockResolvedValueOnce(
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
    fetch.mockImplementation(() => 
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
    fetch
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

    expect(result.current.product).toEqual(testUtils.mockProduct)
    expect(result.current.error).toBe(null)
    expect(result.current.retryCount).toBe(0)
  })

  it('should refetch when productId changes', async () => {
    fetch.mockResolvedValue(
      testUtils.createMockResponse(testUtils.mockApiResponse)
    )

    const { result, rerender } = renderHook(
      ({ id }) => useProductApi(id),
      { initialProps: { id: 'product-1' } }
    )

    await waitFor(() => {
      expect(result.current.loading).toBe(false)
    })

    expect(fetch).toHaveBeenCalledWith(
      'http://localhost:3001/api/products/product-1',
      expect.any(Object)
    )

    // Change product ID
    rerender({ id: 'product-2' })

    await waitFor(() => {
      expect(fetch).toHaveBeenCalledWith(
        'http://localhost:3001/api/products/product-2',
        expect.any(Object)
      )
    })
  })

  it('should not fetch if productId is empty', () => {
    const { result } = renderHook(() => useProductApi(''))

    expect(result.current.loading).toBe(true)
    expect(fetch).not.toHaveBeenCalled()
  })

  it('should calculate retry delay with exponential backoff', async () => {
    fetch.mockRejectedValue(new Error('Network error'))

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
    fetch.mockResolvedValueOnce(
      testUtils.createMockResponse(testUtils.mockApiResponse)
    )

    renderHook(() => useProductApi(mockProductId))

    await waitFor(() => {
      expect(fetch).toHaveBeenCalledWith(
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
})