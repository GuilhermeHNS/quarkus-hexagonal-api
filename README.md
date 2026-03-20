# Desafio Técnico

## Descrição
API REST desenvolvida em Java com Quarkus para gerenciamento de clientes, produtos e vendas, incluindo os relatórios obrigatórios definidos no desafio técnico.

## Tecnologias utilizadas
- Java 21
- Quarkus
- MongoDB
- JUnit 5
- Mockito
- Rest Assured
- OpenAPI / Swagger

## Funcionalidades implementadas

### Clientes
- Criar cliente
- Listar clientes
- Buscar cliente por ID
- Atualizar cliente
- Deletar cliente

### Produtos
- Criar produto
- Listar produtos
- Buscar produto por ID
- Atualizar produto
- Deletar produto

### Vendas
- Criar venda
- Listar vendas
- Buscar venda por ID
- Atualizar venda
- Deletar venda

### Relatórios
- Maior Faturamento
- Novos Clientes
- Faturamento Mensal
- Encalhados

## Regras de negócio
- Forma de pagamento da venda: DINHEIRO ou CARTAO_CREDITO
- Para pagamento em dinheiro, valorPago é obrigatório
- Para pagamento em cartão, numeroCartao é obrigatório
- Imposto fixo de 9% nas vendas
- Não há controle de estoque
- Os produtos exibidos para venda são listados em ordem alfabética por nome

## Endpoints principais

### Clientes
- GET /clientes
- GET /clientes/{id}
- POST /clientes
- PUT /clientes/{id}
- DELETE /clientes/{id}

### Produtos
- GET /produtos
- GET /produtos/{id}
- POST /produtos
- PUT /produtos/{id}
- DELETE /produtos/{id}

### Vendas
- GET /vendas
- GET /vendas/{id}
- POST /vendas
- PUT /vendas/{id}
- DELETE /vendas/{id}

### Relatórios
- GET /relatorios/maior-faturamento
- GET /relatorios/novos-clientes/{ano}
- GET /relatorios/faturamento-mensal/{dataReferencia}
- GET /relatorios/encalhados

## Como executar o projeto

### Pré-requisitos
- Java 21
- Maven
- MongoDB em execução
> Observação: o projeto utiliza Quarkus Dev Services para subir automaticamente uma instância do MongoDB via Docker ao iniciar a aplicação em modo desenvolvimento.
> Portanto, é necessário que o Docker esteja aberto e rodando antes de executar o projeto.

### Rodando em modo desenvolvimento
```bash
./mvnw quarkus:dev