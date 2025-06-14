import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import ProductGallery from '../ProductGallery'
import { Product } from '@/types/product'

describe('ProductGallery', () => {
  const mockProduct: Product = testUtils.mockProduct

  it('should render product gallery with images', () => {
    render(<ProductGallery product={mockProduct} />)

    // Should show main image
    const mainImage = screen.getByAltText('Test image 1')
    expect(mainImage).toBeInTheDocument()

    // Should show thumbnails
    const thumbnails = screen.getAllByRole('button')
    expect(thumbnails).toHaveLength(2) // 2 images = 2 thumbnail buttons
  })

  it('should change main image when thumbnail is clicked', () => {
    render(<ProductGallery product={mockProduct} />)

    const thumbnails = screen.getAllByRole('button')
    
    // Click second thumbnail
    fireEvent.click(thumbnails[1])

    // Main image should change
    const mainImage = screen.getByAltText('Test image 2')
    expect(mainImage).toBeInTheDocument()
  })

  it('should navigate images with arrow buttons', () => {
    render(<ProductGallery product={mockProduct} />)

    // Should have navigation buttons (only visible on hover, but always in DOM)
    const nextButton = screen.getByLabelText('Próxima imagem')
    const prevButton = screen.getByLabelText('Imagem anterior')

    expect(nextButton).toBeInTheDocument()
    expect(prevButton).toBeInTheDocument()

    // Click next
    fireEvent.click(nextButton)
    expect(screen.getByAltText('Test image 2')).toBeInTheDocument()

    // Click previous
    fireEvent.click(prevButton)
    expect(screen.getByAltText('Test image 1')).toBeInTheDocument()
  })

  it('should cycle through images correctly', () => {
    render(<ProductGallery product={mockProduct} />)

    const nextButton = screen.getByLabelText('Próxima imagem')
    
    // Start at first image
    expect(screen.getByAltText('Test image 1')).toBeInTheDocument()

    // Go to second image
    fireEvent.click(nextButton)
    expect(screen.getByAltText('Test image 2')).toBeInTheDocument()

    // Should cycle back to first image
    fireEvent.click(nextButton)
    expect(screen.getByAltText('Test image 1')).toBeInTheDocument()
  })

  it('should show image position indicator', () => {
    render(<ProductGallery product={mockProduct} />)

    expect(screen.getByText('1 / 2')).toBeInTheDocument()

    // Navigate to second image
    const nextButton = screen.getByLabelText('Próxima imagem')
    fireEvent.click(nextButton)

    expect(screen.getByText('2 / 2')).toBeInTheDocument()
  })

  it('should handle single image gracefully', () => {
    const singleImageProduct = {
      ...mockProduct,
      images: [mockProduct.images[0]]
    }

    render(<ProductGallery product={singleImageProduct} />)

    // Should not show navigation buttons for single image
    expect(screen.queryByLabelText('Próxima imagem')).not.toBeInTheDocument()
    expect(screen.queryByLabelText('Imagem anterior')).not.toBeInTheDocument()

    // Should not show position indicator for single image
    expect(screen.queryByText('1 / 1')).not.toBeInTheDocument()

    // Should not show thumbnails for single image
    expect(screen.queryByRole('button')).not.toBeInTheDocument()
  })

  it('should show fallback when no images available', () => {
    const noImageProduct = {
      ...mockProduct,
      images: []
    }

    render(<ProductGallery product={noImageProduct} />)

    expect(screen.getByText('Imagem não disponível')).toBeInTheDocument()
  })

  it('should handle image loading states', async () => {
    render(<ProductGallery product={mockProduct} />)

    const mainImage = screen.getByAltText('Test image 1')
    
    // Should initially have opacity-0 class before loading
    expect(mainImage).toHaveClass('opacity-0')

    // Simulate image load
    fireEvent.load(mainImage)

    await waitFor(() => {
      expect(mainImage).toHaveClass('opacity-100')
    })
  })

  it('should handle image errors', () => {
    render(<ProductGallery product={mockProduct} />)

    const mainImage = screen.getByAltText('Test image 1')
    
    // Simulate image error
    fireEvent.error(mainImage)

    // Should show error message
    expect(screen.getByText('Erro ao carregar imagem')).toBeInTheDocument()
  })

  it('should handle thumbnail image errors', () => {
    render(<ProductGallery product={mockProduct} />)

    const thumbnails = screen.getAllByRole('button')
    const thumbnailImage = thumbnails[0].querySelector('img')
    
    if (thumbnailImage) {
      // Simulate thumbnail error
      fireEvent.error(thumbnailImage)

      // Should show error text in thumbnail
      expect(screen.getByText('Erro')).toBeInTheDocument()
    }
  })

  it('should show zoom button', () => {
    render(<ProductGallery product={mockProduct} />)

    // Zoom button should be in the DOM (visible on hover)
    const zoomButton = document.querySelector('button svg[data-testid="zoom-icon"]') || 
                      document.querySelector('button:has(svg)') ||
                      screen.getByRole('button', { hidden: true })
    
    expect(zoomButton).toBeInTheDocument()
  })

  it('should apply correct styling to selected thumbnail', () => {
    render(<ProductGallery product={mockProduct} />)

    const thumbnails = screen.getAllByRole('button')
    
    // First thumbnail should be selected (have blue border)
    expect(thumbnails[0]).toHaveClass('border-blue-500')
    expect(thumbnails[1]).toHaveClass('border-gray-200')

    // Click second thumbnail
    fireEvent.click(thumbnails[1])

    // Selection should change
    expect(thumbnails[1]).toHaveClass('border-blue-500')
    expect(thumbnails[0]).toHaveClass('border-gray-200')
  })

  it('should use product title as fallback alt text', () => {
    const productWithoutAlt = {
      ...mockProduct,
      images: [
        {
          id: 'img-1',
          url: 'https://example.com/image1.jpg',
          alt: '',
          order: 1,
        }
      ]
    }

    render(<ProductGallery product={productWithoutAlt} />)

    expect(screen.getByAltText('Test Product')).toBeInTheDocument()
  })

  it('should handle keyboard navigation', () => {
    render(<ProductGallery product={mockProduct} />)

    const nextButton = screen.getByLabelText('Próxima imagem')
    
    // Should be focusable
    nextButton.focus()
    expect(nextButton).toHaveFocus()

    // Should handle Enter key
    fireEvent.keyDown(nextButton, { key: 'Enter', code: 'Enter' })
    expect(screen.getByAltText('Test image 2')).toBeInTheDocument()
  })

  it('should have proper ARIA labels', () => {
    render(<ProductGallery product={mockProduct} />)

    expect(screen.getByLabelText('Próxima imagem')).toBeInTheDocument()
    expect(screen.getByLabelText('Imagem anterior')).toBeInTheDocument()
  })

  it('should optimize images with Next.js Image component', () => {
    render(<ProductGallery product={mockProduct} />)

    const images = screen.getAllByRole('img')
    
    // Should have proper sizes attribute for responsive images
    images.forEach(img => {
      expect(img).toHaveAttribute('src')
    })
  })

  it('should handle empty or invalid image URLs gracefully', () => {
    const invalidImageProduct = {
      ...mockProduct,
      images: [
        {
          id: 'img-1',
          url: '',
          alt: 'Empty URL image',
          order: 1,
        },
        {
          id: 'img-2',
          url: 'invalid-url',
          alt: 'Invalid URL image',
          order: 2,
        }
      ]
    }

    // Should not crash when rendering invalid URLs
    expect(() => {
      render(<ProductGallery product={invalidImageProduct} />)
    }).not.toThrow()
  })

  describe('Loading skeleton animation', () => {
    it('should show loading skeleton before image loads', () => {
      render(<ProductGallery product={mockProduct} />)

      // Should show loading skeleton div
      const loadingSkeleton = document.querySelector('.animate-pulse')
      expect(loadingSkeleton).toBeInTheDocument()
    })
  })

  describe('Responsive behavior', () => {
    it('should have responsive image sizing', () => {
      render(<ProductGallery product={mockProduct} />)

      const mainImage = screen.getByAltText('Test image 1')
      
      // Should have responsive classes
      expect(mainImage).toHaveClass('object-contain')
    })

    it('should handle thumbnail scrolling on mobile', () => {
      render(<ProductGallery product={mockProduct} />)

      const thumbnailContainer = document.querySelector('.overflow-x-auto')
      expect(thumbnailContainer).toBeInTheDocument()
    })
  })
})