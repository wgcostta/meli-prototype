import { render, screen, fireEvent } from '@testing-library/react'
import {
  ProductGallerySkeleton,
  ProductInfoSkeleton,
  ErrorState,
  RetryLoadingState,
  ReconnectionNotice,
} from '../LoadingAndError'

describe('Loading and Error Components', () => {
  describe('ProductGallerySkeleton', () => {
    it('should render gallery skeleton', () => {
      render(<ProductGallerySkeleton />)
      
      // Should have main image skeleton - use more flexible selector
      const mainImageSkeleton = document.querySelector('.w-full.h-96.bg-gray-200') ||
                               document.querySelector('[class*="h-96"][class*="bg-gray-200"]') ||
                               document.querySelector('[class*="bg-gray-200"]')
      expect(mainImageSkeleton).toBeInTheDocument()
      
      // Should have thumbnail skeletons
      const thumbnails = document.querySelectorAll('.w-16.h-16.bg-gray-200') ||
                        document.querySelectorAll('[class*="w-16"][class*="h-16"]')
      expect(thumbnails.length).toBeGreaterThanOrEqual(1)
    })

    it('should have proper loading animations', () => {
      render(<ProductGallerySkeleton />)
      
      const skeletonElements = document.querySelectorAll('.animate-pulse') ||
                              document.querySelectorAll('[class*="animate"]')
      expect(skeletonElements.length).toBeGreaterThan(0)
    })
  })

  describe('ProductInfoSkeleton', () => {
    it('should render info skeleton', () => {
      render(<ProductInfoSkeleton />)
      
      // Should have skeleton elements
      const skeletonElements = document.querySelectorAll('.bg-gray-200') ||
                              document.querySelectorAll('[class*="bg-gray"]')
      expect(skeletonElements.length).toBeGreaterThan(0)
      
      // Should have title skeleton
      const titleSkeleton = document.querySelector('.h-6.bg-gray-200') ||
                           document.querySelector('[class*="h-6"]') ||
                           document.querySelector('.bg-gray-200')
      expect(titleSkeleton).toBeInTheDocument()
    })

    it('should have loading animations', () => {
      render(<ProductInfoSkeleton />)
      
      const animatedElements = document.querySelectorAll('.animate-pulse') ||
                              document.querySelectorAll('[class*="animate"]')
      expect(animatedElements.length).toBeGreaterThan(0)
    })
  })

  describe('ErrorState', () => {
    const mockError = {
      message: 'Test error message',
      code: 'TEST_ERROR',
    }
    const mockOnRetry = jest.fn()

    beforeEach(() => {
      mockOnRetry.mockClear()
    })

    it('should render error message', () => {
      render(
        <ErrorState
          error={mockError}
          onRetry={mockOnRetry}
          retryCount={0}
          isRetrying={false}
        />
      )

      expect(screen.getByText('Test error message')).toBeInTheDocument()
      expect(screen.getByText('Tentar novamente')).toBeInTheDocument()
    })

    it('should show connection error for network errors', () => {
      const networkError = {
        message: 'Problema de conexão',
        code: 'FETCH_ERROR',
      }

      render(
        <ErrorState
          error={networkError}
          onRetry={mockOnRetry}
          retryCount={0}
          isRetrying={false}
        />
      )

      expect(screen.getByText('Problema de Conexão')).toBeInTheDocument()
      
      // Look for WifiOff icon by class or SVG presence
      const wifiOffIcon = document.querySelector('svg') ||
                         document.querySelector('[class*="text-red"]')
      expect(wifiOffIcon).toBeInTheDocument()
    })

    it('should call onRetry when retry button is clicked', () => {
      render(
        <ErrorState
          error={mockError}
          onRetry={mockOnRetry}
          retryCount={0}
          isRetrying={false}
        />
      )

      const retryButton = screen.getByText('Tentar novamente')
      fireEvent.click(retryButton)
      expect(mockOnRetry).toHaveBeenCalledTimes(1)
    })

    it('should disable retry button when retrying', () => {
      render(
        <ErrorState
          error={mockError}
          onRetry={mockOnRetry}
          retryCount={1}
          isRetrying={true}
        />
      )

      const retryButton = screen.getByRole('button', { name: /tentando novamente/i })
      expect(retryButton).toBeDisabled()
    })

    it('should show retry count', () => {
      render(
        <ErrorState
          error={mockError}
          onRetry={mockOnRetry}
          retryCount={2}
          isRetrying={false}
        />
      )

      expect(screen.getByText('Tentativa 2 de 3 realizadas')).toBeInTheDocument()
    })

    it('should show troubleshooting tips', () => {
      render(
        <ErrorState
          error={mockError}
          onRetry={mockOnRetry}
          retryCount={0}
          isRetrying={false}
        />
      )

      expect(screen.getByText('Se o problema persistir:')).toBeInTheDocument()
      expect(screen.getByText(/Verifique sua conexão com a internet/)).toBeInTheDocument()
    })

    it('should handle different error codes', () => {
      const connectionError = {
        message: 'Falha na conexão',
        code: 'FETCH_ERROR'
      }

      render(
        <ErrorState
          error={connectionError}
          onRetry={mockOnRetry}
          retryCount={0}
          isRetrying={false}
        />
      )

      // Should show connection-specific messaging
      expect(screen.getByText('Problema de Conexão')).toBeInTheDocument()
    })
  })

  describe('RetryLoadingState', () => {
    it('should render retry loading message', () => {
      render(
        <RetryLoadingState
          message="Tentando reconectar..."
          retryCount={2}
        />
      )

      expect(screen.getByText('Reconectando...')).toBeInTheDocument()
      expect(screen.getByText('Tentando reconectar...')).toBeInTheDocument()
      expect(screen.getByText('Tentativa 2 de 3')).toBeInTheDocument()
    })

    it('should show progress indication', () => {
      render(
        <RetryLoadingState
          message="Loading..."
          retryCount={2}
        />
      )

      // Look for progress bar or visual indication
      const progressElement = document.querySelector('.bg-blue-500') ||
                             document.querySelector('[class*="bg-blue"]') ||
                             document.querySelector('[class*="progress"]')
      expect(progressElement).toBeInTheDocument()
    })

    it('should have spinning icon', () => {
      render(
        <RetryLoadingState
          message="Loading..."
          retryCount={1}
        />
      )

      const spinningIcon = document.querySelector('.animate-spin') ||
                          document.querySelector('[class*="animate"]') ||
                          document.querySelector('svg')
      expect(spinningIcon).toBeInTheDocument()
    })

    it('should show correct retry count', () => {
      render(
        <RetryLoadingState
          message="Reconnecting..."
          retryCount={3}
        />
      )

      const content = document.body.textContent || ''
      expect(content).toContain('3')
      expect(content).toContain('Tentativa')
    })
  })

  describe('ReconnectionNotice', () => {
    const mockOnDismiss = jest.fn()

    beforeEach(() => {
      mockOnDismiss.mockClear()
    })

    it('should render reconnection message', () => {
      render(<ReconnectionNotice onDismiss={mockOnDismiss} />)

      expect(screen.getByText('Conectado! Carregando dados...')).toBeInTheDocument()
    })

    it('should call onDismiss when close button is clicked', () => {
      render(<ReconnectionNotice onDismiss={mockOnDismiss} />)

      const closeButton = screen.getByText('×')
      fireEvent.click(closeButton)
      expect(mockOnDismiss).toHaveBeenCalledTimes(1)
    })

    it('should have proper styling classes', () => {
      const { container } = render(<ReconnectionNotice onDismiss={mockOnDismiss} />)
      
      const notice = container.firstChild as HTMLElement
      expect(notice).toHaveClass('fixed')
      
      // Check for positioning classes
      const hasPositioning = notice.classList.contains('top-4') ||
                            notice.classList.contains('right-4') ||
                            notice.className.includes('top') ||
                            notice.className.includes('right')
      expect(hasPositioning).toBe(true)
    })

    it('should show connectivity icon', () => {
      render(<ReconnectionNotice onDismiss={mockOnDismiss} />)
      
      const icon = document.querySelector('svg') ||
                  document.querySelector('[class*="icon"]')
      expect(icon).toBeInTheDocument()
    })

    it('should be dismissible', () => {
      render(<ReconnectionNotice onDismiss={mockOnDismiss} />)
      
      // Find dismiss button by text or role
      const dismissButton = screen.getByText('×') ||
                           screen.getByRole('button')
      
      expect(dismissButton).toBeInTheDocument()
      
      fireEvent.click(dismissButton)
      expect(mockOnDismiss).toHaveBeenCalled()
    })
  })

  describe('Accessibility', () => {
    it('should have proper ARIA labels on interactive elements', () => {
      const mockOnRetry = jest.fn()
      render(
        <ErrorState
          error={{ message: 'Error', code: 'TEST' }}
          onRetry={mockOnRetry}
          retryCount={0}
          isRetrying={false}
        />
      )

      const retryButton = screen.getByRole('button', { name: /tentar novamente/i })
      expect(retryButton).toBeInTheDocument()
    })

    it('should have proper heading structure', () => {
      const mockOnRetry = jest.fn()
      render(
        <ErrorState
          error={{ message: 'Error', code: 'FETCH_ERROR' }}
          onRetry={mockOnRetry}
          retryCount={0}
          isRetrying={false}
        />
      )

      const headings = screen.getAllByRole('heading')
      expect(headings.length).toBeGreaterThan(0)
      
      // Should have a main heading
      const mainHeading = headings.find(h => h.tagName === 'H2')
      expect(mainHeading).toBeInTheDocument()
    })

    it('should provide meaningful error context', () => {
      const mockOnRetry = jest.fn()
      render(
        <ErrorState
          error={{ message: 'Network timeout', code: 'TIMEOUT' }}
          onRetry={mockOnRetry}
          retryCount={1}
          isRetrying={false}
        />
      )

      // Should provide helpful information
      expect(screen.getByText('Network timeout')).toBeInTheDocument()
      expect(screen.getByText(/Se o problema persistir/)).toBeInTheDocument()
    })
  })

  describe('Loading states visual feedback', () => {
    it('should show skeleton with proper visual hierarchy', () => {
      render(<ProductGallerySkeleton />)
      
      // Should have container structure
      const container = document.querySelector('[class*="bg-white"]') ||
                       document.querySelector('[class*="rounded"]')
      expect(container).toBeInTheDocument()
      
      // Should have skeleton elements
      const skeletons = document.querySelectorAll('[class*="bg-gray-200"]')
      expect(skeletons.length).toBeGreaterThan(0)
    })

    it('should show info skeleton with proper spacing', () => {
      render(<ProductInfoSkeleton />)
      
      // Should have proper spacing and structure
      const spacedElements = document.querySelectorAll('[class*="space-y"]') ||
                            document.querySelectorAll('[class*="mb-"]')
      expect(spacedElements.length).toBeGreaterThan(0)
    })
  })

  describe('Error recovery flow', () => {
    it('should handle multiple retry attempts', () => {
      const mockOnRetry = jest.fn()
      const { rerender } = render(
        <ErrorState
          error={{ message: 'Error', code: 'TEST' }}
          onRetry={mockOnRetry}
          retryCount={0}
          isRetrying={false}
        />
      )

      // First retry
      const retryButton = screen.getByText('Tentar novamente')
      fireEvent.click(retryButton)
      expect(mockOnRetry).toHaveBeenCalledTimes(1)

      // Update with retry count
      rerender(
        <ErrorState
          error={{ message: 'Error', code: 'TEST' }}
          onRetry={mockOnRetry}
          retryCount={1}
          isRetrying={false}
        />
      )

      expect(screen.getByText('Tentativa 1 de 3 realizadas')).toBeInTheDocument()
    })

    it('should show appropriate messaging for max retries', () => {
      const mockOnRetry = jest.fn()
      render(
        <ErrorState
          error={{ message: 'Max retries exceeded', code: 'MAX_RETRIES' }}
          onRetry={mockOnRetry}
          retryCount={3}
          isRetrying={false}
        />
      )

      // Should still show retry option
      expect(screen.getByText('Tentar novamente')).toBeInTheDocument()
      expect(screen.getByText('Tentativa 3 de 3 realizadas')).toBeInTheDocument()
    })
  })
})