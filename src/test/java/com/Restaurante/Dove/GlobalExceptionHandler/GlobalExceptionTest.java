package com.Restaurante.Dove.GlobalExceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import com.Restaurante.Dove.config.GlobalExceptionHandler;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("Deve retornar 400 para erro de campo obrigatório")
    void testHandleRuntimeExceptionCampoObrigatorio() {
        RuntimeException ex = new RuntimeException("A descrição do ingrediente é obrigatória");
        ResponseEntity<Object> response = handler.handleRuntimeException(ex);

        assertEquals(400, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Campo obrigatório ausente"));
    }

    @Test
    @DisplayName("Deve retornar 404 para recurso não encontrado")
    void testHandleRuntimeExceptionNaoEncontrado() {
        RuntimeException ex = new RuntimeException("Pedido não encontrado com id: 10");
        ResponseEntity<Object> response = handler.handleRuntimeException(ex);

        assertEquals(404, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Recurso não encontrado"));
    }

    @Test
    @DisplayName("Deve retornar 409 para data de cardápio duplicada")
    void testHandleRuntimeExceptionCardapioDuplicado() {
        RuntimeException ex = new RuntimeException("Já existe um cardápio cadastrado para a data 2025-10-22");
        ResponseEntity<Object> response = handler.handleRuntimeException(ex);

        assertEquals(409, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Data de cardápio duplicada"));
    }

    @Test
    @DisplayName("Deve retornar 404 para EntityNotFoundException")
    void testHandleEntityNotFound() {
        EntityNotFoundException ex = new EntityNotFoundException();
        ResponseEntity<Object> response = handler.handleEntityNotFound(ex);

        assertEquals(404, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Entidade não encontrada"));
    }

    @Test
    @DisplayName("Deve retornar 400 para IllegalArgumentException de email inválido")
    void testHandleIllegalArgumentEmailInvalido() {
        IllegalArgumentException ex = new IllegalArgumentException("O email deve ser @gmail ou @hotmail");
        ResponseEntity<Object> response = handler.handleIllegalArgument(ex);

        assertEquals(400, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Formato de e-mail inválido"));
    }

    @Test
    @DisplayName("Deve retornar 500 para exceção genérica")
    void testHandleGenericException() {
        Exception ex = new Exception("Falha inesperada no servidor");
        ResponseEntity<Object> response = handler.handleGenericException(ex);

        assertEquals(500, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Erro inesperado"));
    }
}
