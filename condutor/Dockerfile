# Etapa 1: Build (Compilação) usando o Maven Wrapper
FROM amazoncorretto:26 AS build
WORKDIR /app

# Instalação do tar e gzip para o maven wrapper
RUN yum install -y tar gzip || dnf install -y tar gzip

# Copia os arquivos do maven wrapper
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Torna o script executável e baixa as dependências (go-offline)
RUN chmod +x mvnw
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

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
