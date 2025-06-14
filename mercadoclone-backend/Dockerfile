# Dockerfile para mercadoclone-backend/Dockerfile
FROM maven:3.9.4-eclipse-temurin-17-alpine

WORKDIR /app

# Copiar pom.xml e baixar dependências primeiro (cache layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar código fonte
COPY src ./src

# Compilar aplicação
RUN mvn clean package -DskipTests

# Criar diretório para imagens e copiar arquivos estáticos
RUN mkdir -p /app/images
COPY src/main/resources/static/images/ /app/images/

# Expor porta
EXPOSE 3001

# Comando de inicialização
CMD ["sh", "-c", "java -jar target/*.jar"]