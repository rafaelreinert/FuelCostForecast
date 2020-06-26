# FuelCostForecast

[![<ORG_NAME>](https://circleci.com/gh/rafaelreinert/FuelCostForecast.svg?style=svg)](<https://app.circleci.com/pipelines/github/rafaelreinert/FuelCostForecast/>)

Projeto para empresa ficticia Ficticius Clean.


## Instruções para Execução

Este projeto esta configurando com maven,docker-compose e make, para executar o sistema basta usar as opcoes abaixo:

## make

- `make up` - Executa o `mvn clean install` sem testes. controi a imagem docker e executa um docker compose com a imagem e o banco postgres
- `make test` - Executa o `mvn test`

## maven + Docker compose

- `mvn clean install` - Executa o build e controi o jar do projeto no target
- `docker-compose build` - Executa o build da imagem docker usando o jar criado no target
- `docker-compose up` - Inicia a imagem junto com o postgresql.
- `mvn tests` - Executa o testes unitarios e integração


## Definição da API

O projeto esta com o swagger-ui ativado, para verificar a API basta entrar na URL localhost:8080/swagger-ui.html apos a inicializacao do sistema. 


# Importante!

Foi utilizado o loombok no projeto, antes de importar o projeto em alguma IDE verificar se o plugin do loombok esta instalado https://projectlombok.org


http://localhost:8080/webjars/swagger-ui/index.html
