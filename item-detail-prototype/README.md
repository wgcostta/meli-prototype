# Item Detail Prototype - Mercado Livre

## Design Choices
- **Framework**: Next.js foi escolhido por sua simplicidade, SSR/SSG capabilities, e suporte a TypeScript.
- **Estilização**: Tailwind CSS para estilização responsiva e rápida.
- **Mock Data**: Dados mockados localmente para simular a API.

## Challenges & Solutions
- **Responsividade**: Utilizei Tailwind para garantir uma UI adaptável a diferentes tamanhos de tela.
- **Simulação de API**: Implementei um endpoint mock no Next.js para testes iniciais.

## How to Run
1. Instale as dependências: `npm install`
2. Rode o projeto: `npm run dev`
3. Acesse: `http://localhost:3000`


# MercadoClone - Página de Produto

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