package com.Restaurante.Dove.GlobalExceptionHandler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConfigIntegrationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void testPasswordEncoderBeanExists() {
        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
        assertNotNull(encoder, "O bean PasswordEncoder deve estar carregado no contexto");
        assertTrue(encoder.matches("123", encoder.encode("123")), "O PasswordEncoder deve codificar corretamente");
    }
}
