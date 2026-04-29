-- Inserir Usuários (os usernames devem bater com os do Keycloak para linkar dados)
INSERT INTO tb_usuario (nome, username, cpf, email, senha, tipo) VALUES
('Wilian Admin', 'wilianadmin', '111.111.111-11', 'admin@dove.com', '$2a$10$xyz', 'ADMIN'),
('Didi Funcionário', 'didifunc', '222.222.222-22', 'didi@dove.com', '$2a$10$xyz', 'FUNCIONARIO'),
('Luan Cliente', 'luancliente', '333.333.333-33', 'luan@dove.com', '$2a$10$xyz', 'CLIENTE');

-- Inserir Cardápios
INSERT INTO tb_cardapio (data) VALUES
('2025-03-30'),
('2025-03-31'),
('2025-04-01');

-- Inserir Ingredientes
INSERT INTO tb_ingrediente (descricao) VALUES
('Arroz'),
('Feijão'),
('Frango'),
('Bife'),
('Alface'),
('Tomate'),
('Batata frita');

-- Relacionar Ingredientes com Cardápios (tb_cardapio_ingrediente)
INSERT INTO tb_cardapio_ingrediente (cardapio_id, ingrediente_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4),
(2, 4), (2, 5), (2, 6), (2, 7),
(3, 1), (3, 2), (3, 5), (3, 7);

-- Inserir Pedidos (Linkando com o usuario_id do tb_usuario)
INSERT INTO tb_pedido (marmita, status, hora_inicio, hora_fim, cardapio_id, usuario_id) VALUES
('grande', 'retirado', '12:20:05', '12:40:05', 1, 3),
('pequena', 'retirado', '12:30:00', '12:50:00', 2, 3),
('grande', 'preparando', '13:00:00', '13:10:00', 3, 3);

-- Relacionar Ingredientes escolhidos para cada Pedido
INSERT INTO tb_pedido_ingrediente (pedido_id, ingrediente_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4),
(2, 4), (2, 5), (2, 6), (2, 7),
(3, 1), (3, 2), (3, 5), (3, 7);
