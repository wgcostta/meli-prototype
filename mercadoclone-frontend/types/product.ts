// Interfaces para tipagem dos dados da API
export interface ProductImage {
  id: string;
  url: string;
  alt: string;
  order: number;
}

export interface ProductRating {
  average: number;
  count: number;
  distribution: {
    5: number;
    4: number;
    3: number;
    2: number;
    1: number;
  };
}

export interface PaymentMethod {
  type: string;
  name: string;
  icon?: string;
  installments?: number;
  discount?: number;
}

export interface Shipping {
  free: boolean;
  estimatedDays: number;
  cost: number;
  description: string;
}

export interface Seller {
  id: string;
  name: string;
  reputation: number;
  location: string;
  isOfficial: boolean;
  positiveRating: number;
  yearsOnPlatform: number;
  avatar?: string;
}

export interface Product {
  id: string;
  title: string;
  description: string;
  shortDescription?: string;
  price: {
    current: number;
    original?: number;
    currency: string;
    discount?: number;
  };
  images: ProductImage[];
  category: {
    id: string;
    name: string;
    path: string[];
  };
  brand: string;
  sku: string;
  stock: {
    available: number;
    total: number;
    isAvailable: boolean;
  };
  rating: ProductRating;
  paymentMethods: PaymentMethod[];
  shipping: Shipping;
  seller: Seller;
  features: string[];
  specifications: Record<string, string>;
  warranty: string;
  createdAt: string;
  updatedAt: string;
}

export interface ApiResponse<T> {
  data: T;
  success: boolean;
  message?: string;
  timestamp: string;
}

export interface ApiError {
  message: string;
  code: string;
  details?: Record<string, any>;
}

// Estados de loading e erro
export interface ProductState {
  product: Product | null;
  loading: boolean;
  error: ApiError | null;
  retryCount: number;
}

// Reviews (se necessário expandir)
export interface Review {
  id: string;
  userId: string;
  userName: string;
  rating: number;
  comment: string;
  date: string;
  verified: boolean;
  helpful: number;
  images?: string[];
}

export interface ReviewsResponse {
  reviews: Review[];
  pagination: {
    total: number;
    page: number;
    limit: number;
    totalPages: number;
  };
  summary: {
    averageRating: number;
    totalReviews: number;
    distribution: Record<number, number>;
  };
}