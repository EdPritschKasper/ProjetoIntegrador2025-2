    package com.Restaurante.Dove.services;

    import com.Restaurante.Dove.model.CardapioEntity;
    import com.Restaurante.Dove.model.PedidoEntity;
    import com.Restaurante.Dove.repository.CardapioRepository;
    import com.Restaurante.Dove.repository.PedidoRepository;
    import com.Restaurante.Dove.service.PedidoService;
    import org.junit.jupiter.api.*;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.test.annotation.DirtiesContext;
    import org.springframework.test.annotation.Rollback;
    import org.springframework.test.context.ActiveProfiles;
    import org.springframework.transaction.annotation.Transactional;

    import java.time.LocalDate;
    import java.time.LocalTime;
    import java.util.List;

    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @ActiveProfiles("test")
    @TestMethodOrder(MethodOrderer.DisplayName.class)
    @Transactional
    @Rollback
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public class PedidoServiceIntegrationTest {

        @Autowired
        PedidoService pedidoService;

        @Autowired
        PedidoRepository repository;

        @Autowired
        CardapioRepository cardapioRepository;

        PedidoEntity pedido;
        CardapioEntity cardapio;

        @BeforeEach
        void setUp() {
//            repository.deleteAll();
//            cardapioRepository.deleteAll();

            // Criando e salvando um cardápio
            cardapio = new CardapioEntity();
            cardapio.setData(LocalDate.now());
            cardapio = cardapioRepository.save(cardapio);

            // Criando e salvando um pedido
            pedido = new PedidoEntity();
            pedido.setMarmita("Marmita Média");
            pedido.setStatus("ABERTO");
            pedido.setHora_inicio(LocalTime.of(11, 30));
            pedido.setHora_fim(LocalTime.of(12, 0));
            pedido.setCardapio(cardapio);

            pedido = repository.save(pedido);
        }

        @Test
        @DisplayName("1. Deve salvar um pedido com sucesso")
        void save() {
            var novo = new PedidoEntity();
            novo.setMarmita("Marmita Grande");
            novo.setStatus("ABERTO");
            novo.setHora_inicio(LocalTime.of(12, 30));
            novo.setHora_fim(LocalTime.of(13, 0));
            novo.setCardapio(cardapio);

            var response = pedidoService.save(novo);

            Assertions.assertNotNull(response.getId());
            Assertions.assertEquals("Marmita Grande", response.getMarmita());
            Assertions.assertEquals("ABERTO", response.getStatus());
        }

        @Test
        @DisplayName("2. Deve atualizar um pedido existente com sucesso")
        void update() {
            pedido.setStatus("FINALIZADO");
            pedido.setMarmita("Marmita Pequena");

            var atualizado = pedidoService.update(pedido.getId(), pedido);

            Assertions.assertEquals("FINALIZADO", atualizado.getStatus());
            Assertions.assertEquals("Marmita Pequena", atualizado.getMarmita());
        }

        @Test
        @DisplayName("3. Deve lançar erro ao tentar atualizar pedido inexistente")
        void updateNotFound() {
            PedidoEntity fake = new PedidoEntity();
            fake.setMarmita("Fake");

            var ex = Assertions.assertThrows(RuntimeException.class,
                    () -> pedidoService.update(999L, fake));

            Assertions.assertTrue(ex.getMessage().contains("Pedido não encontrado"));
        }

        @Test
        @DisplayName("4. Deve buscar pedido por ID com sucesso")
        void findById() {
            var encontrado = pedidoService.findById(pedido.getId());
            Assertions.assertNotNull(encontrado);
            Assertions.assertEquals(pedido.getMarmita(), encontrado.getMarmita());
        }

        @Test
        @DisplayName("5. Deve lançar erro ao buscar pedido inexistente por ID")
        void findByIdInexistente() {
            var ex = Assertions.assertThrows(RuntimeException.class,
                    () -> pedidoService.findById(999L));

            Assertions.assertTrue(ex.getMessage().contains("Pedido não encontrado"));
        }

        @Test
        @DisplayName("6. Deve retornar todos os pedidos cadastrados")
        void findAll() {
            var lista = pedidoService.findAll();
            Assertions.assertFalse(lista.isEmpty());
            Assertions.assertEquals(1, lista.size());
        }

        @Test
        @DisplayName("7. Deve retornar pedidos pelo status informado")
        void findByStatus() {
            var resultados = pedidoService.findByStatus("ABERTO");

            Assertions.assertFalse(resultados.isEmpty());
            Assertions.assertEquals("ABERTO", resultados.get(0).getStatus());
        }

        @Test
        @DisplayName("8. Deve contar pedidos por data corretamente")
        void contarPedidosPorData() {
            int count = pedidoService.contarPedidosPorData(cardapio.getData());
            Assertions.assertTrue(count >= 1);
        }

        @Test
        @DisplayName("9. Deve lançar erro ao tentar salvar pedido sem cardápio válido")
        void saveSemCardapio() {
            var novo = new PedidoEntity();
            novo.setMarmita("Sem Cardápio");
            novo.setStatus("ABERTO");

            // cardápio inválido
            CardapioEntity fake = new CardapioEntity();
            fake.setId(999L);
            novo.setCardapio(fake);

            var ex = Assertions.assertThrows(RuntimeException.class, () -> pedidoService.save(novo));
            Assertions.assertTrue(ex.getMessage().contains("Cardápio não encontrado"));
        }

        @Test
        @DisplayName("10. Deve deletar pedido existente")
        void delete() {
            pedidoService.delete(pedido.getId());
            var result = repository.findById(Math.toIntExact(pedido.getId()));
            Assertions.assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("11. Deve lançar erro ao tentar deletar pedido inexistente")
        void deleteInexistente() {
            var ex = Assertions.assertThrows(RuntimeException.class, () -> pedidoService.delete(999L));
            Assertions.assertTrue(ex.getMessage().contains("Pedido não encontrado"));
        }

        @Test
        @DisplayName("12. Deve retornar pedidos por cardápio")
        void findByCardapio() {
            List<PedidoEntity> resultados = pedidoService.findByCardapio(cardapio);
            Assertions.assertFalse(resultados.isEmpty());
            Assertions.assertEquals(pedido.getId(), resultados.get(0).getId());
        }

        @Test
        @DisplayName("13. Deve lançar erro ao buscar por cardápio inexistente")
        void findByCardapioInexistente() {
            CardapioEntity fake = new CardapioEntity();
            fake.setId(999L);

            var ex = Assertions.assertThrows(RuntimeException.class, () -> pedidoService.findByCardapio(fake));
            Assertions.assertTrue(ex.getMessage().contains("Cardapio não encontrado"));
        }
    }
