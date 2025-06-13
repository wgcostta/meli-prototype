# MercadoClone - Página de Produto com API Integration

Clone da página de produto do Mercado Livre construído com Next.js 14, React e Tailwind CSS, integrado com API backend.

## 🚀 Tecnologias Utilizadas

- **Next.js 14** - Framework React com App Router
- **React 18** - Biblioteca JavaScript para interfaces
- **TypeScript** - Superset do JavaScript com tipagem estática
- **Tailwind CSS** - Framework CSS utilitário
- **Lucide React** - Biblioteca de ícones

## ✨ Funcionalidades

- **Layout Responsivo** - Funciona em desktop, tablet e mobile
- **Integração com API** - Consome dados reais do backend
- **Tratamento de Erros** - Retry automático e estados de erro elegantes
- **Loading States** - Skeleton loading e estados de carregamento
- **Galeria de Imagens** - Visualização otimizada de múltiplas imagens
- **Sistema de Avaliações** - Exibição de reviews dos usuários
- **Estados Interativos** - Estados hover, focus e transições suaves
- **Offline Support** - Detecção de conectividade e reconexão automática

## 🛠️ Instalação e Uso

### Pré-requisitos
- Node.js 18.0.0 ou superior
- npm, yarn ou pnpm
- Backend API rodando (configurar URL no .env)

### Instalação

1. Clone o repositório:
```bash
git clone <url-do-repositorio>
cd meli-prototype
```

2. Instale as dependências:
```bash
npm install
# ou
yarn install
# ou
pnpm install
```

3. Configure as variáveis de ambiente:
```bash
cp .env.example .env.local
# Edite o arquivo .env.local com suas configurações
```

4. Execute o projeto em modo de desenvolvimento:
```bash
npm run dev
# ou
yarn dev
# ou
pnpm dev
```

