# Arquitetura do Microsserviço

## Diagrama C4 (Container)
```mermaid
C4Context
    title Diagrama de Componentes - Microsserviço Condutor

    Person(admin, "Administrador", "Gerencia os condutores da frota")
    
    System_Boundary(c1, "Ecossistema Trixlog") {
        Container(condutor_api, "Condutor API", "Java/Spring Boot", "Gerencia os condutores e seus vínculos com veículos (via renavam). Roda na porta 8081.")
        ContainerDb(condutor_db, "Condutor DB", "PostgreSQL", "Armazena as informações e CNHs dos condutores")
        Container(veiculo_api, "Veículo API", "Java/Spring Boot", "Microsserviço independente (porta 8080) que gerencia frota.")
    }

    Rel(admin, condutor_api, "Gerencia condutores e vínculos", "HTTPS/REST")
    Rel(condutor_api, condutor_db, "Lê/Escreve dados", "JDBC/Hibernate")
    Rel(veiculo_api, condutor_api, "Sincroniza posse (idealmente via Gateway/Broker)", "REST/Assíncrono")
```

## Separação de Camadas
- **Controller**: Expõe os endpoints e realiza *Bean Validation*. Converte retornos para *ResponseEntity* e intercepta exceções com `@RestControllerAdvice`.
- **Service**: Concentra regras de negócio puras, utilizando limites transacionais (`@Transactional`) para gerenciar as mutações no banco de forma otimizada.
- **Repository**: Camada Spring Data JPA com uso agressivo de `@EntityGraph` para resolver gargalos N+1 nas coleções LAZY.
- **DTO**: Padrão *Record* nativo do Java garantindo isolamento da infraestrutura de Banco de Dados da camada web.
