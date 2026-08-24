# Microsserviço de Condutor

## Visão Geral
O **Microsserviço de Condutor** gerencia o domínio de motoristas (condutores), mantendo as informações pessoais, dados de CNH e a relação simplificada dos veículos atrelados (via lista de RENAVAMs). Ele atua como um sistema independente, provendo as regras de posse de veículos para o ecossistema e expondo funcionalidades através de uma API REST padronizada.

## Stack & Versões
- **Java**: 26
- **Spring Boot**: 3.4.0
- **Banco de Dados**: PostgreSQL (Porta 5432 / container)
- **Documentação de API**: Swagger/OpenAPI (Springdoc)
- **Gestão de Dependências**: Maven

## Como Executar Localmente
### Pré-requisitos
- JDK 26 configurado (`JAVA_HOME`).
- Instância do PostgreSQL rodando na porta 5432 com o banco de dados `condutores` (ou conforme `application.properties`).
- Maven instalado.

### Inicialização
Para compilar e inicializar o serviço localmente:
```bash
./mvnw clean install
./mvnw spring-boot:run
```
O serviço será iniciado na porta **8081** (para não conflitar com o microsserviço de Veículos).

## Links Úteis
- **Swagger UI**: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs)
