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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        cardapio.setData(LocalDate.of(2025, 10, 18));
        cardapio.setIngredientes(new ArrayList<>());
        cardapio.setPedidos(new ArrayList<>());
    }

    @Test
    @DisplayName("Deve salvar cardápio com sucesso")
    void saveSuccess() {
        when(cardapioRepository.findAll()).thenReturn(new ArrayList<>());
        when(cardapioRepository.save(cardapio)).thenReturn(cardapio);

        CardapioEntity saved = cardapioService.save(cardapio);

        assertNotNull(saved);
        assertEquals(cardapio.getData(), saved.getData());
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar cardápio com data duplicada")
    void saveDuplicateDate() {
        List<CardapioEntity> existentes = List.of(cardapio);
        when(cardapioRepository.findAll()).thenReturn(existentes);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> cardapioService.save(cardapio));
        assertTrue(ex.getMessage().contains("Já existe um cardápio cadastrado"));
    }

    @Test
    @DisplayName("Deve buscar cardápio por ID")
    void findById() {
        when(cardapioRepository.findById(anyInt())).thenReturn(Optional.of(cardapio));

        CardapioEntity found = cardapioService.findById(1L);

        assertNotNull(found);
        assertEquals(cardapio.getData(), found.getData());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar ID inexistente")
    void findByIdNotFound() {
        when(cardapioRepository.findById(anyInt())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> cardapioService.findById(1L));
        assertTrue(ex.getMessage().contains("Cardapio não encontrado"));
    }
}
