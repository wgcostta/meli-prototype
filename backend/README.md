0# MercadoClone Backend

Backend API para um clone do Mercado Livre desenvolvido com Java 17, Spring Boot 3.2 e seguindo os princípios SOLID e Clean Code.

## 🚀 Características

- **Arquitetura Limpa**: Implementação seguindo os princípios SOLID
- **API REST**: Endpoints RESTful bem documentados
- **Documentação**: Swagger/OpenAPI 3.0 integrado
- **Testes Abrangentes**: Unitários, integração e BDD
- **Deploy na Nuvem**: Configurado para Netlify
- **Containerização**: Docker e Docker Compose prontos
- **Qualidade de Código**: Checkstyle, SpotBugs e JaCoCo

## 🛠️ Tecnologias

- Java 17
- Spring Boot 3.2
- Spring Web
- Spring Boot Actuator
- Jackson (JSON)
- Swagger/OpenAPI 3
- JUnit 5 + Mockito
- Cucumber (BDD)
- Docker
- Maven

## 📋 Pré-requisitos

- Java 17+
- Maven 3.8+
- Docker (opcional)
- Node.js 18+ (para o frontend)

## 🚀 Instalação e Execução

### Desenvolvimento Local

1. **Clone o repositório**
```bash
git clone <repository-url>
cd mercadoclone-backend
```

2. **Execute a aplicação**
```bash
mvn spring-boot:run
```

3. **Acesse a API**
- API: http://localhost:3001
- Swagger UI: http://localhost:3001/swagger-ui.html
- Health Check: http://localhost:3001/health

### Com Docker Compose (Backend + Frontend)

1. **Clone ambos os repositórios**
```bash
git clone <backend-repo>
git clone <frontend-repo>
```

2. **Execute com Docker Compose**
```bash
docker-compose up -d
```

3. **Acesse as aplicações**
- Frontend: http://localhost:3000
- Backend: http://localhost:3001
- Nginx (produção): http://localhost

### Deploy no Netlify

1. **Configure as variáveis de ambiente no Netlify**
```env
SPRING_PROFILES_ACTIVE=prod
IMAGES_BASE_URL=https://your-app.netlify.app/images/products
```

2. **Configure o build command**
```bash
mvn clean package -DskipTests -Pprod
```

3. **Configure o publish directory**
```
target
```

## 📊 Testes

### Executar todos os testes
```bash
mvn test
```

### Testes unitários apenas
```bash
mvn test -Dtest="**/*Test"
```

### Testes de integração apenas
```bash
mvn test -Dtest="**/*IntegrationTest"
```

### Testes BDD apenas
```bash
mvn test -Dtest="CucumberTestRunner"
```

### Relatório de cobertura
```bash
mvn jacoco:report
# Relatório em: target/site/jacoco/index.html
```

## 📝 API Endpoints

### Produtos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/v1/products` | Lista todos os produtos |
| GET | `/api/v1/products/{id}` | Busca produto por ID |
| GET | `/api/v1/products/category/{categoryId}` | Produtos por categoria |
| GET | `/api/v1/products/brand/{brand}` | Produtos por marca |
| GET | `/api/v1/products/search?q={term}` | Pesquisa produtos |
| GET | `/api/v1/products/available` | Produtos disponíveis |
| GET | `/api/v1/products/discounted` | Produtos com desconto |
| GET | `/api/v1/products/price-range?minPrice={min}&maxPrice={max}` | Produtos por faixa de preço |
| GET | `/api/v1/products/{id}/exists` | Verifica se produto existe |
| GET | `/api/v1/products/count` | Conta total de produtos |

