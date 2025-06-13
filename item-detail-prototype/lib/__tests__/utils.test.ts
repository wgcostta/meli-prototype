import {
  formatPrice,
  formatDate,
  calculateDiscount,
  truncateText,
  generateSEOTitle,
  generateBreadcrumbPath,
  isOnline,
  delay,
  getRetryDelay,
  isValidImageUrl,
  generateImagePlaceholder,
  debounce,
  throttle,
  cn,
} from '../utils'

describe('Utils', () => {
  describe('formatPrice', () => {
    it('should format BRL currency correctly', () => {
      expect(formatPrice(299.99, 'BRL')).toBe('R$ 299,99')
      expect(formatPrice(1000, 'BRL')).toBe('R$ 1.000,00')
      expect(formatPrice(0, 'BRL')).toBe('R$ 0,00')
    })

    it('should default to BRL when no currency provided', () => {
      expect(formatPrice(100)).toBe('R$ 100,00')
    })

    it('should handle different currencies', () => {
      expect(formatPrice(100, 'USD')).toBe('R$ 100,00') // Converts to BRL for display
    })

    it('should handle decimal values', () => {
      expect(formatPrice(99.5, 'BRL')).toBe('R$ 99,50')
      expect(formatPrice(99.99, 'BRL')).toBe('R$ 99,99')
    })
  })

  describe('formatDate', () => {
    it('should format date correctly', () => {
      const date = '2024-01-15T10:30:00Z'
      const formatted = formatDate(date)
      expect(formatted).toMatch(/15 de janeiro de 2024/)
    })

    it('should handle different date formats', () => {
      expect(() => formatDate('2024-01-01')).not.toThrow()
      expect(() => formatDate('2024-01-01T00:00:00.000Z')).not.toThrow()
    })
  })

  describe('calculateDiscount', () => {
    it('should calculate discount percentage correctly', () => {
      expect(calculateDiscount(100, 80)).toBe(20)
      expect(calculateDiscount(399.99, 299.99)).toBe(25)
      expect(calculateDiscount(50, 25)).toBe(50)
    })

    it('should handle zero discount', () => {
      expect(calculateDiscount(100, 100)).toBe(0)
    })

    it('should handle edge cases', () => {
      expect(calculateDiscount(0, 0)).toBe(NaN)
      expect(calculateDiscount(100, 0)).toBe(100)
    })
  })

  describe('truncateText', () => {
    it('should truncate text when longer than maxLength', () => {
      const longText = 'This is a very long text that should be truncated'
      expect(truncateText(longText, 10)).toBe('This is a ...')
    })

    it('should not truncate text when shorter than maxLength', () => {
      const shortText = 'Short'
      expect(truncateText(shortText, 10)).toBe('Short')
    })

    it('should handle exact length', () => {
      const text = '1234567890'
      expect(truncateText(text, 10)).toBe('1234567890')
    })

    it('should handle empty text', () => {
      expect(truncateText('', 10)).toBe('')
    })
  })

  describe('generateSEOTitle', () => {
    it('should generate SEO title with brand', () => {
      const title = generateSEOTitle('Product Name', 'Brand Name')
      expect(title).toBe('Product Name | Brand Name | MercadoClone')
    })

    it('should generate SEO title without brand', () => {
      const title = generateSEOTitle('Product Name')
      expect(title).toBe('Product Name | MercadoClone')
    })

    it('should handle empty brand', () => {
      const title = generateSEOTitle('Product Name', '')
      expect(title).toBe('Product Name | MercadoClone')
    })
  })

  describe('generateBreadcrumbPath', () => {
    it('should generate breadcrumb path correctly', () => {
      const path = generateBreadcrumbPath(['Electronics', 'Audio', 'Headphones'])
      
      expect(path).toEqual([
        { label: 'Início', href: '/' },
        { label: 'Electronics', href: '/electronics' },
        { label: 'Audio', href: '/electronics/audio' },
        { label: 'Headphones', href: '/electronics/audio/headphones' },
      ])
    })

    it('should handle single category', () => {
      const path = generateBreadcrumbPath(['Electronics'])
      
      expect(path).toEqual([
        { label: 'Início', href: '/' },
        { label: 'Electronics', href: '/electronics' },
      ])
    })

    it('should handle empty array', () => {
      const path = generateBreadcrumbPath([])
      
      expect(path).toEqual([
        { label: 'Início', href: '/' },
      ])
    })
  })

  describe('isOnline', () => {
    it('should return true when online', () => {
      Object.defineProperty(navigator, 'onLine', { value: true, writable: true })
      expect(isOnline()).toBe(true)
    })

    it('should return false when offline', () => {
      Object.defineProperty(navigator, 'onLine', { value: false, writable: true })
      expect(isOnline()).toBe(false)
    })
  })

  describe('delay', () => {
    it('should delay execution', async () => {
      const start = Date.now()
      await delay(100)
      const end = Date.now()
      expect(end - start).toBeGreaterThanOrEqual(90) // Allow some margin
    })
  })

  describe('getRetryDelay', () => {
    it('should calculate exponential backoff correctly', () => {
      expect(getRetryDelay(0)).toBe(1000) // 1000 * 2^0
      expect(getRetryDelay(1)).toBe(2000) // 1000 * 2^1
      expect(getRetryDelay(2)).toBe(4000) // 1000 * 2^2
      expect(getRetryDelay(3)).toBe(8000) // 1000 * 2^3
    })

    it('should respect max delay', () => {
      expect(getRetryDelay(10)).toBe(30000) // Should be capped at 30s
    })

    it('should use custom base delay', () => {
      expect(getRetryDelay(1, 500)).toBe(1000) // 500 * 2^1
    })
  })

  describe('isValidImageUrl', () => {
    it('should validate correct image URLs', () => {
      expect(isValidImageUrl('https://example.com/image.jpg')).toBe(true)
      expect(isValidImageUrl('https://example.com/image.png')).toBe(true)
      expect(isValidImageUrl('https://example.com/image.gif')).toBe(true)
      expect(isValidImageUrl('https://example.com/image.webp')).toBe(true)
      expect(isValidImageUrl('https://example.com/image.avif')).toBe(true)
    })

    it('should reject invalid image URLs', () => {
      expect(isValidImageUrl('https://example.com/file.pdf')).toBe(false)
      expect(isValidImageUrl('https://example.com/page.html')).toBe(false)
      expect(isValidImageUrl('not-a-url')).toBe(false)
      expect(isValidImageUrl('')).toBe(false)
    })

    it('should handle case insensitive extensions', () => {
      expect(isValidImageUrl('https://example.com/IMAGE.JPG')).toBe(true)
      expect(isValidImageUrl('https://example.com/image.PNG')).toBe(true)
    })
  })

  describe('generateImagePlaceholder', () => {
    it('should generate valid SVG placeholder', () => {
      const placeholder = generateImagePlaceholder(300, 200)
      
      expect(placeholder).toContain('data:image/svg+xml;base64,')
      expect(placeholder).toContain('300')
      expect(placeholder).toContain('200')
    })

    it('should handle different dimensions', () => {
      const placeholder1 = generateImagePlaceholder(100, 100)
      const placeholder2 = generateImagePlaceholder(500, 300)
      
      expect(placeholder1).not.toBe(placeholder2)
    })
  })

  describe('debounce', () => {
    beforeEach(() => {
      jest.useFakeTimers()
    })

    afterEach(() => {
      jest.useRealTimers()
    })

    it('should debounce function calls', () => {
      const mockFn = jest.fn()
      const debouncedFn = debounce(mockFn, 1000)

      debouncedFn('arg1')
      debouncedFn('arg2')
      debouncedFn('arg3')

      expect(mockFn).not.toHaveBeenCalled()

      jest.advanceTimersByTime(1000)

      expect(mockFn).toHaveBeenCalledTimes(1)
      expect(mockFn).toHaveBeenCalledWith('arg3')
    })

    it('should reset timer on subsequent calls', () => {
      const mockFn = jest.fn()
      const debouncedFn = debounce(mockFn, 1000)

      debouncedFn()
      jest.advanceTimersByTime(500)
      debouncedFn()
      jest.advanceTimersByTime(500)

      expect(mockFn).not.toHaveBeenCalled()

      jest.advanceTimersByTime(500)
      expect(mockFn).toHaveBeenCalledTimes(1)
    })
  })

  describe('throttle', () => {
    beforeEach(() => {
      jest.useFakeTimers()
    })

    afterEach(() => {
      jest.useRealTimers()
    })

    it('should throttle function calls', () => {
      const mockFn = jest.fn()
      const throttledFn = throttle(mockFn, 1000)

      throttledFn()
      throttledFn()
      throttledFn()

      expect(mockFn).toHaveBeenCalledTimes(1)

      jest.advanceTimersByTime(1000)

      throttledFn()
      expect(mockFn).toHaveBeenCalledTimes(2)
    })
  })

  describe('cn (className merger)', () => {
    it('should merge valid class names', () => {
      expect(cn('class1', 'class2', 'class3')).toBe('class1 class2 class3')
    })

    it('should filter out falsy values', () => {
      expect(cn('class1', null, undefined, false, 'class2')).toBe('class1 class2')
    })

    it('should handle empty input', () => {
      expect(cn()).toBe('')
      expect(cn(null, undefined, false)).toBe('')
    })

    it('should handle mixed valid and invalid values', () => {
      expect(cn('valid', '', null, 'another-valid')).toBe('valid another-valid')
    })
  })
})