package com.Restaurante.Dove.GlobalExceptionHandler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teste")
public class TestExceptionController {

    @GetMapping("/runtime")
    public void gerarRuntime() {
        throw new RuntimeException("Pedido não encontrado com id: 10");
    }

    @GetMapping("/obrigatorio")
    public void gerarCampoObrigatorio() {
        throw new RuntimeException("A descrição do ingrediente é obrigatória");
    }

    @GetMapping("/duplicado")
    public void gerarDuplicado() {
        throw new RuntimeException("Já existe um cardápio cadastrado para a data 2025-10-22");
    }

    @GetMapping("/email-invalido")
    public void gerarEmailInvalido() {
        throw new IllegalArgumentException("O email deve ser @gmail ou @hotmail");
    }

    @GetMapping("/erro-generico")
    public void gerarErroGenerico() throws Exception {
        throw new Exception("Falha inesperada no servidor");
    }
}
