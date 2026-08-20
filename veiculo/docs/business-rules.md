# Regras de Negócio - Veículos

Este documento compila as regras aplicadas durante as operações com veículos na API.

## 1. Cadastro de Veículo
- **Regex Renavam**: O renavam deve obrigatoriamente possuir exatos 11 dígitos numéricos (`(?=.*\d).{11}`).
- **Regex Placa**: A placa deve seguir o modelo antigo (ABC1234) ou modelo Mercosul (ABC1D23) utilizando a validação: `[A-Z]{3}\d[A-Z]\d{2}|[A-Z]{3}\d{4}`.
- **Unicidade**: O sistema deve rejeitar o cadastro de um veículo caso já exista outro com o mesmo `renavam` ou `placa`. Um `IllegalArgumentException` é lançado pelo serviço nesses casos.
- **Validação de Estado (UF)**: A UF informada na placa deve existir no Enum oficial da aplicação (ex: CE, SP, RJ). Caso contrário, um erro de negócio é disparado antes de chegar ao banco.

## 2. Alocação de Condutor
- A coluna `cnhCondutor` serve para rastrear quem é o condutor ativo no veículo.
- Não é possível alocar um condutor se ele já estiver alocado em outro veículo ativo ou se o veículo desejado já estiver em uso por outra pessoa.

## 3. Fluxo de Busca por Data de Aquisição
O usuário pode filtrar veículos por data de aquisição. A data informada no DTO (padrão `dd-MM-yyyy`) é convertida para `LocalDate` do Java e consultada no PostgreSQL através de queries SQL `BETWEEN`.
