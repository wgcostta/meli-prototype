# Dockerfile para Backend
FROM openjdk:17-jdk-slim as builder

WORKDIR /app

  # Copia arquivos de dependências
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

  # Baixa dependências
RUN ./mvnw dependency:go-offline -B

  # Copia código fonte
COPY src src

  # Builda aplicação
RUN ./mvnw clean package -DskipTests

  # Estágio de runtime
FROM openjdk:17-jre-slim

WORKDIR /app

  # Instala curl para healthcheck
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

  # Copia JAR da aplicação
COPY --from=builder /app/target/*.jar app.jar

  # Cria diretório para imagens
RUN mkdir -p /app/images

  # Copia imagens estáticas
COPY src/main/resources/static/images/ /app/images/

  # Expõe porta
EXPOSE 3001

  # Variáveis de ambiente
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=docker

  # Comando de inicialização
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

  # Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
CMD curl -f http://localhost:3001/health || exit 1