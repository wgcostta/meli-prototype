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
      
      // Should have main image skeleton
      const mainImageSkeleton = document.querySelector('.w-full.h-96.bg-gray-200') ||
                               document.querySelector('[data-testid="main-image-skeleton"]')
      expect(mainImageSkeleton).toBeInTheDocument()
      
      // Should have thumbnail skeletons
      const thumbnails = document.querySelectorAll('.w-16.h-16.bg-gray-200')
      expect(thumbnails.length).toBeGreaterThanOrEqual(4)
    })

    it('should have proper loading animations', () => {
      render(<ProductGallerySkeleton />)
      
      const skeletonElements = document.querySelectorAll('.animate-pulse')
      expect(skeletonElements.length).toBeGreaterThan(0)
    })
  })

  describe('ProductInfoSkeleton', () => {
    it('should render info skeleton', () => {
      render(<ProductInfoSkeleton />)
      
      // Should have title skeleton
      const titleSkeleton = document.querySelector('.h-6.bg-gray-200') ||
                           document.querySelector('.bg-gray-200')
      expect(titleSkeleton).toBeInTheDocument()
      
      // Should have price skeleton  
      const priceSkeleton = document.querySelector('.h-8.bg-gray-200')
      expect(priceSkeleton).toBeInTheDocument()
      
      // Should have button skeletons
      const buttonSkeletons = document.querySelectorAll('.h-12.bg-gray-200')
      expect(buttonSkeletons.length).toBeGreaterThanOrEqual(2)
    })

    it('should have loading animations', () => {
      render(<ProductInfoSkeleton />)
      
      const animatedElements = document.querySelectorAll('.animate-pulse')
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

    it('should show connection error icon for network errors', () => {
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
      const wifiOffIcon = document.querySelector('[data-testid="wifi-off-icon"]')
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

      const retryButton = screen.getByText('Tentando novamente...')
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

    it('should show progress bar', () => {
      render(
        <RetryLoadingState
          message="Loading..."
          retryCount={2}
        />
      )

      const progressBar = document.querySelector('.bg-blue-500.h-2') ||
                         document.querySelector('.bg-blue-500')
      expect(progressBar).toBeInTheDocument()
    })

    it('should have spinning icon', () => {
      render(
        <RetryLoadingState
          message="Loading..."
          retryCount={1}
        />
      )

      const spinningIcon = document.querySelector('[data-testid="refresh-icon"]') ||
                          document.querySelector('.animate-spin')
      expect(spinningIcon).toBeInTheDocument()
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
      expect(notice).toHaveClass('fixed', 'top-4', 'right-4')
    })

    it('should show wifi icon', () => {
      render(<ReconnectionNotice onDismiss={mockOnDismiss} />)
      
      const wifiIcon = document.querySelector('[data-testid="wifi-icon"]') ||
                      document.querySelector('svg')
      expect(wifiIcon).toBeInTheDocument()
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

      const heading = screen.getByRole('heading', { level: 2 })
      expect(heading).toBeInTheDocument()
    })
  })
})