5. Abra [http://localhost:3000](http://localhost:3000) no seu navegador.

### Scripts Disponíveis

- `npm run dev` - Inicia o servidor de desenvolvimento
- `npm run build` - Cria a build de produção
- `npm run start` - Inicia o servidor de produção
- `npm run lint` - Executa o ESLint
- `npm run type-check` - Verifica os tipos TypeScript

## 📁 Estrutura do Projeto

```
meli-prototype/
├── app/
│   ├── components/
│   │   ├── Header.tsx
│   │   ├── Breadcrumb.tsx
│   │   ├── ProductGallery.tsx
│   │   ├── ProductInfo.tsx
│   │   ├── ProductDescription.tsx
│   │   └── LoadingAndError.tsx
│   ├── hooks/
│   │   └── useProductApi.ts
│   ├── globals.css
│   ├── layout.tsx
│   └── page.tsx
├── types/
│   └── product.ts
├── lib/
│   └── utils.ts
├── public/
├── .env.example
├── next.config.js
├── tailwind.config.js
├── tsconfig.json
└── package.json
```

## 🔌 Integração com API

### Estrutura de Resposta Esperada

A API deve retornar dados no seguinte formato:

```typescript
interface ApiResponse<Product> {
  data: {
    id: string;
    title: string;
    description: string;
    price: {
      current: number;
      original?: number;
      currency: string;
    };
    images: Array<{
      id: string;
      url: string;
      alt: string;
      order: number;
    }>;
    rating: {
      average: number;
      count: number;
      distribution: Record<number, number>;
    };
    seller: {
      id: string;
      name: string;
      reputation: number;
      location: string;
      isOfficial: boolean;
    };
    // ... outros campos
  };
  success: boolean;
  message?: string;
  timestamp: string;
}
```

### Endpoints Esperados

- `GET /api/products/:id` - Buscar produto por ID
- `GET /health` - Health check para verificar conectividade

## 🎯 Funcionalidades Implementadas

### Estados de Loading e Erro
- [x] Skeleton loading para carregamento inicial
- [x] Estados de retry automático com backoff exponencial
- [x] Detecção de conectividade e reconexão automática
- [x] Mensagens de erro amigáveis
- [x] Indicadores visuais de progresso

### Galeria de Produtos
- [x] Carrossel de imagens otimizado
- [x] Lazy loading e otimização de imagens
- [x] Fallbacks para imagens com erro
- [x] Zoom e navegação por teclado
- [x] Indicadores de posição

### Informações do Produto
- [x] Formatação de preços e moedas
- [x] Cálculo automático de descontos
- [x] Avaliações com distribuição de estrelas
- [x] Informações do vendedor
- [x] Estados de estoque e disponibilidade

### Tratamento de Erros
- [x] Retry automático (até 3 tentativas)
- [x] Backoff exponencial (2s, 4s, 8s)
- [x] Detecção de problemas de conectividade
- [x] Fallbacks graceful
- [x] Logs detalhados para debug

## ⚙️ Configuração da API

### Variáveis de Ambiente

```bash
# URL do backend
NEXT_PUBLIC_API_URL=http://localhost:3001

# Timeout para requisições (em ms)
NEXT_PUBLIC_API_TIMEOUT=10000
```

### Customização do Hook de API

O hook `useProductApi` pode ser configurado:

```typescript
// Configurações disponíveis
const MAX_RETRY_ATTEMPTS = 3;      // Número máximo de tentativas
const RETRY_DELAY = 2000;          // Delay base entre tentativas (ms)
const REQUEST_TIMEOUT = 10000;     // Timeout da requisição (ms)
```

## 🔧 Personalização

### Cores
As cores podem ser customizadas no arquivo `tailwind.config.js`:

```javascript
colors: {
  mercado: {
    yellow: '#FFE600',
    blue: {
      // Tons de azul personalizados
    }
  }
}
```

### Formatação de Preços
A formatação pode ser alterada em `lib/utils.ts`:

```typescript
export function formatPrice(price: number, currency: string = 'BRL'): string {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: currency,
  }).format(price);
}
```

## 📱 Responsividade

O projeto foi desenvolvido com abordagem mobile-first:

- **Mobile** (< 768px): Layout em coluna única, galeria otimizada
- **Tablet** (768px - 1024px): Layout adaptado com sidebar
- **Desktop** (> 1024px): Layout em grid completo

## 🚀 Deploy

### Vercel (Recomendado)
```bash
npm install -g vercel
vercel
```

### Build Manual
```bash
npm run build
npm run start
```

### Docker (Opcional)
```dockerfile
FROM node:18-alpine

WORKDIR /app

COPY package*.json ./
RUN npm ci --only=production

COPY . .
RUN npm run build

EXPOSE 3000

CMD ["npm", "start"]
```

## 🧪 Testando a Integração

### Testando sem Backend
Para testar o frontend sem o backend rodando:

1. O sistema mostrará automaticamente os estados de erro
2. Você pode ver os componentes de loading
3. O retry automático será executado
4. Estados de offline/online são simulados

### Testando com Backend Mock
Crie um servidor simples para testes:

```javascript
// mock-server.js
const express = require('express');
const cors = require('cors');
const app = express();

app.use(cors());
app.use(express.json());

// Health check
app.get('/health', (req, res) => {
  res.json({ status: 'ok' });
});

// Produto mock
app.get('/api/products/:id', (req, res) => {
  res.json({
    success: true,
    data: {
      id: req.params.id,
      title: "Produto Teste da API",
      description: "Este é um produto de teste retornado pela API mock.",
      price: {
        current: 299.99,
        original: 399.99,
        currency: "BRL"
      },
      images: [
        {
          id: "1",
          url: "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500&h=500&fit=crop",
          alt: "Produto teste",
          order: 1
        }
      ],
      rating: {
        average: 4.5,
        count: 150,
        distribution: { 5: 75, 4: 45, 3: 20, 2: 7, 1: 3 }
      },
      seller: {
        id: "seller1",
        name: "Loja Teste",
        reputation: 4.8,
        location: "São Paulo, SP",
        isOfficial: true,
        positiveRating: 98,
        yearsOnPlatform: 5
      },
      stock: {
        available: 10,
        total: 50,
        isAvailable: true
      },
      brand: "Marca Teste",
      sku: "TEST-001",
      category: {
        id: "cat1",
        name: "Eletrônicos",
        path: ["Eletrônicos", "Áudio", "Fones de Ouvido"]
      },
      features: [
        "Cancelamento de ruído",
        "Bluetooth 5.0",
        "Bateria de longa duração"
      ],
      specifications: {
        "Tipo de conexão": "Bluetooth",
        "Autonomia": "30 horas",
        "Peso": "250g"
      },
      warranty: "12 meses",
      paymentMethods: [
        {
          type: "credit_card",
          name: "Cartão de crédito",
          installments: 12
        }
      ],
      shipping: {
        free: true,
        estimatedDays: 2,
        cost: 0,
        description: "Frete grátis"
      }
    },
    timestamp: new Date().toISOString()
  });
});

app.listen(3001, () => {
  console.log('Mock server rodando na porta 3001');
});
```

Execute: `node mock-server.js`

## 🐛 Troubleshooting

### Problemas Comuns

**1. Erro de CORS**
```bash
# Verifique se o backend tem CORS configurado
# Ou use um proxy no next.config.js
```

**2. Timeout das Requisições**
```bash
# Aumente o timeout no .env
NEXT_PUBLIC_API_TIMEOUT=15000
```

**3. Imagens não Carregam**
```bash
# Adicione os domínios no next.config.js
domains: ['your-image-domain.com']
```

**4. TypeScript Errors**
```bash
# Execute a verificação de tipos
npm run type-check
```

### Debug Mode

Para ativar logs detalhados, adicione no console do navegador:
```javascript
localStorage.setItem('debug', 'true');
```

## 🔒 Considerações de Segurança

- Validação de dados da API no frontend
- Sanitização de URLs de imagens
- Timeout em todas as requisições
- Rate limiting através de debounce
- Logs sem informações sensíveis

## 📊 Performance

### Otimizações Implementadas

- **Images**: Next.js Image com lazy loading
- **Bundle Splitting**: Componentes carregados sob demanda
- **Caching**: Cache de requisições com headers apropriados
- **Prefetching**: Links importantes são prefetchados
- **Code Splitting**: Divisão automática do código

### Métricas Alvo

- **First Contentful Paint**: < 1.5s
- **Largest Contentful Paint**: < 2.5s
- **Cumulative Layout Shift**: < 0.1
- **First Input Delay**: < 100ms

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## 📝 API Contract

### Endpoints Obrigatórios

```typescript
// GET /api/products/:id
// Response: ApiResponse<Product>

// GET /health  
// Response: { status: 'ok' | 'error' }
```

### Headers Recomendados

```
Content-Type: application/json
Cache-Control: public, max-age=300
ETag: "product-version"
```

## 📄 Licença

Este projeto é apenas para fins educacionais e de demonstração.

## 👨‍💻 Autor

Desenvolvido como parte do desafio técnico MercadoLibre.

---

**Nota**: Este frontend está preparado para consumir dados de qualquer API que implemente o contrato especificado. O sistema de retry e tratamento de erros garante uma experiência robusta mesmo com problemas de conectividade.# MercadoClone - Página de Produto

Clone da página de produto do Mercado Livre construído com Next.js 14, React e Tailwind CSS.

## 🚀 Tecnologias Utilizadas

- **Next.js 14** - Framework React com App Router
- **React 18** - Biblioteca JavaScript para interfaces
- **TypeScript** - Superset do JavaScript com tipagem estática
- **Tailwind CSS** - Framework CSS utilitário
- **Lucide React** - Biblioteca de ícones

## ✨ Funcionalidades

- **Layout Responsivo** - Funciona em desktop, tablet e mobile
- **Galeria de Imagens** - Visualização de múltiplas imagens do produto
- **Seleção de Variações** - Cores, quantidade e outras opções
- **Sistema de Avaliações** - Exibição de reviews dos usuários
- **Informações Detalhadas** - Especificações, descrição e FAQ
- **Interface Interativa** - Estados hover, focus e transições suaves

## 🛠️ Instalação e Uso

### Pré-requisitos
- Node.js 18.0.0 ou superior
- npm, yarn ou pnpm

### Instalação

1. Clone o repositório:
```bash
git clone <url-do-repositorio>
cd meli-prototype
```

2. Instale as dependências:
```bash
npm install
# ou
yarn install
# ou
pnpm install
```

3. Execute o projeto em modo de desenvolvimento:
```bash
npm run dev
# ou
yarn dev
# ou
pnpm dev
```

4. Abra [http://localhost:3000](http://localhost:3000) no seu navegador.

### Scripts Disponíveis

- `npm run dev` - Inicia o servidor de desenvolvimento
- `npm run build` - Cria a build de produção
- `npm run start` - Inicia o servidor de produção
- `npm run lint` - Executa o ESLint
- `npm run type-check` - Verifica os tipos TypeScript

## 📁 Estrutura do Projeto

```
meli-prototype/
├── app/
│   ├── components/
│   │   ├── Header.tsx
│   │   ├── Breadcrumb.tsx
│   │   ├── ProductGallery.tsx
│   │   ├── ProductInfo.tsx
│   │   └── ProductDescription.tsx
│   ├── globals.css
│   ├── layout.tsx
│   └── page.tsx
├── public/
├── next.config.js
├── tailwind.config.js
├── tsconfig.json
└── package.json
```

## 🎨 Componentes Principais

### Header
- Logo da aplicação
- Barra de pesquisa
- Links de navegação (Conta e Carrinho)

### ProductGallery
- Carrossel de imagens
- Miniaturas clicáveis
- Controles de navegação
- Indicador de posição

### ProductInfo
- Título e descrição do produto
- Sistema de avaliações com estrelas
- Preços e descontos
- Seleção de variações (cor, quantidade)
- Botões de ação (Comprar/Adicionar ao carrinho)
- Informações de entrega e garantias
- Dados do vendedor

### ProductDescription
- Tabs navegáveis (Descrição, Especificações, Avaliações, Perguntas)
- Descrição expandível
- Lista de especificações técnicas
- Sistema de reviews
- FAQ interativo

## 🎯 Funcionalidades Implementadas

- [x] Layout responsivo
- [x] Galeria de imagens interativa
- [x] Seleção de variações
- [x] Sistema de quantidade
- [x] Favoritos (coração)
- [x] Compartilhamento
- [x] Tabs de informações
- [x] Reviews e avaliações
- [x] Perguntas e respostas
- [x] Informações de entrega
- [x] Dados do vendedor
- [x] Animações e transições
- [x] Estados de hover e focus
- [x] Acessibilidade básica

## 🔧 Personalização

### Cores
As cores podem ser customizadas no arquivo `tailwind.config.js`:

```javascript
colors: {
  mercado: {
    yellow: '#FFE600',
    blue: {
      // Tons de azul personalizados
    }
  }
}
```

### Tipografia
A fonte padrão pode ser alterada no `tailwind.config.js`:

```javascript
fontFamily: {
  'sans': ['Inter', 'system-ui', 'sans-serif'],
}
```

## 📱 Responsividade

O projeto foi desenvolvido com abordagem mobile-first:

- **Mobile** (< 768px): Layout em coluna única
- **Tablet** (768px - 1024px): Layout adaptado
- **Desktop** (> 1024px): Layout em grid com sidebar

## 🚀 Deploy

### Vercel (Recomendado)
```bash
npm install -g vercel
vercel
```

### Build Manual
```bash
npm run build
npm run start
```

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto é apenas para fins educacionais e de demonstração.

## 👨‍💻 Autor

Desenvolvido como parte de um desafio de clonagem de interface.