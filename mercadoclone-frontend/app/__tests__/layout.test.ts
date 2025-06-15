import { render, screen } from '@testing-library/react';
import RootLayout, { metadata } from './layout';

describe('RootLayout', () => {
  // Test suite for metadata
  describe('Metadata', () => {
    test('should have correct title', () => {
      expect(metadata.title).toBe('MercadoClone - Fone de Ouvido Bluetooth Premium');
    });

    test('should have correct description', () => {
      expect(metadata.description).toBe(
        'Fone de Ouvido Bluetooth Premium com cancelamento ativo de ruído. Som de alta qualidade, bateria de longa duração e design confortável.'
      );
    });

    test('should have correct keywords', () => {
      expect(metadata.keywords).toEqual(['fone de ouvido', 'bluetooth', 'cancelamento de ruído', 'wireless', 'premium']);
    });

    test('should have correct OpenGraph title', () => {
      expect(metadata.openGraph?.title).toBe('Fone de Ouvido Bluetooth Premium - MercadoClone');
    });

    test('should have correct OpenGraph description', () => {
      expect(metadata.openGraph?.description).toBe('Som de alta qualidade com cancelamento ativo de ruído');
    });

    test('should have correct OpenGraph image', () => {
      expect(metadata.openGraph?.images).toEqual([
        'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=1200&h=630&fit=crop',
      ]);
    });
  });

  // Test suite for RootLayout component
  describe('RootLayout Component', () => {
    test('should render children correctly', () => {
      const testContent = 'Test Content';
      render(<RootLayout><div>{testContent}</div></RootLayout>);

      expect(screen.getByText(testContent)).toBeInTheDocument();
    });

    test('should render with correct HTML structure', () => {
      const testContent = 'Test Content';
      const { container } = render(<RootLayout><div>{testContent}</div></RootLayout>);

      // Check if the root element is an HTML element
      expect(container.firstChild?.nodeName.toLowerCase()).toBe('html');

      // Check if the body contains the children
      const body = container.querySelector('body');
      expect(body).toBeInTheDocument();
      expect(body?.textContent).toContain(testContent);
    });
  });
});