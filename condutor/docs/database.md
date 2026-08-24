# Banco de Dados

## Diagrama Entidade-Relacionamento
```mermaid
erDiagram
    CONDUTORES ||--o{ CONDUTOR_RENAVAMS : possui
    
    CONDUTORES {
        varchar(11) numeroCnh PK "Identificador único"
        varchar(255) nomeCondutor "Nome do motorista"
    }

    CONDUTOR_RENAVAMS {
        varchar(11) condutor_numero_cnh FK "Referência ao Condutor"
        varchar(255) renavam "RENAVAM do veículo associado"
    }
```

## Dicionário de Dados
A tabela `condutores` armazena as raízes (aggregates). A lista de posse de veículos (`renavams`) foi desenhada usando a arquitetura de coleções nativas do Hibernate (`@ElementCollection`), gerando implicitamente a tabela `condutor_renavams`. Esse formato viabiliza que o Condutor saiba seus renavams sem carregar chaves pesadas da tabela de Veículos (desacoplamento físico).
