# Onboarding de Desenvolvedores

Bem-vindo ao microsserviço de Condutor! Para configurar seu ambiente, siga estas instruções:

## Configuração da IDE (IntelliJ / Eclipse)
1. Certifique-se de que sua IDE está rodando **Java 26**.
2. Atualize o SDK do projeto (`Project Structure > SDK`).
3. O projeto usa dependências puras e Records nativos, logo, **NÃO há suporte nem uso de Lombok**. Não é necessário instalar plugins extras.

## Rodando os Testes
A suíte de testes do domínio foi desenhada sob extrema performance, não necessitando de bancos de dados H2 ou contêineres de banco para executar, utilizando Mocks via `Mockito` e `MockMvc`.
Para testar:
```bash
./mvnw clean test
```
Verifique o report do Maven. Todos os testes devem falhar caso haja modificações em regras de validação, visto que as asserts testam os status *ProblemDetail* retornados pelo `@RestControllerAdvice`.
