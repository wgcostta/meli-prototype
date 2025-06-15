// Utilitários para formatação e helpers
export function formatPrice(price: number, currency: string = 'BRL'): string {
  // Always format as BRL for Brazilian e-commerce site
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL', // Force BRL regardless of input currency
  }).format(price);
}

export function formatDate(date: string): string {
  return new Intl.DateTimeFormat('pt-BR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  }).format(new Date(date));
}

export function calculateDiscount(original: number, current: number): number {
  return Math.round(((original - current) / original) * 100);
}

export function truncateText(text: string, maxLength: number): string {
  if (text.length <= maxLength) return text;
  return text.substring(0, maxLength) + '...';
}

export function generateSEOTitle(productTitle: string, brand?: string): string {
  const parts = [productTitle];
  if (brand) parts.push(brand);
  parts.push('MercadoClone');
  return parts.join(' | ');
}

export function generateBreadcrumbPath(categoryPath: string[]): Array<{label: string, href: string}> {
  const breadcrumb = [{ label: 'Início', href: '/' }];
  
  categoryPath.forEach((category, index) => {
    const href = `/${categoryPath.slice(0, index + 1).join('/')}`.toLowerCase();
    breadcrumb.push({
      label: category,
      href
    });
  });
  
  return breadcrumb;
}

// Função para detectar se está online
export function isOnline(): boolean {
  return typeof navigator !== 'undefined' ? navigator.onLine : true;
}

// Função para delay
export function delay(ms: number): Promise<void> {
  return new Promise(resolve => setTimeout(resolve, ms));
}

// Função para retry com backoff exponencial
export function getRetryDelay(attempt: number, baseDelay: number = 1000): number {
  return Math.min(baseDelay * Math.pow(2, attempt), 30000);
}

// Validar URL de imagem
export function isValidImageUrl(url: string): boolean {
  try {
    const urlObj = new URL(url);
    return /\.(jpg|jpeg|png|gif|webp|avif)$/i.test(urlObj.pathname);
  } catch {
    return false;
  }
}

// Gerar placeholder para imagem
export function generateImagePlaceholder(width: number, height: number): string {
  return `data:image/svg+xml;base64,${btoa(
    `<svg width="${width}" height="${height}" viewBox="0 0 ${width} ${height}" fill="none" xmlns="http://www.w3.org/2000/svg">
      <rect width="${width}" height="${height}" fill="#E5E7EB"/>
      <path d="M${width/2-10} ${height/2-5}L${width/2+10} ${height/2-5}L${width/2} ${height/2+5}Z" fill="#9CA3AF"/>
    </svg>`
  )}`;
}

// Debounce function
export function debounce<T extends (...args: any[]) => any>(
  func: T,
  delay: number
): (...args: Parameters<T>) => void {
  let timeoutId: NodeJS.Timeout;
  
  return (...args: Parameters<T>) => {
    clearTimeout(timeoutId);
    timeoutId = setTimeout(() => func(...args), delay);
  };
}

// Throttle function  
export function throttle<T extends (...args: any[]) => any>(
  func: T,
  delay: number
): (...args: Parameters<T>) => void {
  let lastCall = 0;
  
  return (...args: Parameters<T>) => {
    const now = Date.now();
    if (now - lastCall >= delay) {
      lastCall = now;
      func(...args);
    }
  };
}

// Classe para merge de classNames (similar ao clsx)
export function cn(...classes: (string | undefined | null | false)[]): string {
  return classes.filter(Boolean).join(' ');
}