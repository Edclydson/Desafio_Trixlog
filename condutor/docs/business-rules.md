# Regras de Negócio

## Validações de Criação/Alteração
- **CNH**: Deve conter exatos 11 dígitos numéricos. Qualquer outra formatação bloqueia a inserção via erro `400 Bad Request`. (Garantido pelo `jakarta.validation`).
- **Nomes**: Devem possuir pelo menos duas letras, sem números.

## Regras de Vínculo de Posse (Veículo)
- **Adquirir Veículo**: Um condutor só pode assumir a posse de um veículo se a sua CNH existir e for válida. A operação embute o RENAVAM do veículo diretamente na sua coleção local (`renavams`).
- **Liberar Veículo**: O condutor obrigatoriamente já deve possuir o RENAVAM registrado em sua lista para conseguir liberá-lo.
- **Orquestração**: Na vida real, a liberação/aquisição precisa acontecer de forma bidirecional (microsserviço de veículo e microsserviço de condutor). Atualmente, as APIs são chamadas isoladamente pelos orquestradores.

## Tratamento de Erros
Implementa a **RFC 7807 (ProblemDetail)**:
- **400 Bad Request**: Validações de negócio (CNH inválida, formato errado) e quebra de regras (ex: Liberar veículo que não possui).
- **404 Not Found**: Ações realizadas contra CNHs que não existem na base.
