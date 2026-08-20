# 🚗 Serviço de Veículos (Vehicle Service)

## 📌 Visão Geral
Este módulo é responsável por todo o gerenciamento de Veículos dentro da API Trixlog. Ele fornece as funcionalidades necessárias para cadastrar, consultar, atualizar e excluir veículos, além de manter a relação de posse com os condutores.

## 🛠️ Stack & Versões
- **Java:** JDK 17
- **Framework:** Spring Boot 3.x
- **Banco de Dados:** PostgreSQL 14+
- **JPA/ORM:** Spring Data JPA
- **Documentação de API:** Springdoc OpenAPI (Swagger 3)

## 🚀 Como Executar Localmente

### Pré-requisitos
- Ter o **JDK 17+** instalado.
- Ter o **Docker** e **Docker Compose** (opcional, para rodar o banco de dados via container).
- Variáveis de ambiente devem estar configuradas no seu `application.yml` (perfis como `dev` e `prod`).

### Passo a Passo
1. No diretório raiz do módulo, inicialize os serviços de apoio (ex: banco de dados) com:
   ```bash
   docker compose up -d
   ```
2. Inicie a aplicação com o Maven Wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```

## 🔗 Links Úteis
- **Swagger UI (Documentação Interativa da API):** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Actuator Health (Saúde da Aplicação):** [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
- **Documentação Arquitetural e de Banco:** Veja a [pasta /docs](./docs) para guias de Onboarding, diagramas C4 e de Entidade-Relacionamento.

---

## 📋 Atributos do Veículo
Cada veículo no sistema possui os seguintes dados:
- **Renavam:** (Chave Primária) - Exclusivo de cada veículo, com exatos 11 dígitos.
- **Placa:** Identificador único do veículo (Ex: ABC1234 ou ABC1D23).
- **Chassi:** Identificador único de fábrica com 17 caracteres.
- **Ano de Fabricação / Ano do Modelo:** (Ex: 2023).
- **Cor:** Cor predominante do veículo.
- **UF da Placa:** Estado de origem da placa (Ex: SP, RJ).
- **Data de Aquisição:** Data em que o veículo foi adquirido.
- **CNH do Condutor:** Quando o veículo está alocado, armazena a CNH do condutor responsável.

---

## 🔒 Regras de Negócio e Validações
O serviço implementa regras rígidas para garantir a integridade da frota:
1. **Formatos Restritos:** Placas e Renavam são validados via Regex para garantir conformidade nacional.
2. **Unicidade:** Não é possível cadastrar dois veículos com o mesmo Renavam ou Placa.
3. **Validação de UF:** A UF informada deve ser um estado brasileiro válido.
4. **Vínculo com Condutor:** Um veículo só pode ser atribuído a um condutor se ele já estiver cadastrado e não estiver em uso por outra pessoa no momento.
*Ver [docs/business-rules.md](./docs/business-rules.md) para mais detalhes.*

---

## 🛡️ Tratamento de Erros e Arquitetura
- **Isolamento de Entidade**: A API nunca expõe a entidade JPA do banco de dados na web. Todas as saídas utilizam um DTO enxuto (`VeiculoResponse`), blindando a aplicação contra vazamentos de estrutura do banco.
- **ProblemDetail (RFC 7807)**: Exceções de negócio (como Renavam duplicado ou UF inválida) são capturadas globalmente por um `@RestControllerAdvice` e formatadas no padrão JSON `ProblemDetail` com código HTTP `400 Bad Request`.
- **Identificadores (UUID)**: O veículo adota UUID versão 4 gerado automaticamente pelo JPA para sua chave primária interna (`veiculo_id`).

---

## 📡 Endpoints Disponíveis
A API expõe os seguintes recursos na rota base `/veiculos`:

### 📝 C.R.U.D. Básico
- `POST /veiculos/cadastraveiculo` - Registra um novo veículo no banco de dados.
- `GET /veiculos` - Lista todos os veículos cadastrados.
- `GET /veiculos/{renavam}` - Busca os detalhes de um veículo específico pelo Renavam.
- `PUT /veiculos/alteraveiculo` - Atualiza os dados de um veículo existente.
- `DELETE /veiculos/deletaveiculo/{renavam}` - Remove um veículo do sistema.

### 🔍 Consultas Avançadas e Filtros
- `GET /veiculos/buscaplaca/{placa}` - Encontra um veículo específico utilizando sua placa.
- `GET /veiculos/placa/{uf}` - Retorna uma lista de veículos agrupados por estado (UF da Placa).
- `GET /veiculos/intervaloaquisicao/{datainicio}/{datafim}` - Filtra e retorna veículos adquiridos dentro de um intervalo de datas específico.
