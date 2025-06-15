# Mercado Clone - Guia de Execução

Este guia explica como executar a aplicação completa usando Docker ou individualmente.

## 📋 Pré-requisitos

- Docker e Docker Compose instalados
- Node.js 18+ (para execução individual do frontend)
- Java 17+ e Maven (para execução individual do backend)

## 🚀 Execução Completa com Docker Compose

### Executar toda a aplicação
```bash
# Subir todos os serviços
docker-compose up

# Visualizar logs em tempo real
docker-compose logs -f

# Visualizar logs de um serviço específico
docker-compose logs -f backend
docker-compose logs -f frontend
```

### Reconstruir após mudanças
```bash
# Parar e reconstruir
docker-compose down
docker-compose up --build -d
```

### Parar aplicação
```bash
# Parar serviços
docker-compose down

# Parar e remover volumes
docker-compose down -v
```

## 🔧 Execução Individual

### Backend (Spring Boot)
```bash
cd mercadoclone-backend

# Com Maven
mvn spring-boot:run

# Ou com Maven Wrapper
./mvnw spring-boot:run

# Ou via Docker individual
docker build -t backend .
docker run -p 3001:3001 backend
```

### Frontend (Next.js)
```bash
cd mercadoclone-frontend

# Instalar dependências
npm install

# Executar em modo desenvolvimento
npm run dev

# Ou via Docker individual
docker build -t frontend .
docker run -p 3000:3000 frontend
```

## 🧪 Executar Testes

### Testes do Backend
```bash
cd mercadoclone-backend

# Executar todos os testes
mvn test

# Ou com Maven Wrapper
./mvnw test

# Executar testes específicos
mvn test -Dtest=NomeDoTeste

# Executar com relatório de cobertura
mvn test jacoco:report
```

### Testes via Docker
```bash
# Executar testes dentro do container
docker-compose exec backend mvn test

# Ou construir uma imagem específica para testes
docker build -f Dockerfile.test -t backend-test .
docker run --rm backend-test
```

## 🌐 Acessar Aplicação

Após executar com sucesso:

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:3001
- **Health Check**: http://localhost:3001/actuator/health (se configurado)

## 🔍 Comandos Úteis para Debug

### Ver containers em execução
```bash
docker ps
```

### Acessar shell do container
```bash
# Backend
docker-compose exec backend sh

# Frontend  
docker-compose exec frontend sh
```

### Limpar Docker (se necessário)
```bash
# Remover containers parados
docker container prune

# Remover imagens não utilizadas
docker image prune

# Limpeza completa (cuidado!)
docker system prune -a
```

### Verificar logs de erro
```bash
# Logs detalhados
docker-compose logs --tail=50 backend
docker-compose logs --tail=50 frontend

# Seguir logs em tempo real
docker-compose logs -f --tail=10
```

## ⚡ Comandos Rápidos

```bash
# Setup completo
git clone <repo>
cd <projeto>
docker-compose up --build -d

# Verificar status
docker-compose ps

# Ver logs
docker-compose logs -f

# Parar tudo
docker-compose down
```

## 🐛 Troubleshooting

### Frontend não conecta com Backend
- Verificar se ambos containers estão na mesma rede
- Conferir variável `REACT_APP_API_URL=http://backend:3001`

### Erro de build no Backend
- Verificar se `pom.xml` está correto
- Limpar cache: `docker system prune`

### Erro de dependências no Frontend
- Verificar se `package.json` tem todas as dependências
- Limpar `node_modules`: `docker-compose down -v`

### Portas já em uso
```bash
# Verificar processos usando as portas
lsof -i :3000
lsof -i :3001

# Parar processos se necessário
kill -9 <PID>
```