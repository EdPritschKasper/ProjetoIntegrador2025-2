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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class IngredienteServiceTest {

    @Mock
    private IngredienteRepository ingredienteRepository;

    @Mock
    private CardapioRepository cardapioRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private IngredienteService ingredienteService;

    private IngredienteEntity ingrediente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ingrediente = new IngredienteEntity();
        ingrediente.setId(1L);
        ingrediente.setDescricao("Arroz");
        ingrediente.setCardapios(new ArrayList<>());
        ingrediente.setPedidos(new ArrayList<>());
    }

    // ---------- FIND ALL ----------
    @Test
    @DisplayName("Deve retornar todos os ingredientes")
    void testFindAll() {
        when(ingredienteRepository.findAll()).thenReturn(List.of(ingrediente));

        var result = ingredienteService.findAll();

        assertEquals(1, result.size());
        assertEquals("Arroz", result.get(0).getDescricao());
        verify(ingredienteRepository, times(1)).findAll();
    }

    // ---------- SAVE ----------
    @Test
    @DisplayName("Deve salvar ingrediente com sucesso")
    void testSaveSuccess() {
        when(ingredienteRepository.save(any(IngredienteEntity.class))).thenReturn(ingrediente);

        var result = ingredienteService.save(ingrediente);

        assertNotNull(result);
        assertEquals("Arroz", result.getDescricao());
        verify(ingredienteRepository, times(1)).save(any(IngredienteEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar ingrediente sem descrição")
    void testSaveFailWithoutDescricao() {
        ingrediente.setDescricao(" ");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ingredienteService.save(ingrediente));

        assertEquals("A descrição do ingrediente é obrigatória", exception.getMessage());
        verify(ingredienteRepository, never()).save(any());
    }

    // ---------- FIND BY ID ----------
    @Test
    @DisplayName("Deve retornar ingrediente por ID")
    void testFindByIdSuccess() {
        when(ingredienteRepository.findById(1)).thenReturn(Optional.of(ingrediente));

        var result = ingredienteService.findById(1L);

        assertNotNull(result);
        assertEquals("Arroz", result.getDescricao());
        verify(ingredienteRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao não encontrar ingrediente por ID")
    void testFindByIdNotFound() {
        when(ingredienteRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ingredienteService.findById(1L));

        assertEquals("Ingrediente não encontrado com id: 1", exception.getMessage());
    }

    // ---------- UPDATE ----------
    @Test
    @DisplayName("Deve atualizar ingrediente com sucesso")
    void testUpdateSuccess() {
        CardapioEntity cardapio = new CardapioEntity();
        cardapio.setId(1L);

        PedidoEntity pedido = new PedidoEntity();
        pedido.setId(1L);

        IngredienteEntity novo = new IngredienteEntity();
        novo.setDescricao("Feijão");
        novo.setCardapios(List.of(cardapio));
        novo.setPedidos(List.of(pedido));

        when(ingredienteRepository.findById(1)).thenReturn(Optional.of(ingrediente));
        when(cardapioRepository.findById(1)).thenReturn(Optional.of(cardapio));
        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedido));
        when(ingredienteRepository.save(any())).thenReturn(ingrediente);

        var result = ingredienteService.update(1L, novo);

        assertNotNull(result);
        assertEquals("Feijão", ingrediente.getDescricao());
        verify(ingredienteRepository, times(1)).save(ingrediente);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar ingrediente inexistente")
    void testUpdateIngredienteNotFound() {
        when(ingredienteRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ingredienteService.update(1L, ingrediente));

        assertEquals("Ingrediente não encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção se cardápio não existir no update")
    void testUpdateCardapioNotFound() {
        CardapioEntity cardapio = new CardapioEntity();
        cardapio.setId(99L);
        IngredienteEntity novo = new IngredienteEntity();
        novo.setDescricao("Feijão");
        novo.setCardapios(List.of(cardapio));

        when(ingredienteRepository.findById(1)).thenReturn(Optional.of(ingrediente));
        when(cardapioRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ingredienteService.update(1L, novo));

        assertEquals("Cardápio não encontrado: 99", exception.getMessage());
    }

    // ---------- DELETE ----------
    @Test
    @DisplayName("Deve deletar ingrediente existente")
    void testDeleteSuccess() {
        when(ingredienteRepository.existsById(1)).thenReturn(true);
        doNothing().when(ingredienteRepository).deleteById(1);

        ingredienteService.delete(1L);

        verify(ingredienteRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar ingrediente inexistente")
    void testDeleteNotFound() {
        when(ingredienteRepository.existsById(1)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ingredienteService.delete(1L));

        assertEquals("Ingrediente não encontrado com id: 1", exception.getMessage());
    }

    // ---------- FIND BY RELATIONSHIPS ----------
    @Test
    @DisplayName("Deve retornar ingredientes por cardápio")
    void testFindByCardapios() {
        CardapioEntity cardapio = new CardapioEntity();
        when(ingredienteRepository.findByCardapios(cardapio)).thenReturn(List.of(ingrediente));

        var result = ingredienteService.findByCardapios(cardapio);

        assertEquals(1, result.size());
        assertEquals("Arroz", result.get(0).getDescricao());
        verify(ingredienteRepository, times(1)).findByCardapios(cardapio);
    }

    @Test
    @DisplayName("Deve retornar ingredientes por pedido")
    void testFindByPedidos() {
        PedidoEntity pedido = new PedidoEntity();
        when(ingredienteRepository.findByPedidos(pedido)).thenReturn(List.of(ingrediente));

        var result = ingredienteService.findByPedidos(pedido);

        assertEquals(1, result.size());
        assertEquals("Arroz", result.get(0).getDescricao());
        verify(ingredienteRepository, times(1)).findByPedidos(pedido);
    }
}
