# Guia de Onboarding para Desenvolvedores

Seja bem-vindo ao módulo de Veículos da Trixlog! 🚀

## 1. Configurando sua IDE
Recomendamos o uso do **IntelliJ IDEA** (Community ou Ultimate) para este projeto.
- Instale os plugins: `Lombok` (se aplicável) e `Spring Boot Helper`.

## 2. Preparando o Ambiente
Você precisará de:
- **Java JDK 17** ou superior (verifique com `javac -version`).
- **PostgreSQL 14+** rodando localmente (se aplicável).
- **Maven** (o projeto já utiliza o wrapper `mvnw`, portanto basta usá-lo).

## 3. Rodando o Projeto

1. Clone o repositório.
2. Na raiz do projeto, acesse a pasta do módulo de veículo:
   ```bash
   cd veiculo
   ```
3. Suba as dependências (Banco de Dados) usando o Docker (caso configurado):
   ```bash
   docker compose up -d
   ```
4. Inicie o servidor embutido do Tomcat (Spring Boot):
   ```bash
   ./mvnw spring-boot:run
   ```

## 4. Testes
- Rode `mvn test` para executar os testes unitários.
- Antes de commitar, certifique-se que seu código não quebra regras do checkstyle ou cobertura de código exigida.

## 5. Swagger / OpenAPI
Para acessar a documentação visual da API em modo desenvolvedor (após rodar o projeto):
Abra no navegador: `http://localhost:8080/swagger-ui.html`
