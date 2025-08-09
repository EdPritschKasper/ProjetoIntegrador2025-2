-- DROP TABLES - para resetar o banco
drop table tb_pedido_ingrediente;
drop table tb_cardapio_ingrediente;
drop table tb_pedido;
drop table tb_cardapio;
drop table tb_funcionario;
drop table tb_ingrediente;
drop table tb_cliente;

CREATE TABLE tb_funcionario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    cpf VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE tb_cliente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(50) NOT NULL
);

CREATE TABLE tb_cardapio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data DATE NOT NULL
);

CREATE TABLE tb_ingrediente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(50) NOT NULL
);

CREATE TABLE tb_pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    marmita VARCHAR(20),
    status VARCHAR(10),
    hora_inicio TIME NOT NULL,
    hora_fim TIME,
    cardapio_id BIGINT,
    funcionario_id BIGINT,
    cliente_id BIGINT,
    CONSTRAINT fk_pedido_cardapio FOREIGN KEY (cardapio_id) REFERENCES tb_cardapio (id),
    CONSTRAINT fk_pedido_funcionario FOREIGN KEY (funcionario_id) REFERENCES tb_funcionario (id),
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES tb_cliente (id)
);

CREATE TABLE tb_cardapio_ingrediente (
    cardapio_id BIGINT NOT NULL,
    ingrediente_id BIGINT NOT NULL,
    CONSTRAINT fk_ci_cardapio FOREIGN KEY (cardapio_id) REFERENCES tb_cardapio (id),
    CONSTRAINT fk_ci_ingrediente FOREIGN KEY (ingrediente_id) REFERENCES tb_ingrediente (id)
);

CREATE TABLE tb_pedido_ingrediente (
    pedido_id BIGINT NOT NULL,
    ingrediente_id BIGINT NOT NULL,
    CONSTRAINT fk_pi_pedido FOREIGN KEY (pedido_id) REFERENCES tb_pedido (id),
    CONSTRAINT fk_pi_ingrediente FOREIGN KEY (ingrediente_id) REFERENCES tb_ingrediente (id)
);


-- INSERT INTO
insert into tb_funcionario (nome, cpf) values
('Didi Mocó', '111.111.111-11'),
('Zacarias', '222.222.222-22');

insert into tb_cliente (nome, email, senha) values
('Dado Dolabela', 'dado@gmail.com', 'iloveyou'),
('Luan Santana', 'luan@gmail.com', 'meteorodapaixao');

INSERT INTO tb_cardapio (data) VALUES
('2025-03-30'),
('2025-03-31'),
('2025-04-01');

insert into tb_ingrediente (descricao) values
('arroz'),
('feijão'),
('frango'),
('bife'),
('alface'),
('tomate'),
('batata frita');

insert into tb_pedido (marmita, status, hora_inicio, hora_fim, cardapio_id, funcionario_id, cliente_id) values
(null, 'retirado', '12:20:05','12:40:05', 1, 1, 1),
('pequena', 'retirado', '12:20:05','12:40:05', 2, 2, 2),
('grande', 'preparando', '12:20:05','12:40:05', 3, 1, 2);

insert into tb_cardapio_ingrediente (cardapio_id, ingrediente_id) values
(1, 1), (1, 2), (1, 3), (1, 4),
(2, 4), (2, 5), (2, 6), (2, 7),
(3, 1), (3, 2), (3, 5), (3, 7);

insert into tb_pedido_ingrediente (pedido_id, ingrediente_id) values
(1, 1), (1, 2), (1, 3), (1, 4),
(2, 4), (2, 5), (2, 6), (2, 7),
(3, 1), (3, 2), (3, 5), (3, 7);

-- SELECTS
select * from tb_cardapio;
select * from tb_pedido;
select * from tb_cardapio_ingrediente;
select * from tb_pedido_ingrediente;
select * from tb_funcionario;
select * from tb_cliente;
select * from tb_ingrediente;
