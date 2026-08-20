# Etapa 1: Build (Compilação)
FROM amazoncorretto:26 AS build
WORKDIR /app

# Copia os arquivos do maven wrapper e de configuração para baixar as dependências
RUN dnf install -y tar gzip
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw
# Baixa as dependências offline para otimizar cache
RUN ./mvnw dependency:go-offline -B

# Copia o código fonte e compila a aplicação
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Etapa 2: Run (Execução)
FROM amazoncorretto:26-alpine
WORKDIR /app

# Cria um usuário não-root para rodar a aplicação com mais segurança
RUN addgroup -S javauser && adduser -S javauser -G javauser
USER javauser:javauser

# Copia o .jar gerado da etapa de build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
