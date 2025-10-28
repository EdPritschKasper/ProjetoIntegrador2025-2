package com.Restaurante.Dove.services;

import com.Restaurante.Dove.model.CardapioEntity;
import com.Restaurante.Dove.model.IngredienteEntity;
import com.Restaurante.Dove.model.PedidoEntity;
import com.Restaurante.Dove.repository.CardapioRepository;
import com.Restaurante.Dove.repository.IngredienteRepository;
import com.Restaurante.Dove.repository.PedidoRepository;
import com.Restaurante.Dove.service.IngredienteService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.DisplayName.class)
@Rollback
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class IngredienteServiceIntegrationTest {

    @Autowired
    private IngredienteService ingredienteService;

    @Autowired
    private IngredienteRepository ingredienteRepository;

    @Autowired
    private CardapioRepository cardapioRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    private IngredienteEntity ingrediente;

    @BeforeEach
    void setup() {
//        ingredienteRepository.deleteAll();
//        cardapioRepository.deleteAll();
//        pedidoRepository.deleteAll();

        ingrediente = new IngredienteEntity();
        ingrediente.setDescricao("Arroz");
    }

    @Test
    @DisplayName("1. Deve salvar ingrediente com sucesso")
    void testSaveIngrediente() {
        IngredienteEntity salvo = ingredienteService.save(ingrediente);
        assertNotNull(salvo.getId());
        assertEquals("Arroz", salvo.getDescricao());
    }

    @Test
    @DisplayName("2. Deve lançar erro ao salvar ingrediente sem descrição")
    void testSaveIngredienteSemDescricao() {
        IngredienteEntity invalido = new IngredienteEntity();
        Exception exception = assertThrows(RuntimeException.class, () -> ingredienteService.save(invalido));
        assertEquals("A descrição do ingrediente é obrigatória", exception.getMessage());
    }

    @Test
    @DisplayName("3. Deve buscar ingrediente por ID")
    void testFindById() {
        IngredienteEntity salvo = ingredienteRepository.save(ingrediente);
        IngredienteEntity encontrado = ingredienteService.findById(salvo.getId());
        assertEquals(salvo.getDescricao(), encontrado.getDescricao());
    }

    @Test
    @DisplayName("4. Deve lançar erro ao buscar ID inexistente")
    void testFindByIdInexistente() {
        assertThrows(RuntimeException.class, () -> ingredienteService.findById(9999L));
    }

//    @Test
//    @DisplayName("5. Deve atualizar ingrediente com novos relacionamentos")
//    void testUpdateComRelacionamentos() {
//        // Cardapio e Pedido
//        CardapioEntity cardapio = new CardapioEntity();
//        cardapio.setData(LocalDate.now());
//        cardapio = cardapioRepository.save(cardapio);
//
//        PedidoEntity pedido = new PedidoEntity();
//        pedido.setCardapio(cardapio);
//        pedido = pedidoRepository.save(pedido);
//
//        IngredienteEntity salvo = ingredienteRepository.save(ingrediente);
//
//        IngredienteEntity novo = new IngredienteEntity();
//        novo.setDescricao("Feijão");
//        novo.setCardapios(List.of(cardapio));
//        novo.setPedidos(List.of(pedido));
//
//        IngredienteEntity atualizado = ingredienteService.update(salvo.getId(), novo);
//
//        assertEquals("Feijão", atualizado.getDescricao());
//        assertEquals(1, atualizado.getCardapios().size());
//        assertEquals(1, atualizado.getPedidos().size());
//    }

    @Test
    @DisplayName("6. Deve atualizar ingrediente sem relacionamentos")
    void testUpdateSemRelacionamentos() {
        IngredienteEntity salvo = ingredienteRepository.save(ingrediente);

        IngredienteEntity novo = new IngredienteEntity();
        novo.setDescricao("Feijão");

        IngredienteEntity atualizado = ingredienteService.update(salvo.getId(), novo);
        assertEquals("Feijão", atualizado.getDescricao());
        assertTrue(atualizado.getCardapios().isEmpty());
        assertTrue(atualizado.getPedidos().isEmpty());
    }

    @Test
    @DisplayName("7. Deve lançar erro ao atualizar ingrediente inexistente")
    void testUpdateInexistente() {
        IngredienteEntity novo = new IngredienteEntity();
        novo.setDescricao("Feijão");

        assertThrows(RuntimeException.class, () -> ingredienteService.update(9999L, novo));
    }

    @Test
    @DisplayName("8. Deve deletar ingrediente com sucesso")
    void testDeleteIngrediente() {
        IngredienteEntity salvo = ingredienteRepository.save(ingrediente);
        ingredienteService.delete(salvo.getId());
        assertFalse(ingredienteRepository.existsById(Math.toIntExact(salvo.getId())));
    }

    @Test
    @DisplayName("9. Deve lançar erro ao deletar ingrediente inexistente")
    void testDeleteInexistente() {
        assertThrows(RuntimeException.class, () -> ingredienteService.delete(9999L));
    }

//    @Test
//    @DisplayName("10. Deve buscar todos os ingredientes")
//    void testFindAll() {
//        IngredienteEntity i1 = ingredienteRepository.save(ingrediente);
//
//        IngredienteEntity i2 = new IngredienteEntity();
//        i2.setDescricao("Feijão");
//        ingredienteRepository.save(i2);
//
//        List<IngredienteEntity> todos = ingredienteService.findAll();
//        assertEquals(2, todos.size());
//    }

//    @Test
//    @DisplayName("11. Deve buscar ingredientes por cardápio e pedido")
//    void testFindByCardapioAndPedido() {
//        IngredienteEntity ingr = ingredienteRepository.save(ingrediente);
//
//        CardapioEntity cardapio = new CardapioEntity();
//        cardapio.setData(LocalDate.now());
//        cardapio.setIngredientes(List.of(ingr));
//        cardapio = cardapioRepository.save(cardapio);
//
//        PedidoEntity pedido = new PedidoEntity();
//        pedido.setCardapio(cardapio);
//        pedido.setIngredientes(List.of(ingr));
//        pedido = pedidoRepository.save(pedido);
//
//        List<IngredienteEntity> porCardapio = ingredienteService.findByCardapios(cardapio);
//        List<IngredienteEntity> porPedido = ingredienteService.findByPedidos(pedido);
//
//        assertTrue(porCardapio.contains(ingr));
//        assertTrue(porPedido.contains(ingr));
//    }

//    @Test
//    @DisplayName("12. Deve salvar múltiplos ingredientes e buscar todos")
//    void testSaveMultiplos() {
//        IngredienteEntity i1 = new IngredienteEntity();
//        i1.setDescricao("Feijão");
//        IngredienteEntity i2 = new IngredienteEntity();
//        i2.setDescricao("Macarrão");
//
//        ingredienteService.save(i1);
//        ingredienteService.save(i2);
//
//        List<IngredienteEntity> todos = ingredienteService.findAll();
//        assertEquals(2, todos.size());
//    }

}
