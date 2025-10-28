package com.Restaurante.Dove.services;

import com.Restaurante.Dove.model.CardapioEntity;
import com.Restaurante.Dove.model.PedidoEntity;
import com.Restaurante.Dove.model.IngredienteEntity;
import com.Restaurante.Dove.repository.CardapioRepository;
import com.Restaurante.Dove.repository.IngredienteRepository;
import com.Restaurante.Dove.repository.PedidoRepository;
import com.Restaurante.Dove.service.CardapioService;
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
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.DisplayName.class)
@Transactional
@Rollback
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CardapioServiceIntegrationTest {

    @Autowired
    private CardapioService cardapioService;

    @Autowired
    private CardapioRepository cardapioRepository;

    @Autowired
    private IngredienteRepository ingredienteRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    private CardapioEntity cardapio;

    @BeforeEach
    void setUp() {
//        pedidoRepository.deleteAll();
//        ingredienteRepository.deleteAll();
//        cardapioRepository.deleteAll();

        cardapio = new CardapioEntity();
        cardapio.setData(LocalDate.now());
        cardapio.setIngredientes(List.of());
        cardapio.setPedidos(List.of());

        cardapio = cardapioRepository.save(cardapio);
    }

    @Test
    @DisplayName("1. Deve salvar cardápio com sucesso")
    void save() {
        CardapioEntity novo = new CardapioEntity();
        novo.setData(LocalDate.now().plusDays(1));

        CardapioEntity saved = cardapioService.save(novo);

        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(novo.getData(), saved.getData());
    }

    @Test
    @DisplayName("2. Deve buscar cardápio por ID com sucesso")
    void findById() {
        CardapioEntity found = cardapioService.findById(cardapio.getId());
        Assertions.assertNotNull(found);
        Assertions.assertEquals(cardapio.getData(), found.getData());
    }

    @Test
    @DisplayName("3. Deve listar todos os cardápios existentes")
    void findAll() {
        CardapioEntity outro = new CardapioEntity();
        outro.setData(LocalDate.now().plusDays(2));
        cardapioRepository.save(outro);

        List<CardapioEntity> lista = cardapioService.findAll();

        Assertions.assertFalse(lista.isEmpty());
        Assertions.assertTrue(lista.size() >= 2);
    }

//    @Test
//    @DisplayName("4. Deve atualizar data de um cardápio existente")
//    void updateData() {
//        cardapio.setData(LocalDate.now().plusDays(5));
//        CardapioEntity updated = cardapioService.save(cardapio);
//
//        Assertions.assertEquals(LocalDate.now().plusDays(5), updated.getData());
//    }

//    @Test
//    @DisplayName("5. Deve salvar cardápio com pedidos associados")
//    void saveWithPedidos() {
//        CardapioEntity novoCardapio = new CardapioEntity();
//        novoCardapio.setData(LocalDate.now().plusDays(1));
//        novoCardapio = cardapioRepository.save(novoCardapio);
//
//        PedidoEntity pedido = new PedidoEntity();
//        pedido.setMarmita("Executiva");
//        pedido.setStatus("ABERTO");
//        pedido.setHora_inicio(LocalTime.of(10, 30));
//        pedido.setHora_fim(LocalTime.of(11, 30));
//        pedido.setCardapio(novoCardapio);
//
//        pedidoRepository.save(pedido);
//        novoCardapio.setPedidos(List.of(pedido));
//
//        CardapioEntity saved = cardapioService.save(novoCardapio);
//
//        Assertions.assertNotNull(saved.getPedidos());
//        Assertions.assertEquals(1, saved.getPedidos().size());
//        Assertions.assertEquals("Executiva", saved.getPedidos().get(0).getMarmita());
//    }


//    @Test
//    @DisplayName("6. Deve salvar cardápio com ingredientes associados")
//    void saveWithIngredientes() {
//        IngredienteEntity ingrediente = new IngredienteEntity();
//        ingrediente.setDescricao("Arroz");
//        ingredienteRepository.save(ingrediente);
//
//        cardapio.setIngredientes(List.of(ingrediente));
//        CardapioEntity saved = cardapioService.save(cardapio);
//
//        Assertions.assertNotNull(saved.getIngredientes());
//        Assertions.assertEquals(1, saved.getIngredientes().size());
//        Assertions.assertEquals("Arroz", saved.getIngredientes().get(0).getDescricao());
//    }

    @Test
    @DisplayName("7. Deve deletar cardápio existente com sucesso")
    void delete() {
        cardapioService.delete(cardapio.getId());
        Assertions.assertTrue(cardapioRepository.findById(Math.toIntExact(cardapio.getId())).isEmpty());
    }

    @Test
    @DisplayName("8. Deve lançar exceção ao buscar cardápio inexistente")
    void findByIdNotFound() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            cardapioService.findById(9999L);
        });
    }

    @Test
    @DisplayName("9. Deve salvar múltiplos cardápios e listar corretamente")
    void saveMultiple() {
        CardapioEntity c1 = new CardapioEntity();
        c1.setData(LocalDate.now().plusDays(1));
        CardapioEntity c2 = new CardapioEntity();
        c2.setData(LocalDate.now().plusDays(2));

        cardapioService.save(c1);
        cardapioService.save(c2);

        List<CardapioEntity> all = cardapioService.findAll();
        Assertions.assertTrue(all.size() >= 3);
    }

    @Test
    @DisplayName("10. Deve verificar persistência entre cardápio e pedidos")
    void persistPedidoRelacionamento() {
        PedidoEntity pedido1 = new PedidoEntity();
        pedido1.setMarmita("Vegetariana");
        pedido1.setStatus("FECHADO");
        pedido1.setHora_inicio(LocalTime.of(12, 0));
        pedido1.setHora_fim(LocalTime.of(12, 30));
        pedido1.setCardapio(cardapio);

        pedidoRepository.save(pedido1);

        CardapioEntity found = cardapioRepository.findById(Math.toIntExact(cardapio.getId())).orElseThrow();
        Assertions.assertNotNull(found);
        Assertions.assertEquals(cardapio.getId(), found.getId());
    }

    @Test
    @DisplayName("11. Deve permitir deletar cardápio com pedidos sem lançar exceção")
    void deleteWithPedidos() {
        PedidoEntity pedido = new PedidoEntity();
        pedido.setMarmita("Marmita Simples");
        pedido.setStatus("ABERTO");
        pedido.setHora_inicio(LocalTime.of(11, 0));
        pedido.setHora_fim(LocalTime.of(11, 30));
        pedido.setCardapio(cardapio);

        pedidoRepository.save(pedido);

        Assertions.assertDoesNotThrow(() -> cardapioService.delete(cardapio.getId()));
    }

    @Test
    @DisplayName("12. Deve garantir que repositório está vazio após exclusão em massa")
    void deleteAll() {
        cardapioRepository.deleteAll();
        List<CardapioEntity> all = cardapioRepository.findAll();
        Assertions.assertTrue(all.isEmpty());
    }

    @Test
    @DisplayName("13. Deve verificar se cardápio salvo é retornado pelo findAll")
    void verifyFindAllContainsSaved() {
        List<CardapioEntity> all = cardapioService.findAll();
        Optional<CardapioEntity> match = all.stream().filter(c -> c.getId().equals(cardapio.getId())).findFirst();
        Assertions.assertTrue(match.isPresent());
    }
}
