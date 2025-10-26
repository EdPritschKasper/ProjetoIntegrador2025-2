package com.Restaurante.Dove.controllers;

import com.Restaurante.Dove.controller.FuncionarioController;
import com.Restaurante.Dove.model.FuncionarioEntity;
import com.Restaurante.Dove.service.FuncionarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class FuncionarioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FuncionarioService funcionarioService;

    @InjectMocks
    private FuncionarioController funcionarioController;

    private ObjectMapper objectMapper;
    private FuncionarioEntity funcionario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc = MockMvcBuilders.standaloneSetup(funcionarioController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        funcionario = new FuncionarioEntity();
        funcionario.setId(1L);
        funcionario.setNome("João Silva");
        funcionario.setCpf("12345678900");
    }

    @Test
    @DisplayName("GET /api/funcionario/findAll - Deve retornar lista de funcionários")
    void testFindAll() throws Exception {
        when(funcionarioService.findAll()).thenReturn(List.of(funcionario));

        mockMvc.perform(get("/api/funcionario/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(funcionario.getId()))
                .andExpect(jsonPath("$[0].nome").value(funcionario.getNome()));

        verify(funcionarioService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/funcionario/findById/{id} - Deve retornar funcionário por ID")
    void testFindById() throws Exception {
        when(funcionarioService.findById(1L)).thenReturn(funcionario);

        mockMvc.perform(get("/api/funcionario/findById/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(funcionario.getId()))
                .andExpect(jsonPath("$.nome").value(funcionario.getNome()));

        verify(funcionarioService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("POST /api/funcionario/save - Deve salvar um funcionário")
    void testSave() throws Exception {
        when(funcionarioService.save(any(FuncionarioEntity.class))).thenReturn(funcionario);

        mockMvc.perform(post("/api/funcionario/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funcionario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(funcionario.getId()))
                .andExpect(jsonPath("$.nome").value(funcionario.getNome()))
                .andExpect(jsonPath("$.cpf").value(funcionario.getCpf()));

        verify(funcionarioService, times(1)).save(any(FuncionarioEntity.class));
    }

    @Test
    @DisplayName("PUT /api/funcionario/update/{id} - Deve atualizar um funcionário")
    void testUpdate() throws Exception {
        when(funcionarioService.update(eq(1L), any(FuncionarioEntity.class))).thenReturn(funcionario);

        mockMvc.perform(put("/api/funcionario/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funcionario)));

        verify(funcionarioService, times(1)).update(eq(1L), any(FuncionarioEntity.class));
    }

    @Test
    @DisplayName("DELETE /api/funcionario/delete/{id} - Deve deletar funcionário")
    void testDelete() throws Exception {
        doNothing().when(funcionarioService).delete(1L);

        mockMvc.perform(delete("/api/funcionario/delete/{id}", 1))
                .andExpect(status().isNoContent());

        verify(funcionarioService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("GET /api/funcionario/findByNome - Deve buscar funcionário pelo nome")
    void testFindByNome() throws Exception {
        when(funcionarioService.findByNome("João")).thenReturn(List.of(funcionario));

        mockMvc.perform(get("/api/funcionario/findByNome")
                        .param("nome", "João"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("João Silva"));

        verify(funcionarioService, times(1)).findByNome("João");
    }

    @Test
    @DisplayName("GET /api/funcionario/findByCpf - Deve buscar funcionário pelo CPF")
    void testFindByCpf() throws Exception {
        when(funcionarioService.findByCpf("12345678900")).thenReturn(Optional.of(funcionario));

        mockMvc.perform(get("/api/funcionario/cpf/{cpf}", "12345678900"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value("12345678900"));

        verify(funcionarioService, times(1)).findByCpf("12345678900");
    }

    @Test
    @DisplayName("GET /api/funcionario/relatorio/{id} - Deve gerar relatório de pedidos")
    void testGerarRelatorioPedidos() throws Exception {
        Map<String, Object> relatorio = Map.of("totalPedidos", 10, "nomeFuncionario", "João Silva");
        when(funcionarioService.gerarRelatorioPedidos(1L)).thenReturn(relatorio);

        mockMvc.perform(get("/api/funcionario/relatorio/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPedidos").value(10))
                .andExpect(jsonPath("$.nomeFuncionario").value("João Silva"));

        verify(funcionarioService, times(1)).gerarRelatorioPedidos(1L);
    }

    @Test
    @DisplayName("GET /api/funcionario/funcionarioMP - Deve retornar funcionário com mais pedidos")
    void testFuncionarioMaisPedidos() throws Exception {
        Map<String, Object> funcionarioMaisPedidos = Map.of("nome", "João Silva", "totalPedidos", 15);
        when(funcionarioService.funcionarioMaisPedidos()).thenReturn(funcionarioMaisPedidos);

        mockMvc.perform(get("/api/funcionario/funcionarioMP"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.totalPedidos").value(15));

        verify(funcionarioService, times(1)).funcionarioMaisPedidos();
    }
}
