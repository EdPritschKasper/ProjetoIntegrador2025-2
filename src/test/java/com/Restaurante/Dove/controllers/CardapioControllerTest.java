package com.Restaurante.Dove.controllers;

import com.Restaurante.Dove.controller.CardapioController;
import com.Restaurante.Dove.model.CardapioEntity;
import com.Restaurante.Dove.model.IngredienteEntity;
import com.Restaurante.Dove.model.PedidoEntity;
import com.Restaurante.Dove.service.CardapioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CardapioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CardapioService cardapioService;

    @InjectMocks
    private CardapioController cardapioController;

    private ObjectMapper objectMapper;
    private CardapioEntity cardapio;
    private IngredienteEntity ingrediente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc = MockMvcBuilders.standaloneSetup(cardapioController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        cardapio = new CardapioEntity();
        cardapio.setId(1L);
        cardapio.setData(LocalDate.of(2025, 10, 18));
    }


    @Test
    @DisplayName("GET /api/cardapios - Deve retornar lista de cardápios")
    void testFindAll() throws Exception {
        when(cardapioService.findAll()).thenReturn(List.of(cardapio));

        mockMvc.perform(get("/api/cardapios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].data").value(cardapio.getData().toString()));

        verify(cardapioService, times(1)).findAll();
    }


    @Test
    @DisplayName("POST /api/cardapios - Deve salvar cardápio")
    void testSave() throws Exception {
        when(cardapioService.save(any(CardapioEntity.class))).thenReturn(cardapio);

        mockMvc.perform(post("/api/cardapios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardapio)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(cardapio.getId()))
                .andExpect(jsonPath("$.data").value(cardapio.getData().toString()));

        verify(cardapioService, times(1)).save(any(CardapioEntity.class));
    }

    @Test
    @DisplayName("GET /api/cardapios/{id} - Deve buscar cardápio por ID")
    void testFindById() throws Exception {
        when(cardapioService.findById(1L)).thenReturn(cardapio);

        mockMvc.perform(get("/api/cardapios/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cardapio.getId()))
                .andExpect(jsonPath("$.data").value(cardapio.getData().toString()));

        verify(cardapioService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("DELETE /api/cardapios/{id} - Deve deletar cardápio")
    void testDelete() throws Exception {
        doNothing().when(cardapioService).delete(1L);

        mockMvc.perform(delete("/api/cardapios/{id}", 1))
                .andExpect(status().isNoContent());

        verify(cardapioService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("Validar update - sucesso -> 200")
    void update_sucesso() {
        Integer id = 5;
        var entrada = new CardapioEntity();
        var atualizado = new CardapioEntity();
        atualizado.setId(id.longValue());

        when(cardapioService.update(id.longValue(), entrada)).thenReturn(atualizado);

        ResponseEntity<?> response = cardapioController.update(id, entrada);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(atualizado, response.getBody());
        verify(cardapioService, times(1)).update(id.longValue(), entrada);
    }

    @Test
    @DisplayName("Validar update - não encontrado -> 404")
    void update_naoEncontrado() {
        Integer id = 7;
        var entrada = new CardapioEntity();

        when(cardapioService.update(id.longValue(), entrada)).thenReturn(null);

        ResponseEntity<?> response = cardapioController.update(id, entrada);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cardápio com ID " + id + " não encontrado.", response.getBody());
        verify(cardapioService, times(1)).update(id.longValue(), entrada);
    }

    @Test
    @DisplayName("Validar findByPedidos (Pedido) -> 200")
    void findByPedidos_porPedido() {
        var pedido = new PedidoEntity();
        var card = new CardapioEntity();
        when(cardapioService.findByPedidos(pedido)).thenReturn(Optional.of(card));

        ResponseEntity<Optional<CardapioEntity>> response = cardapioController.findByPedidos(pedido);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Optional.of(card), response.getBody());
        verify(cardapioService, times(1)).findByPedidos(pedido);
    }

    @Test
    @DisplayName("Validar findByIngredientes -> 200")
    void findByPedidos_porIngrediente() {
        var ingrediente = new IngredienteEntity();
        var c1 = new CardapioEntity();
        var c2 = new CardapioEntity();
        List<CardapioEntity> esperado = List.of(c1, c2);
        when(cardapioService.findByIngredientes(ingrediente)).thenReturn(esperado);

        ResponseEntity<List<CardapioEntity>> response = cardapioController.findByPedidos(ingrediente);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(esperado, response.getBody());
        verify(cardapioService, times(1)).findByIngredientes(ingrediente);
    }

    @Test
    @DisplayName("Validar getCardapioDoDia -> 200 (retorno do serviço)")
    void getCardapioDoDia() {
        var esperado = new CardapioEntity();
        when(cardapioService.getCardapioDoDia()).thenReturn(esperado);

        CardapioEntity result = cardapioController.getCardapioDoDia();

        assertEquals(esperado, result);
        verify(cardapioService, times(1)).getCardapioDoDia();
    }
}
