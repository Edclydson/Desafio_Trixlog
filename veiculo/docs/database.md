# Modelagem do Banco de Dados

O serviço de veículos utiliza o banco de dados PostgreSQL.

## Diagrama Entidade-Relacionamento

```mermaid
erDiagram
    VEICULO {
        UUID veiculo_id PK
        String renavam_veiculo "UK - 11 caracteres"
        String placa "UK - 7 caracteres"
        String chassi "17 caracteres"
        String ano_modelo "4 caracteres"
        String ano_fabricacao "4 caracteres"
        String cor "20 caracteres"
        String uf_placa "2 caracteres"
        Date data_aquisicao
        String cnh_condutor "FK (lógica)"
    }
```

## Dicionário de Dados

A tabela principal do módulo é a `veiculo`. Abaixo está a definição de algumas colunas cruciais:

- **veiculo_id**: Chave primária utilizando UUID para maior segurança e distribuição.
- **renavam_veiculo**: O Renavam deve ser único e conter 11 dígitos numéricos, validado pela aplicação.
- **placa**: Única, validada conforme padrões brasileiros (Mercosul ou antigo).
- **cnh_condutor**: Armazena a referência para o condutor caso o veículo esteja alocado a alguém. Se `null`, significa que o veículo está livre na frota.
