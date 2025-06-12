import type { Product, ApiResponse } from './product'

declare global {
  namespace jest {
    interface Matchers<R> {
      toBeInTheDocument(): R
      toHaveClass(className: string): R
      toHaveAttribute(attr: string, value?: string): R
      toHaveStyle(style: string | Record<string, any>): R
      toBeVisible(): R
      toBeDisabled(): R
      toHaveFocus(): R
    }
  }

  var testUtils: {
    mockProduct: Product
    mockApiResponse: ApiResponse<Product>
    createMockResponse: (data: any, ok?: boolean, status?: number) => {
      ok: boolean
      status: number
      json: () => Promise<any>
      headers: Headers
      statusText: string
    }
  }

  var fetch: jest.MockedFunction<typeof fetch>
}

export {}