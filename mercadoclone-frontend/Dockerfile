# Dockerfile otimizado para mercadoclone-frontend/Dockerfile
# Etapa 1: Build
FROM node:18-alpine AS builder

WORKDIR /app

# Copiar arquivos de dependências
COPY package.json package-lock.json* ./

# Instalar todas as dependências (incluindo devDependencies)
RUN npm ci

# Copiar código fonte
COPY . .

# Build da aplicação
RUN npm run build

# Etapa 2: Runtime (para produção)
FROM node:18-alpine AS production

WORKDIR /app

# Copiar package.json e instalar apenas dependências de produção
COPY package.json package-lock.json* ./
RUN npm ci --only=production && npm cache clean --force

# Copiar build da etapa anterior
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/public ./public

# Expor porta
EXPOSE 3000

# Comando para produção
CMD ["npm", "start"]

# Etapa 3: Desenvolvimento
FROM node:18-alpine AS development

WORKDIR /app

# Copiar arquivos de dependências
COPY package.json package-lock.json* ./

# Instalar todas as dependências
RUN npm ci

# Copiar código fonte
COPY . .

# Expor porta
EXPOSE 3000

# Comando para desenvolvimento
CMD ["npm", "run", "dev"]