### Sistema

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/health` | Health check |
| GET | `/actuator/health` | Health check detalhado |
| GET | `/swagger-ui.html` | Documentação interativa |
| GET | `/api-docs` | OpenAPI specification |

## 🏗️ Arquitetura

```
src/
├── main/
│   ├── java/com/mercadoclone/
│   │   ├── config/           # Configurações Spring
│   │   ├── controller/       # Controllers REST
│   │   ├── domain/
│   │   │   ├── entity/       # Entidades de domínio
│   │   │   └── repository/   # Interfaces de repositório
│   │   ├── dto/              # Data Transfer Objects
│   │   ├── exception/        # Exceções customizadas
│   │   ├── infrastructure/   # Implementações de infraestrutura
│   │   └── service/          # Serviços de negócio
│   └── resources/
│       ├── data/             # Dados JSON
│       ├── static/images/    # Imagens dos produtos
│       └── application.yml   # Configurações
└── test/
├── java/
│   ├── unit/            # Testes unitários
│   ├── integration/     # Testes de integração
│   └── bdd/             # Testes BDD
└── resources/
└── features/        # Arquivos .feature (Gherkin)
```

## 🎯 Princípios SOLID Implementados

### Single Responsibility Principle (SRP)
- Controllers: apenas camada de apresentação
- Services: apenas lógica de negócio
- Repositories: apenas acesso a dados

### Open/Closed Principle (OCP)
- Uso de interfaces para extensibilidade
- Strategy pattern para diferentes persistências

### Liskov Substitution Principle (LSP)
- Implementações intercambiáveis
- Hierarquia de exceções consistente

### Interface Segregation Principle (ISP)
- Interfaces específicas e coesas
- Segregação por responsabilidade

### Dependency Inversion Principle (DIP)
- Dependência de abstrações
- Injeção de dependência

## 🔧 Configuração

### Profiles disponíveis

- **dev**: Desenvolvimento local
- **test**: Testes automatizados
- **prod**: Produção

### Variáveis de ambiente

```env
SPRING_PROFILES_ACTIVE=dev
IMAGES_BASE_URL=http://localhost:3001/images/products
PORT=3001
```

## 📈 Monitoramento

### Métricas disponíveis
- `/actuator/health` - Status da aplicação
- `/actuator/metrics` - Métricas da aplicação
- `/actuator/info` - Informações da aplicação

### Logs
- Formato JSON estruturado
- Níveis configuráveis por ambiente
- Correlação de requests

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 👥 Equipe

- **MercadoClone Team** - *Desenvolvimento inicial* - [GitHub](https://github.com/mercadoclone)

## 🔗 Links Úteis

- [Documentação Spring Boot](https://spring.io/projects/spring-boot)
- [Swagger/OpenAPI](https://swagger.io/)
- [Docker](https://www.docker.com/)
- [Netlify](https://www.netlify.com/)

---
# Makefile para automatizar comandos
```shell
.PHONY: help build test run clean docker-build docker-run

help: ## Mostra esta ajuda
@echo "Comandos disponíveis:"
@grep -E '^[a-zA-Z_-]+:.*?## .*$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $1, $2}'

build: ## Builda a aplicação
mvn clean package -DskipTests

build-prod: ## Builda para produção
mvn clean package -DskipTests -Pprod

test: ## Executa todos os testes
mvn test

test-unit: ## Executa apenas testes unitários
mvn test -Dtest="**/*Test"

test-integration: ## Executa apenas testes de integração
mvn test -Dtest="**/*IntegrationTest"

test-bdd: ## Executa apenas testes BDD
mvn test -Dtest="CucumberTestRunner"

test-coverage: ## Gera relatório de cobertura
mvn clean test jacoco:report
@echo "Relatório disponível em: target/site/jacoco/index.html"

run: ## Executa a aplicação localmente
mvn spring-boot:run

run-dev: ## Executa em modo desenvolvimento
mvn spring-boot:run -Dspring-boot.run.profiles=dev

run-prod: ## Executa em modo produção
mvn spring-boot:run -Dspring-boot.run.profiles=prod

clean: ## Limpa arquivos de build
mvn clean

docker-build: ## Constrói imagem Docker
docker build -t mercadoclone-backend .

docker-run: ## Executa container Docker
docker run -p 3001:3001 mercadoclone-backend

docker-compose-up: ## Sobe todos os serviços
docker-compose up -d

docker-compose-down: ## Para todos os serviços
docker-compose down

docker-compose-logs: ## Mostra logs dos serviços
docker-compose logs -f

quality-check: ## Executa verificações de qualidade
mvn spotbugs:check checkstyle:check

docs: ## Gera documentação
mvn javadoc:javadoc
@echo "Documentação disponível em: target/site/apidocs/index.html"

deps-update: ## Atualiza dependências
mvn versions:display-dependency-updates

deps-tree: ## Mostra árvore de dependências
mvn dependency:tree

format: ## Formata código
mvn spotless:apply

```