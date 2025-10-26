package com.Restaurante.Dove.controllers;

import com.Restaurante.Dove.controller.IngredienteController;
import com.Restaurante.Dove.model.CardapioEntity;
import com.Restaurante.Dove.model.IngredienteEntity;
import com.Restaurante.Dove.model.PedidoEntity;
import com.Restaurante.Dove.service.IngredienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class IngredienteControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private IngredienteService ingredienteService;

    @InjectMocks
    private IngredienteController ingredienteController;

    private IngredienteEntity ingrediente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(ingredienteController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        ingrediente = new IngredienteEntity();
        ingrediente.setId(1L);
        ingrediente.setDescricao("Arroz");
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve retornar lista de ingredientes")
    void testFindAll() throws Exception {
        when(ingredienteService.findAll()).thenReturn(List.of(ingrediente));

        mockMvc.perform(get("/api/ingredientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Arroz"));

        verify(ingredienteService, times(1)).findAll();
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve buscar ingrediente por ID")
    void testFindById() throws Exception {
        when(ingredienteService.findById(1L)).thenReturn(ingrediente);

        mockMvc.perform(get("/api/ingredientes/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ingrediente.getId()))
                .andExpect(jsonPath("$.descricao").value("Arroz"));

        verify(ingredienteService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve salvar ingrediente com sucesso")
    void testSave() throws Exception {
        when(ingredienteService.save(any(IngredienteEntity.class))).thenReturn(ingrediente);

        mockMvc.perform(post("/api/ingredientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ingrediente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ingrediente.getId()))
                .andExpect(jsonPath("$.descricao").value("Arroz"));

        verify(ingredienteService, times(1)).save(any(IngredienteEntity.class));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve atualizar ingrediente existente")
    void testUpdate() throws Exception {
        when(ingredienteService.update(eq(1L), any(IngredienteEntity.class))).thenReturn(ingrediente);

        mockMvc.perform(put("/api/ingredientes/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ingrediente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Arroz"));

        verify(ingredienteService, times(1)).update(eq(1L), any(IngredienteEntity.class));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve retornar 404 ao atualizar ingrediente inexistente")
    void testUpdateNotFound() throws Exception {
        when(ingredienteService.update(eq(999L), any(IngredienteEntity.class))).thenReturn(null);

        mockMvc.perform(put("/api/ingredientes/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ingrediente)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("\"Ingrediente com ID 999 não encontrado.\""));

        verify(ingredienteService, times(1)).update(eq(999L), any(IngredienteEntity.class));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve deletar ingrediente")
    void testDelete() throws Exception {
        doNothing().when(ingredienteService).delete(1L);

        mockMvc.perform(delete("/api/ingredientes/{id}", 1))
                .andExpect(status().isNoContent());

        verify(ingredienteService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve buscar ingredientes por cardápio")
    void testFindByCardapios() throws Exception {
        CardapioEntity cardapio = new CardapioEntity();
        when(ingredienteService.findByCardapios(any(CardapioEntity.class))).thenReturn(List.of(ingrediente));

        mockMvc.perform(get("/api/ingredientes/findByCardapios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardapio)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Arroz"));

        verify(ingredienteService, times(1)).findByCardapios(any(CardapioEntity.class));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve buscar ingredientes por pedido")
    void testFindByPedidos() throws Exception {
        PedidoEntity pedido = new PedidoEntity();
        when(ingredienteService.findByPedidos(any(PedidoEntity.class))).thenReturn(List.of(ingrediente));

        mockMvc.perform(get("/api/ingredientes/findByPedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Arroz"));

        verify(ingredienteService, times(1)).findByPedidos(any(PedidoEntity.class));
    }
}
