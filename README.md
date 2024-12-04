Microserviço de Gestão de Produtos e Importação de CSV

Este microserviço é responsável pela gestão de produtos (CRUD), importação em massa de produtos via CSV e controle de estoque. 

Ele utiliza Spring Boot para o back-end, Spring Batch para o processamento de arquivos CSV e Spring Data JPA para interação com o banco de dados PostgreSQL.

Funcionalidades

Gestão de Produtos (CRUD): Criação, leitura, atualização e exclusão de produtos.

Importação de Produtos em Massa: Importação de produtos a partir de um arquivo CSV.

Controle de Estoque: Verificação e atualização de estoque de produtos.

Upload de Arquivos CSV: Carregamento de arquivos CSV contendo múltiplos produtos para o sistema.

Tecnologias Utilizadas

Spring Boot: Framework principal para a construção do microserviço.

Spring Batch: Processamento de arquivos CSV e carga de dados no banco de dados.

Spring Data JPA: Acesso ao banco de dados PostgreSQL.

PostgreSQL: Banco de dados relacional utilizado para persistir os dados.

Swagger/OpenAPI: Para a documentação da API REST.

Variáveis de Configuração

As configurações da aplicação podem ser alteradas no arquivo application.properties.

properties
```
# Nome da aplicação
spring.application.name=msproduto

# Configuração do Banco de Dados PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/fase4
spring.datasource.username=postgres
spring.datasource.password=password
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.platform=postgres

# Configurações do Hibernate e JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Configurações do Spring Batch
spring.batch.jdbc.initialize-schema=always
spring.sql.init.mode=always
spring.datasource.initialize=true

# Pool de Conexões com HikariCP
spring.datasource.hikari.driver-class-name=org.postgresql.Driver
spring.datasource.hikari.pool-name=HikariCP
spring.datasource.hikari.maximum-pool-size=10

# Caminho para armazenar arquivos CSV carregados
carga.input-path=C:/Users/SYS/Documents/fiap

# Desabilitar o Job Batch automaticamente no início
spring.batch.job.enabled=false

# Porta do servidor
server.port=8082
```
Descrição dos parâmetros:
spring.application.name: Nome da aplicação.
spring.datasource: Configurações de conexão com o banco de dados PostgreSQL.
spring.jpa.hibernate.ddl-auto: Definido como update, permite que o Hibernate atualize o esquema do banco de dados conforme necessário.
spring.batch.jdbc.initialize-schema: Garantir que o Spring Batch inicialize seu esquema no banco de dados.
carga.input-path: Diretório onde os arquivos CSV serão armazenados para processamento.
server.port: A porta onde o servidor da aplicação estará disponível.
Endpoints da API
1. Criar um Produto
Método: POST
Endpoint: /produtos
Descrição: Cria um novo produto no banco de dados.
Requisição:
json
```
{
    "nome": "Produto Exemplo",
    "descricao": "Descrição do produto",
    "preco": 150.00,
    "quantidadeestoque": 30
}
```
Resposta (200 OK):
json
```
{
    "id": 1,
    "nome": "Produto Exemplo",
    "descricao": "Descrição do produto",
    "preco": 150.00,
    "quantidadeestoque": 30,
    "createdatetime": "2024-12-03T00:00:00"
}
```
2. Obter Produto pelo ID
Método: GET
Endpoint: /produtos/{id}
Descrição: Recupera os detalhes de um produto específico pelo ID.
Resposta (200 OK):
json
```
{
    "id": 1,
    "nome": "Produto Exemplo",
    "descricao": "Descrição do produto",
    "preco": 150.00,
    "quantidadeestoque": 30,
    "createdatetime": "2024-12-03T00:00:00"
}
```
Resposta (404 NOT FOUND): Se o produto não for encontrado.
3. Obter Todos os Produtos
Método: GET
Endpoint: /produtos
Descrição: Retorna todos os produtos cadastrados no sistema.
Resposta (200 OK):
json
```
[
    {
        "id": 1,
        "nome": "Produto Exemplo",
        "descricao": "Descrição do produto",
        "preco": 150.00,
        "quantidadeestoque": 30,
        "createdatetime": "2024-12-03T00:00:00"
    },
    ...
]
```
4. Atualizar um Produto
Método: PUT
Endpoint: /produtos/{id}
Descrição: Atualiza um produto existente no banco de dados.
Requisição:
json
```
{
    "nome": "Produto Atualizado",
    "descricao": "Nova descrição do produto",
    "preco": 200.00,
    "quantidadeestoque": 50
}
```
Resposta (200 OK):
json
```
{
    "id": 1,
    "nome": "Produto Atualizado",
    "descricao": "Nova descrição do produto",
    "preco": 200.00,
    "quantidadeestoque": 50,
    "createdatetime": "2024-12-03T00:00:00"
}
```
5. Deletar um Produto
Método: DELETE
Endpoint: /produtos/{id}
Descrição: Deleta um produto do banco de dados.
Resposta (200 OK): Produto deletado com sucesso.
Resposta (404 NOT FOUND): Se o produto não for encontrado.
6. Importar Produtos em Massa (CSV)
Método: POST
Endpoint: /produtos/importar
Descrição: Realiza a importação em massa de produtos a partir de um arquivo CSV enviado.
Requisição:
json
```
[
    {
        "nome": "Produto 1",
        "descricao": "Descrição 1",
        "preco": 100.00,
        "quantidadeestoque": 50
    },
    {
        "nome": "Produto 2",
        "descricao": "Descrição 2",
        "preco": 200.00,
        "quantidadeestoque": 30
    }
]
```
Resposta (200 OK): Produtos importados com sucesso.
7. Verificar e Atualizar Estoque
Método: PUT

Endpoint: /produtos/verificar-estoque/{id}/quantidade/{quantidade}

Descrição: Verifica se há estoque suficiente para um produto e, se necessário, faz a baixa.

Resposta (200 OK):
json
```
"Estoque atualizado com sucesso."
Resposta (400 BAD REQUEST): Se a quantidade solicitada for maior que a quantidade em estoque.
Como Rodar o Microserviço
Pré-requisitos
Java 17 ou superior.
Maven ou Gradle.
PostgreSQL: Certifique-se de que o PostgreSQL está rodando em localhost:5432 e o banco de dados fase4 está criado.
Passos para execução:
Clone este repositório para sua máquina local.
Configure o PostgreSQL conforme as configurações no application.properties.
Execute o comando Maven para compilar e rodar o serviço:
bash
```
mvn spring-boot:run

O microserviço estará disponível em http://localhost:8082.

Conclusão

Esse microserviço permite gerenciar produtos e realizar a importação de dados em massa via CSV. Ele integra o Spring Batch para processamento de arquivos e oferece uma API RESTful para interação com os dados.

