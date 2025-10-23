package com.Restaurante.Dove.controllers;

import com.Restaurante.Dove.controller.ClienteController;
import com.Restaurante.Dove.repository.ClienteRepository;
import com.Restaurante.Dove.model.ClienteEntity;
import com.Restaurante.Dove.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ClienteControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    ClienteController clienteController;

    @Mock
    private ClienteService clienteService;
    @Mock
    private ClienteRepository repo;
    @Mock
    private ClienteEntity cliente;


    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

        cliente = new ClienteEntity();
        cliente.setId(1L);
        cliente.setNome("Edkasper");
        cliente.setEmail("teste@gmail.com");
        cliente.setSenha("123123");

    }

    private Map<String, String> body(String atual, String nova, String conf) {
        return Map.ofEntries(
                atual != null ? Map.entry("123123", atual) : Map.entry("senhaAtual", null),
                nova  != null ? Map.entry("novaSenha", nova)   : Map.entry("novaSenha", null),
                conf  != null ? Map.entry("confirmacao", conf) : Map.entry("confirmacao", null)
        );
    }

    @Test
    @DisplayName("Validar save")
    void scenario1() {
        when(clienteService.save(any(ClienteEntity.class))).thenReturn(cliente);

        ResponseEntity<ClienteEntity> response = clienteController.save(cliente);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(cliente, response.getBody());
        verify(clienteService, times(1)).save(cliente);
    }

    @Test
    @DisplayName("Validar findAll")
    void scenario2() {
        List<ClienteEntity> clientes = new ArrayList<>();
        clientes.add(cliente);
        when(clienteService.findAll()).thenReturn(clientes);

        ResponseEntity<List<ClienteEntity>> response = clienteController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(cliente, response.getBody().get(0));
        verify(clienteService, times(1)).findAll();
    }

    @Test
    @DisplayName("Validar findById")
    void scenario3() {
        when(clienteService.findById(1L)).thenReturn(cliente);

        ResponseEntity<ClienteEntity> response = clienteController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cliente, response.getBody());
        verify(clienteService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Validar update")
    void scenario4() {
        ClienteEntity att = new ClienteEntity();
        att.setId(1L);
        att.setNome("TesteAtt");
        att.setEmail("TestAtt@gmail.com");
        att.setSenha("TesteAttSenha");

        when(clienteService.update(eq(1L), any(ClienteEntity.class))).thenReturn(att);

        ResponseEntity<ClienteEntity> response = clienteController.update(1L, att);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(clienteService, times(1)).update(1L, att);
    }

    @Test
    @DisplayName("Validar delete")
    void scenario5() {
        Mockito.doNothing().when(clienteService).delete(1L);

        ResponseEntity<Void> response = clienteController.delete(1l);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clienteService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("Validar findByNome")
    void scenario6() {
        when(clienteService.findByEmail("Edkasper")).thenReturn(Optional.ofNullable(cliente));

        ResponseEntity<ClienteEntity> response = clienteController.findByEmail("Edkasper");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cliente, response.getBody());
        verify(clienteService, times(1)).findByEmail("Edkasper");
    }

    @Test
    @DisplayName("Validar findByEmail")
    void scenario7() {
        when(clienteService.findByEmail("teste@gmail.com")).thenReturn(Optional.ofNullable(cliente));

        ResponseEntity<ClienteEntity> response = clienteController.findByEmail("teste@gmail.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cliente, response.getBody());
        verify(clienteService, times(1)).findByEmail("teste@gmail.com");
    }

    @Test
    @DisplayName("Validar Trocar Senha")
    void scenario8() {
        when(repo.findById(1L)).thenReturn(Optional.ofNullable(cliente));


        ResponseEntity<?> r = clienteController.trocarSenha(1L, Map.of(
                "senhaAtual", "123123",
                "novaSenha", "novaSenha123",
                "confirmacao", "novaSenha123"
        ));

        assertEquals(HttpStatus.NO_CONTENT, r.getStatusCode());
        assertNull(r.getBody());

        ArgumentCaptor<ClienteEntity> captor = ArgumentCaptor.forClass(ClienteEntity.class);
        verify(repo, times(1)).save(captor.capture());
        assertEquals("novaSenha123", captor.getValue().getSenha());
    }

}
