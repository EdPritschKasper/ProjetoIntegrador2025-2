package com.Restaurante.Dove.config;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Exceções genéricas lançadas pelos services
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        return buildResponse(ex.getMessage());
    }

    // Exceções de validação (IllegalArgumentException)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex) {
        String message = ex.getMessage();
        HttpStatus status;
        String error;

        if (message == null) message = "Erro de validação desconhecido";

        if (message.contains("preencher o nome")) {
            status = HttpStatus.BAD_REQUEST;
            error = "Nome do cliente é obrigatório";
        } else if (message.contains("email deve ser")) {
            status = HttpStatus.BAD_REQUEST;
            error = "Formato de e-mail inválido";
        } else if (message.contains("Senha")) {
            status = HttpStatus.BAD_REQUEST;
            error = "Senha inválida";
        } else {
            status = HttpStatus.BAD_REQUEST;
            error = "Erro de validação";
        }

        return response(status, error, message);
    }

    //  Entidade não encontrada (ex: ClienteRepository)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        return response(HttpStatus.NOT_FOUND, "Recurso não encontrado", "Entidade não encontrada no banco de dados");
    }

    // Exceções genéricas não tratadas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        return response(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado", ex.getMessage());
    }

    //  Método auxiliar de análise de RuntimeException
    private ResponseEntity<Object> buildResponse(String message) {
        if (message == null) message = "Erro desconhecido";

        HttpStatus status;
        String error;

        //  Detecção de mensagens personalizadas
        if (message.contains("não encontrado")) {
            status = HttpStatus.NOT_FOUND;
            error = "Recurso não encontrado";
        } else if (message.contains("obrigatório") || message.contains("obrigatória")) {
            status = HttpStatus.BAD_REQUEST;
            error = "Campo obrigatório ausente";
        } else if (message.contains("não pode ser nulo")) {
            status = HttpStatus.BAD_REQUEST;
            error = "Objeto inválido";
        } else if (message.contains("Nenhum cardápio informado")) {
            status = HttpStatus.BAD_REQUEST;
            error = "Cardápio obrigatório";
        } else if (message.contains("Já existe um cardápio cadastrado")) {
            status = HttpStatus.CONFLICT;
            error = "Data de cardápio duplicada";
        } else if (message.contains("Cardápio do dia não encontrado")) {
            status = HttpStatus.NOT_FOUND;
            error = "Cardápio do dia não encontrado";
        } else if (message.contains("Ingrediente não encontrado")) {
            status = HttpStatus.NOT_FOUND;
            error = "Ingrediente não encontrado";
        } else if (message.contains("Cardápio não encontrado")) {
            status = HttpStatus.NOT_FOUND;
            error = "Cardápio não encontrado";
        } else if (message.contains("Cliente não encontrado")) {
            status = HttpStatus.NOT_FOUND;
            error = "Cliente não encontrado";
        } else if (message.contains("Funcionário não encontrado")) {
            status = HttpStatus.NOT_FOUND;
            error = "Funcionário não encontrado";
        } else if (message.contains("Pedido não encontrado")) {
            status = HttpStatus.NOT_FOUND;
            error = "Pedido não encontrado";
        } else if (message.contains("Nenhum funcionário encontrado")) {
            status = HttpStatus.NOT_FOUND;
            error = "Nenhum funcionário encontrado no sistema";
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            error = "Erro interno no servidor";
        }

        return response(status, error, message);
    }

    // Método auxiliar para criar a resposta JSON padronizada
    private ResponseEntity<Object> response(HttpStatus status, String error, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
