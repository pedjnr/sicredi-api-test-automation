# Sicredi API Test Automation

![Java](https://img.shields.io/badge/Java-11-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![JUnit 5](https://img.shields.io/badge/JUnit%205-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![RestAssured](https://img.shields.io/badge/RestAssured-API%20Testing-5B47A5?style=for-the-badge)
![Allure](https://img.shields.io/badge/Allure-Reports-FF6A4A?style=for-the-badge)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)

Projeto de automação de testes de API desenvolvido em Java com RestAssured, JUnit 5, validação de contrato via JSON Schema e relatórios Allure.

O repositório inclui a API do desafio técnico Sicredi usada como aplicação sob teste e uma suíte automatizada para validar endpoints de restrições e simulações de crédito. A estrutura foi organizada para portfólio, com documentação, execução local, Docker, relatórios Allure e pipeline CI/CD.

## Links Principais

- [Relatório Allure publicado no GitHub Pages](https://pedjnr.github.io/sicredi-api-test-automation/)
- [Matriz de Casos de Teste](documentacao_planejamento_testes/relatorios/matriz_casos_teste_api_sicredi.xlsx)
- [Relatório de Bugs](documentacao_planejamento_testes/relatorios/relatorio_bugs_api_sicredi.xlsx)

## Escopo Dos Testes

- Validação dos endpoints `GET /restricoes/{cpf}` e `/simulacoes`.
- Cenários positivos e negativos para criação, consulta, alteração e remoção de simulações.
- Validação de status code, payload, mensagens de erro e regras de negócio.
- Validação de contrato com JSON Schema.
- Massa dinâmica de dados para reduzir dependência entre execuções.
- Relatórios Allure com evidências de execução.

## Estrutura

```txt
.
|-- .github/workflows/api-tests.yml
|-- documentacao_planejamento_testes/
|   |-- Plano de Testes.md
|   |-- images/
|   |-- mapa_mental/
|   `-- relatorios/
|-- sicredi_API/desafio-sicredi-master/
|   |-- readme.md
|   |-- Orientações para execução do Desafio_v1.pdf
|   `-- prova-tecnica-api/
|-- testes_automatizados/desafiosicredi/
|   `-- suíte RestAssured/JUnit 5
|-- docker-compose.yaml
`-- README.md
```

## Pré-Requisitos

- Java JDK 11 ou superior.
- Maven configurado no `PATH`.
- Docker Desktop, caso queira rodar via container.

## Executando Localmente

Em um terminal, suba a API:

```bash
cd sicredi_API/desafio-sicredi-master/prova-tecnica-api
mvn clean spring-boot:run
```

A API ficará disponível em:

```txt
http://localhost:8080
```

Swagger:

```txt
http://localhost:8080/swagger-ui.html
```

Em outro terminal, rode os testes:

```bash
cd testes_automatizados/desafiosicredi
mvn clean test -Denv=local
```

## Relatórios Allure

Após executar os testes:

```bash
cd testes_automatizados/desafiosicredi
mvn allure:report
mvn allure:serve
```

Os resultados gerados ficam em `testes_automatizados/desafiosicredi/target/`.

No pipeline CI/CD, o relatório Allure é gerado mesmo quando existem testes falhando, preservando as evidências da execução. O relatório publicado pode ser acessado pelos links principais no início deste README.

## Artefatos De Qualidade

- [Matriz de Casos de Teste](documentacao_planejamento_testes/relatorios/matriz_casos_teste_api_sicredi.xlsx)
- [Relatório de Bugs](documentacao_planejamento_testes/relatorios/relatorio_bugs_api_sicredi.xlsx)

## Executando Com Docker

Na raiz do projeto:

```bash
docker compose up --build --abort-on-container-exit --exit-code-from testes
```

Depois da execução:

```bash
docker compose down
```

## Pipeline

O workflow em `.github/workflows/api-tests.yml` executa:

- Checkout do repositório.
- Configuração do Java 11.
- Inicialização da API Spring Boot.
- Espera ativa pelo health check da API.
- Execução da suíte RestAssured.
- Geração de relatório Allure.
- Upload dos artefatos de teste.
- Publicação do relatório Allure no GitHub Pages.

Caso existam testes falhando, o workflow mantém o status final de falha para refletir os defeitos encontrados, mas ainda publica o relatório e os artefatos para análise.

## Documentação

- [Plano de Testes](documentacao_planejamento_testes/Plano%20de%20Testes.md)
- [Matriz de Casos de Teste](documentacao_planejamento_testes/relatorios/matriz_casos_teste_api_sicredi.xlsx)
- [Relatório de Bugs](documentacao_planejamento_testes/relatorios/relatorio_bugs_api_sicredi.xlsx)
- [Regras da API](sicredi_API/desafio-sicredi-master/readme.md)
- [Orientações do desafio](sicredi_API/desafio-sicredi-master/Orienta%C3%A7%C3%B5es%20para%20execu%C3%A7%C3%A3o%20do%20Desafio_v1.pdf)
- [Mapa mental](documentacao_planejamento_testes/mapa_mental/%5BAPI%5D%20SICRED.xmind)

![API Spring Boot em execução](documentacao_planejamento_testes/images/ReadMe/aplicação%20spring-boot%20rodando.png)
