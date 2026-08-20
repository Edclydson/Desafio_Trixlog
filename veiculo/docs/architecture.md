# Arquitetura do Serviço de Veículos

Este documento descreve a arquitetura do módulo de gerenciamento de veículos.

## Diagrama de Componentes (C4 Model)

```mermaid
C4Context
    title Diagrama de Componente - Serviço de Veículos

    Person(admin, "Administrador de Frota", "Gerencia os veículos da frota")
    System_Boundary(veiculo_module, "Módulo de Veículos") {
        Container(controller, "VeiculoController", "Spring MVC REST Controller", "Recebe as requisições e retorna DTOs (VeiculoResponse)")
        Container(exception_handler, "VeiculoExceptionHandler", "@RestControllerAdvice", "Captura exceções e formata em RFC 7807 (ProblemDetail)")
        Container(mapper, "VeiculoMapper", "MapStruct", "Converte DTOs de Request para Entidade e Entidade para VeiculoResponse")
        Container(service, "VeiculoService", "Spring Service", "Contém a lógica de negócio e as validações")
        Container(repository, "VeiculoRepository", "Spring Data JPA", "Interface para comunicação com o banco de dados")
    }
    SystemDb(database, "PostgreSQL", "Armazena as informações dos veículos")

    Rel(admin, controller, "Realiza chamadas REST", "JSON/HTTPS")
    Rel(controller, exception_handler, "Dispara exceções não tratadas")
    Rel(controller, mapper, "Usa para converter resposta")
    Rel(controller, service, "Delega requisições usando Request DTOs")
    Rel(service, repository, "Executa operações de persistência")
    Rel(repository, database, "Lê/Escreve dados", "JDBC/SQL")
```

## Fluxo de Dados

1. O cliente HTTP envia um payload JSON para o `VeiculoController`.
2. O `VeiculoController` utiliza Bean Validation (`@Valid` em `NovoVeiculo` DTO). Falhas de validação caem no `VeiculoExceptionHandler` (retornando HTTP 400).
3. O controller envia o DTO para o `VeiculoService`.
4. O `VeiculoService` aplica as regras de negócio pesadas. Se falhar, lança um `IllegalArgumentException` que também é traduzido pelo `VeiculoExceptionHandler`.
5. Com a aprovação do Service, o `VeiculoRepository` salva no `PostgreSQL`.
6. Na volta (GETs ou retornos de busca), o `VeiculoController` passa a Entidade JPA pelo `VeiculoMapper`, devolvendo ao cliente um `VeiculoResponse` (DTO enxuto).
