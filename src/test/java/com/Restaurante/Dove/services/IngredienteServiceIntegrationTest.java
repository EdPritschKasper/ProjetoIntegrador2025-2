package com.Restaurante.Dove.services;

import com.Restaurante.Dove.model.CardapioEntity;
import com.Restaurante.Dove.model.IngredienteEntity;
import com.Restaurante.Dove.model.PedidoEntity;
import com.Restaurante.Dove.repository.CardapioRepository;
import com.Restaurante.Dove.repository.IngredienteRepository;
import com.Restaurante.Dove.repository.PedidoRepository;
import com.Restaurante.Dove.service.IngredienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
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
        ingrediente = new IngredienteEntity();
        ingrediente.setDescricao("Arroz");
    }

    @Test
    @DisplayName("Deve salvar ingrediente com sucesso")
    void testSaveIngrediente() {
        IngredienteEntity salvo = ingredienteService.save(ingrediente);
        assertNotNull(salvo.getId());
        assertEquals("Arroz", salvo.getDescricao());
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar salvar ingrediente sem descrição")
    void testSaveIngredienteSemDescricao() {
        IngredienteEntity invalido = new IngredienteEntity();
        Exception exception = assertThrows(RuntimeException.class, () -> ingredienteService.save(invalido));
        assertEquals("A descrição do ingrediente é obrigatória", exception.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar ingrediente e relacionar com cardápio e pedido")
    void testUpdateComRelacionamentos() {
        // Cria e salva um cardapio com data obrigatoria
        CardapioEntity cardapio = new CardapioEntity();
        cardapio.setData(LocalDate.now());
        cardapio = cardapioRepository.save(cardapio);

        // Cria e salva um pedido associado a esse cardapio
        PedidoEntity pedido = new PedidoEntity();
        pedido.setCardapio(cardapio);
        pedido = pedidoRepository.save(pedido);

        IngredienteEntity salvo = ingredienteRepository.save(ingrediente);

        IngredienteEntity novo = new IngredienteEntity();
        novo.setDescricao("Feijão");
        novo.setCardapios(List.of(cardapio));
        novo.setPedidos(List.of(pedido));

        IngredienteEntity atualizado = ingredienteService.update(salvo.getId(), novo);

        assertEquals("Feijão", atualizado.getDescricao());
        assertEquals(1, atualizado.getCardapios().size());
        assertEquals(1, atualizado.getPedidos().size());
    }

    @Test
    @DisplayName("Deve deletar ingrediente com sucesso")
    void testDeleteIngrediente() {
        IngredienteEntity salvo = ingredienteRepository.save(ingrediente);
        ingredienteService.delete(salvo.getId());
        assertFalse(ingredienteRepository.existsById(Math.toIntExact(salvo.getId())));
    }

    @Test
    @DisplayName("Deve buscar ingredientes por cardápio e pedido")
    void testFindByCardapioAndPedido() {
        // Cria o ingrediente primeiro
        IngredienteEntity ingrediente = new IngredienteEntity();
        ingrediente.setDescricao("Arroz");
        ingrediente = ingredienteRepository.saveAndFlush(ingrediente);

        // Cria o cardapio e associa o ingrediente
        CardapioEntity cardapio = new CardapioEntity();
        cardapio.setData(LocalDate.now());
        cardapio.setIngredientes(List.of(ingrediente));
        cardapio = cardapioRepository.saveAndFlush(cardapio);

        // Cria o pedido e associa o ingrediente
        PedidoEntity pedido = new PedidoEntity();
        pedido.setCardapio(cardapio);
        pedido.setIngredientes(List.of(ingrediente));
        pedido = pedidoRepository.saveAndFlush(pedido);

        // Sincroniza tudo
        ingredienteRepository.flush();
        cardapioRepository.flush();
        pedidoRepository.flush();

        // Busca usando o service
        List<IngredienteEntity> porCardapio = ingredienteService.findByCardapios(cardapio);
        List<IngredienteEntity> porPedido = ingredienteService.findByPedidos(pedido);

        assertTrue(porCardapio.contains(ingrediente));
        assertTrue(porPedido.contains(ingrediente));
    }


}
