DROP TABLE IF EXISTS produto;

create table produto
(
    id                serial PRIMARY KEY,
    nome              varchar(240),
    descricao         varchar(240),
    preco             numeric(10, 2),
    quantidadeestoque bigint,
    createdatetime  timestamp
)
