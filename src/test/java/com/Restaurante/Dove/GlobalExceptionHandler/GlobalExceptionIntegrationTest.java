package com.Restaurante.Dove.GlobalExceptionHandler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GlobalExceptionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deve retornar 404 quando recurso não encontrado é lançado")
    void testRuntimeNotFound() throws Exception {
        mockMvc.perform(get("/api/teste/runtime")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Recurso não encontrado"))
                .andExpect(jsonPath("$.message").value(containsString("Pedido não encontrado")));
    }

    @Test
    @DisplayName("Deve retornar 400 para erro de campo obrigatório ausente")
    void testCampoObrigatorio() throws Exception {
        mockMvc.perform(get("/api/teste/obrigatorio")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Campo obrigatório ausente"))
                .andExpect(jsonPath("$.message").value(containsString("A descrição do ingrediente é obrigatória")));
    }

    @Test
    @DisplayName("Deve retornar 409 quando data de cardápio duplicada for lançada")
    void testCardapioDuplicado() throws Exception {
        mockMvc.perform(get("/api/teste/duplicado")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Data de cardápio duplicada"))
                .andExpect(jsonPath("$.message").value(containsString("Já existe um cardápio cadastrado")));
    }

    @Test
    @DisplayName("Deve retornar 400 para email inválido")
    void testEmailInvalido() throws Exception {
        mockMvc.perform(get("/api/teste/email-invalido")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Formato de e-mail inválido"))
                .andExpect(jsonPath("$.message").value(containsString("@gmail ou @hotmail")));
    }

    @Test
    @DisplayName("Deve retornar 500 para erro genérico inesperado")
    void testErroGenerico() throws Exception {
        mockMvc.perform(get("/api/teste/erro-generico")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Erro inesperado"))
                .andExpect(jsonPath("$.message").value(containsString("Falha inesperada")));
    }
}
