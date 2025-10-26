package com.Restaurante.Dove.services;

import com.Restaurante.Dove.model.CardapioEntity;
import com.Restaurante.Dove.model.IngredienteEntity;
import com.Restaurante.Dove.model.PedidoEntity;
import com.Restaurante.Dove.repository.CardapioRepository;
import com.Restaurante.Dove.repository.IngredienteRepository;
import com.Restaurante.Dove.repository.PedidoRepository;
import com.Restaurante.Dove.service.CardapioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CardapioServiceTest {

    @Mock
    private CardapioRepository cardapioRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private IngredienteRepository ingredienteRepository;

    @InjectMocks
    private CardapioService cardapioService;

    private CardapioEntity cardapio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cardapio = new CardapioEntity();
        cardapio.setId(1L);
        cardapio.setData(LocalDate.of(2025, 10, 18));
        cardapio.setIngredientes(new ArrayList<>());
        cardapio.setPedidos(new ArrayList<>());
    }

    // ---------- SAVE ----------
    @Test
    @DisplayName("Deve salvar cardápio com sucesso")
    void saveSuccess() {
        when(cardapioRepository.findAll()).thenReturn(new ArrayList<>());
        when(cardapioRepository.save(cardapio)).thenReturn(cardapio);

        CardapioEntity saved = cardapioService.save(cardapio);

        assertNotNull(saved);
        assertEquals(cardapio.getData(), saved.getData());
        verify(cardapioRepository).save(cardapio);
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar cardápio com data duplicada")
    void saveDuplicateDate() {
        when(cardapioRepository.findAll()).thenReturn(List.of(cardapio));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> cardapioService.save(cardapio));
        assertTrue(ex.getMessage().contains("Já existe um cardápio cadastrado"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar cardápio sem data")
    void saveWithoutDate() {
        cardapio.setData(null);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> cardapioService.save(cardapio));
        assertTrue(ex.getMessage().contains("A data do cardápio é obrigatória"));
    }

    // ---------- FIND ----------
    @Test
    @DisplayName("Deve buscar todos os cardápios")
    void findAll() {
        when(cardapioRepository.findAll()).thenReturn(List.of(cardapio));

        List<CardapioEntity> result = cardapioService.findAll();

        assertEquals(1, result.size());
        assertEquals(cardapio.getData(), result.get(0).getData());
    }

    @Test
    @DisplayName("Deve buscar cardápio por ID")
    void findByIdSuccess() {
        when(cardapioRepository.findById(1)).thenReturn(Optional.of(cardapio));

        CardapioEntity found = cardapioService.findById(1L);

        assertNotNull(found);
        assertEquals(cardapio.getData(), found.getData());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar ID inexistente")
    void findByIdNotFound() {
        when(cardapioRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> cardapioService.findById(1L));
        assertTrue(ex.getMessage().contains("Cardapio não encontrado"));
    }

    // ---------- UPDATE ----------
    @Test
    @DisplayName("Deve atualizar cardápio com sucesso")
    void updateSuccess() {
        IngredienteEntity ingrediente = new IngredienteEntity();
        ingrediente.setId(1L);
        PedidoEntity pedido = new PedidoEntity();
        pedido.setId(2L);

        CardapioEntity novo = new CardapioEntity();
        novo.setData(LocalDate.of(2025, 12, 1));
        novo.setIngredientes(List.of(ingrediente));
        novo.setPedidos(List.of(pedido));

        when(cardapioRepository.findById(1)).thenReturn(Optional.of(cardapio));
        when(ingredienteRepository.findById(1)).thenReturn(Optional.of(ingrediente));
        when(pedidoRepository.findById(2)).thenReturn(Optional.of(pedido));
        when(cardapioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        CardapioEntity atualizado = cardapioService.update(1L, novo);

        assertEquals(novo.getData(), atualizado.getData());
        assertEquals(1, atualizado.getIngredientes().size());
        assertEquals(1, atualizado.getPedidos().size());
        verify(cardapioRepository).save(atualizado);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar cardápio inexistente")
    void updateNotFound() {
        when(cardapioRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> cardapioService.update(1L, new CardapioEntity()));
        assertTrue(ex.getMessage().contains("Cardápio não encontrado"));
    }

    @Test
    @DisplayName("Deve lançar exceção se ingrediente não for encontrado")
    void updateIngredienteNotFound() {
        IngredienteEntity ingrediente = new IngredienteEntity();
        ingrediente.setId(1L);

        CardapioEntity novo = new CardapioEntity();
        novo.setIngredientes(List.of(ingrediente));

        when(cardapioRepository.findById(1)).thenReturn(Optional.of(cardapio));
        when(ingredienteRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> cardapioService.update(1L, novo));
        assertTrue(ex.getMessage().contains("Ingrediente não encontrado"));
    }

    @Test
    @DisplayName("Deve lançar exceção se pedido não for encontrado")
    void updatePedidoNotFound() {
        PedidoEntity pedido = new PedidoEntity();
        pedido.setId(2L);

        CardapioEntity novo = new CardapioEntity();
        novo.setPedidos(List.of(pedido));

        when(cardapioRepository.findById(1)).thenReturn(Optional.of(cardapio));
        when(pedidoRepository.findById(2)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> cardapioService.update(1L, novo));
        assertTrue(ex.getMessage().contains("Pedido não encontrado"));
    }

    // ---------- DELETE ----------
    @Test
    @DisplayName("Deve deletar cardápio existente")
    void deleteSuccess() {
        when(cardapioRepository.existsById(1)).thenReturn(true);

        cardapioService.delete(1L);

        verify(cardapioRepository).deleteById(1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar cardápio inexistente")
    void deleteNotFound() {
        when(cardapioRepository.existsById(1)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> cardapioService.delete(1L));
        assertTrue(ex.getMessage().contains("Cardapio não encontrado"));
    }

    // ---------- FIND BY RELATION ----------
    @Test
    @DisplayName("Deve buscar cardápio por pedido")
    void findByPedidos() {
        PedidoEntity pedido = new PedidoEntity();
        when(cardapioRepository.findByPedidos(pedido)).thenReturn(Optional.of(cardapio));

        Optional<CardapioEntity> result = cardapioService.findByPedidos(pedido);

        assertTrue(result.isPresent());
        assertEquals(cardapio, result.get());
    }

    @Test
    @DisplayName("Deve buscar cardápio por ingrediente")
    void findByIngredientes() {
        IngredienteEntity ingrediente = new IngredienteEntity();
        when(cardapioRepository.findByIngredientes(ingrediente)).thenReturn(List.of(cardapio));

        List<CardapioEntity> result = cardapioService.findByIngredientes(ingrediente);

        assertEquals(1, result.size());
        assertEquals(cardapio, result.get(0));
    }

    // ---------- GET CARDÁPIO DO DIA ----------
    @Test
    @DisplayName("Deve retornar cardápio do dia com sucesso")
    void getCardapioDoDiaSuccess() {
        when(cardapioRepository.findByData(LocalDate.now())).thenReturn(Optional.of(cardapio));

        CardapioEntity result = cardapioService.getCardapioDoDia();

        assertNotNull(result);
        assertEquals(cardapio.getData(), result.getData());
    }

    @Test
    @DisplayName("Deve lançar exceção se cardápio do dia não for encontrado")
    void getCardapioDoDiaNotFound() {
        when(cardapioRepository.findByData(LocalDate.now())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> cardapioService.getCardapioDoDia());
        assertTrue(ex.getMessage().contains("Cardápio do dia não encontrado"));
    }
}
