FROM openjdk:17-jdk-slim

WORKDIR /app

# Copia e instala dependências
COPY pom.xml .
RUN ./mvnw dependency:go-offline -B

# Copia código e builda aplicação
COPY src src
RUN ./mvnw clean package -DskipTests

# Cria diretório para imagens e copia arquivos estáticos
RUN mkdir -p /app/images
COPY src/main/resources/static/images/ /app/images/

# Expõe porta
EXPOSE 3001

# Comando de inicialização
CMD ["sh", "-c", "java -jar target/*.jar"]