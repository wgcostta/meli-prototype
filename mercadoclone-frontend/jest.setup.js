import '@testing-library/jest-dom'

// Mock Next.js Image component
jest.mock('next/image', () => ({
  __esModule: true,
  default: (props) => {
    const { src, alt, ...rest } = props
    return <img src={src} alt={alt} {...rest} />
  },
}))

// Mock Lucide React icons with data-testid for easier testing
jest.mock('lucide-react', () => ({
  Heart: ({ className, size, fill, ...props }) => (
    <svg data-testid="heart-icon" className={className} fill={fill} {...props} />
  ),
  Share2: ({ className, size, ...props }) => (
    <svg data-testid="share-icon" className={className} {...props} />
  ),
  Star: ({ className, size, fill, ...props }) => (
    <svg data-testid="star-icon" className={className} fill={fill} {...props} />
  ),
  Truck: ({ className, size, ...props }) => (
    <svg data-testid="truck-icon" className={className} {...props} />
  ),
  Shield: ({ className, size, ...props }) => (
    <svg data-testid="shield-icon" className={className} {...props} />
  ),
  CreditCard: ({ className, size, ...props }) => (
    <svg data-testid="credit-card-icon" className={className} {...props} />
  ),
  Plus: ({ className, size, ...props }) => (
    <svg data-testid="plus-icon" className={className} {...props} />
  ),
  Minus: ({ className, size, ...props }) => (
    <svg data-testid="minus-icon" className={className} {...props} />
  ),
  ChevronLeft: ({ className, size, ...props }) => (
    <svg data-testid="chevron-left-icon" className={className} {...props} />
  ),
  ChevronRight: ({ className, size, ...props }) => (
    <svg data-testid="chevron-right-icon" className={className} {...props} />
  ),
  ZoomIn: ({ className, size, ...props }) => (
    <svg data-testid="zoom-icon" className={className} {...props} />
  ),
  ChevronDown: ({ className, size, ...props }) => (
    <svg data-testid="chevron-down-icon" className={className} {...props} />
  ),
  ChevronUp: ({ className, size, ...props }) => (
    <svg data-testid="chevron-up-icon" className={className} {...props} />
  ),
  RefreshCw: ({ className, size, ...props }) => (
    <svg data-testid="refresh-icon" className={className} {...props} />
  ),
  AlertCircle: ({ className, size, ...props }) => (
    <svg data-testid="alert-icon" className={className} {...props} />
  ),
  Wifi: ({ className, size, ...props }) => (
    <svg data-testid="wifi-icon" className={className} {...props} />
  ),
  WifiOff: ({ className, size, ...props }) => (
    <svg data-testid="wifi-off-icon" className={className} {...props} />
  ),
  Search: ({ className, size, ...props }) => (
    <svg data-testid="search-icon" className={className} {...props} />
  ),
  User: ({ className, size, ...props }) => (
    <svg data-testid="user-icon" className={className} {...props} />
  ),
  ShoppingCart: ({ className, size, ...props }) => (
    <svg data-testid="shopping-cart-icon" className={className} {...props} />
  ),
}))

// Mock fetch globalmente
global.fetch = jest.fn()

// Global testUtils - dados de teste padronizados
global.testUtils = {
  mockProduct: {
    id: 'test-product-1',
    title: 'Test Product',
    description: 'This is a test product description that is long enough to test the expand/collapse functionality. It contains multiple sentences and should provide a good test case for various text manipulation features.',
    shortDescription: 'Short test description for quick overview',
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
      'Bateria de longa duração',
      'Design ergonômico',
      'Conexão Bluetooth 5.0'
    ],
    specifications: {
      'Marca': 'Test Brand',
      'Modelo': 'TB-001',
      'Cor': 'Azul',
      'Material': 'Plástico ABS',
      'Peso': '250g',
      'Dimensões': '18x15x7cm',
      'Conectividade': 'Bluetooth 5.0',
      'Bateria': '20 horas'
    },
    warranty: '12 months',
    category: {
      id: 'electronics',
      name: 'Electronics',
      path: ['Electronics', 'Audio', 'Headphones']
    },
    createdAt: '2024-01-01T00:00:00Z',
    updatedAt: '2024-01-01T00:00:00Z'
  },

  mockApiResponse: {
    data: {
      id: 'test-product-1',
      title: 'Test Product',
      description: 'Test description',
      brand: 'Test Brand',
      sku: 'TB-001'
    },
    success: true,
    message: 'Product retrieved successfully',
    timestamp: '2024-01-01T00:00:00Z'
  },

  createMockResponse: (data, ok = true, status = 200) => ({
    ok,
    status,
    statusText: ok ? 'OK' : 'Error',
    json: jest.fn().mockResolvedValue(data),
    text: jest.fn().mockResolvedValue(JSON.stringify(data)),
    headers: new Headers({
      'content-type': 'application/json',
    }),
  })
}

// Reset mocks before each test
beforeEach(() => {
  if (global.fetch && typeof global.fetch.mockClear === 'function') {
    global.fetch.mockClear()
  }
})

// Suppress console errors in tests (opcional)
const originalError = console.error
beforeAll(() => {
  console.error = (...args) => {
    if (
      typeof args[0] === 'string' &&
      args[0].includes('Warning: ReactDOM.render is deprecated')
    ) {
      return
    }
    originalError.call(console, ...args)
  }
})

afterAll(() => {
  console.error = originalError
})

// Mock window.matchMedia (para testes responsivos)
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

// Mock IntersectionObserver
global.IntersectionObserver = class IntersectionObserver {
  constructor() {}
  disconnect() {}
  observe() {}
  unobserve() {}
}

// Mock ResizeObserver
global.ResizeObserver = class ResizeObserver {
  constructor() {}
  disconnect() {}
  observe() {}
  unobserve() {}
}

// Mock scrollTo
global.scrollTo = jest.fn()

// Mock localStorage (se necessário)
const localStorageMock = {
  getItem: jest.fn(),
  setItem: jest.fn(),
  removeItem: jest.fn(),
  clear: jest.fn(),
}
global.localStorage = localStorageMock

// Mock sessionStorage (se necessário)
const sessionStorageMock = {
  getItem: jest.fn(),
  setItem: jest.fn(),
  removeItem: jest.fn(),
  clear: jest.fn(),
}
global.sessionStorage = sessionStorageMock