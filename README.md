# 🚛💨 Desafio Trixlog - Ecossistema de Microsserviços

**Autor:** Edclydson Sousa (Refatorado com Padrões Arquiteturais Modernos)

## 📖 Resumo
Este projeto é a resolução do desafio técnico proposto pela equipe da Trixlog, evoluído de uma aplicação monolítica legada para um **ecossistema de microsserviços isolados**.

A solução fornece APIs REST robustas para:
- C.R.U.D completo e independente para Condutores e Veículos.
- Desacoplamento físico: O Condutor possui uma lista de veículos apenas pelo RENAVAM, mantendo a consistência distribuída e evitando amarração forte (Foreign Keys cruzadas).
- Buscas dinâmicas (Consulta de Condutor por Nome, Veículos por Renavam, Placa, Estado da Placa, e filtragem por Data de Aquisição).

## 🛠️ Stack Tecnológica
- **Java 26**
- **Spring Boot 3.4.0**
- **PostgreSQL 15**
- **Docker & Docker Compose** (Implantação Multi-stage)
- **Documentação de API:** Springdoc OpenAPI (Swagger UI)
- **Testes:** JUnit 5 + Mockito / MockMvc

## 📂 Estrutura do Projeto

O projeto foi estrangulado em dois microsserviços autônomos, cada um possuindo sua própria base de dados e domínio de responsabilidades:

### 1. Módulo `condutor/`
Gerencia exclusivamente os **motoristas** e a tabela de posses de RENAVAMs.
- **Arquitetura:** Clean Architecture com DTOs (Records), Mappers, Tratamento Global de Exceções (`@RestControllerAdvice` com RFC 7807 ProblemDetail), e otimizações JPA agressivas (`@EntityGraph` para contornar problemas N+1).
- **Porta Host:** 8081 (API) / 5432 (PostgreSQL Local)
- **Execução:** Suporta inicialização isolada via Docker Compose na respectiva pasta.
- **Docs:** Leia a [Documentação de Condutores](./condutor/README.md) e as cartilhas de negócio na subpasta `condutor/docs/`.

### 2. Módulo `veiculo/`
Gerencia exclusivamente o catálogo da frota de **veículos**.
- **Arquitetura:** Microsserviço independente seguindo as mesmas diretrizes modernas implementadas no módulo de condutores.
- **Porta Host:** 8080 (API) / 5433 (PostgreSQL Local)
- **Docs:** Leia a [Documentação de Arquitetura de Veículos](./veiculo/README.md) para mais detalhes sobre os padrões arquiteturais, uso de DTOs, Mappers, Records e Jakarta.

## 🚀 Como Rodar o Ecossistema

Ambos os projetos estão 100% conteinerizados com Docker.

Para subir a infraestrutura completa, você deve entrar no diretório de cada microsserviço e subir a sua composição:

```bash
# Subindo a API de Veículos (Porta 8080)
cd veiculo
docker compose up -d --build
cd ..

# Subindo a API de Condutores (Porta 8081)
cd condutor
docker compose up -d --build
cd ..
```

### Acessando os Swaggers UI
Uma vez no ar, as especificações OpenAPI estarão disponíveis em:
- **API Veículo:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **API Condutor:** [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
