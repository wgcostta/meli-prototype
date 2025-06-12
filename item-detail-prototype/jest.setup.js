import '@testing-library/jest-dom'
import React from 'react'

// Mock Next.js Image component
jest.mock('next/image', () => {
  const MockedImage = (props) => {
    // eslint-disable-next-line @next/next/no-img-element, jsx-a11y/alt-text
    return React.createElement('img', {
      ...props,
      alt: props.alt || '',
    })
  }
  MockedImage.displayName = 'NextImage'
  return MockedImage
})

// Mock Next.js router
jest.mock('next/navigation', () => ({
  useRouter() {
    return {
      push: jest.fn(),
      replace: jest.fn(),
      prefetch: jest.fn(),
      back: jest.fn(),
      forward: jest.fn(),
      refresh: jest.fn(),
    }
  },
  useSearchParams() {
    return new URLSearchParams()
  },
  usePathname() {
    return '/'
  },
}))

// Mock window.fetch
global.fetch = jest.fn()

// Mock IntersectionObserver
global.IntersectionObserver = jest.fn().mockImplementation(() => ({
  observe: jest.fn(),
  unobserve: jest.fn(),
  disconnect: jest.fn(),
}))

// Mock ResizeObserver
global.ResizeObserver = jest.fn().mockImplementation(() => ({
  observe: jest.fn(),
  unobserve: jest.fn(),
  disconnect: jest.fn(),
}))

// Mock matchMedia
Object.defineProperty(window, 'matchMedia', {
  writable: true,
  value: jest.fn().mockImplementation(query => ({
    matches: false,
    media: query,
    onchange: null,
    addListener: jest.fn(), // deprecated
    removeListener: jest.fn(), // deprecated
    addEventListener: jest.fn(),
    removeEventListener: jest.fn(),
    dispatchEvent: jest.fn(),
  })),
})

// Mock navigator.onLine
Object.defineProperty(navigator, 'onLine', {
  writable: true,
  value: true,
})

// Setup environment variables for tests
process.env.NEXT_PUBLIC_API_URL = 'http://localhost:3001'
process.env.NEXT_PUBLIC_API_TIMEOUT = '10000'

// Global test helpers
global.testUtils = {
  // Mock product data
  mockProduct: {
    id: 'test-product-1',
    title: 'Test Product',
    description: 'This is a test product description',
    shortDescription: 'Short test description',
    price: {
      current: 299.99,
      original: 399.99,
      currency: 'BRL',
      discount: 25,
    },
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
      },
    ],
    category: {
      id: 'cat-1',
      name: 'Electronics',
      path: ['Electronics', 'Audio', 'Headphones'],
    },
    brand: 'Test Brand',
    sku: 'TEST-001',
    stock: {
      available: 10,
      total: 50,
      isAvailable: true,
    },
    rating: {
      average: 4.5,
      count: 150,
      distribution: {
        5: 75,
        4: 45,
        3: 20,
        2: 7,
        1: 3,
      },
    },
    paymentMethods: [
      {
        type: 'credit_card',
        name: 'Cartão de crédito',
        installments: 12,
      },
    ],
    shipping: {
      free: true,
      estimatedDays: 2,
      cost: 0,
      description: 'Frete grátis',
    },
    seller: {
      id: 'seller-1',
      name: 'Test Store',
      reputation: 4.8,
      location: 'São Paulo, SP',
      isOfficial: true,
      positiveRating: 98,
      yearsOnPlatform: 5,
    },
    features: [
      'Noise cancellation',
      'Bluetooth 5.0',
      'Long battery life',
    ],
    specifications: {
      'Connection Type': 'Bluetooth',
      'Battery Life': '30 hours',
      Weight: '250g',
    },
    warranty: '12 months',
    createdAt: '2024-01-01T00:00:00Z',
    updatedAt: '2024-01-01T00:00:00Z',
  },

  // Mock API response
  mockApiResponse: {
    data: null, // Will be set to mockProduct
    success: true,
    message: 'Success',
    timestamp: '2024-01-01T00:00:00Z',
  },

  // Mock fetch response
  createMockResponse: (data, ok = true, status = 200) => ({
    ok,
    status,
    json: async () => data,
    headers: new Headers(),
    statusText: ok ? 'OK' : 'Error',
  }),
}

// Set mockProduct in mockApiResponse
global.testUtils.mockApiResponse.data = global.testUtils.mockProduct

// Clean up after each test
afterEach(() => {
  jest.clearAllMocks()
  if (global.fetch && global.fetch.mockClear) {
    global.fetch.mockClear()
  }
})