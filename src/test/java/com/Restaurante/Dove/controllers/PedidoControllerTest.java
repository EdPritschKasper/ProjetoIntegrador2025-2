package com.Restaurante.Dove.controllers;

import com.Restaurante.Dove.controller.PedidoController;
import com.Restaurante.Dove.model.CardapioEntity;
import com.Restaurante.Dove.model.PedidoEntity;
import com.Restaurante.Dove.service.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class PedidoControllerTest {

    @InjectMocks
    PedidoController pedidoController;

    @Mock
    PedidoService pedidoService;

    PedidoEntity pedido;
    CardapioEntity cardapio;

    @BeforeEach
    void setUp() {
        cardapio = new CardapioEntity();
        cardapio.setId(1L);

        pedido = new PedidoEntity();
        pedido.setId(1L);
        pedido.setMarmita("Grande");
        pedido.setStatus("Pronto");
        pedido.setHora_inicio(LocalTime.of(10, 0));
        pedido.setHora_fim(LocalTime.of(10, 30));
        pedido.setCardapio(cardapio);
    }

    @Test
    @DisplayName("Validar save")
    void scenario1() {
        Mockito.when(pedidoService.save(any(PedidoEntity.class))).thenReturn(pedido);

        ResponseEntity<PedidoEntity> response = pedidoController.save(pedido);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(pedido, response.getBody());
        Mockito.verify(pedidoService, Mockito.times(1)).save(pedido);
    }

    @Test
    @DisplayName("Validar findAll")
    void scenario2() {
        List<PedidoEntity> pedidos = new ArrayList<>();
        pedidos.add(pedido);
        Mockito.when(pedidoService.findAll()).thenReturn(pedidos);

        ResponseEntity<List<PedidoEntity>> response = pedidoController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(pedido, response.getBody().get(0));
        Mockito.verify(pedidoService, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Validar findById")
    void scenario3() {
        Mockito.when(pedidoService.findById(1L)).thenReturn(pedido);

        ResponseEntity<PedidoEntity> response = pedidoController.findById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pedido, response.getBody());
        Mockito.verify(pedidoService, Mockito.times(1)).findById(1L);
    }

    @Test
    @DisplayName("Validar update")
    void scenario4() {
        PedidoEntity atualizado = new PedidoEntity();
        atualizado.setId(1L);
        atualizado.setMarmita("Pequena");
        atualizado.setCardapio(cardapio);

        Mockito.when(pedidoService.update(eq(1L), any(PedidoEntity.class))).thenReturn(atualizado);

        ResponseEntity<PedidoEntity> response = pedidoController.update(1, atualizado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pequena", response.getBody().getMarmita());
        Mockito.verify(pedidoService, Mockito.times(1)).update(1L, atualizado);
    }

    @Test
    @DisplayName("Validar delete")
    void scenario5() {
        Mockito.doNothing().when(pedidoService).delete(1L);

        ResponseEntity<Void> response = pedidoController.delete(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Mockito.verify(pedidoService, Mockito.times(1)).delete(1L);
    }

    @Test
    @DisplayName("Validar findByCardapio")
    void scenario6() {
        List<PedidoEntity> pedidos = new ArrayList<>();
        pedidos.add(pedido);
        Mockito.when(pedidoService.findByCardapio(cardapio)).thenReturn(pedidos);

        ResponseEntity<List<PedidoEntity>> response = pedidoController.findByCardapio(cardapio);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pedido, response.getBody().get(0));
        Mockito.verify(pedidoService, Mockito.times(1)).findByCardapio(cardapio);
    }

    @Test
    @DisplayName("Validar findByStatus")
    void scenario7() {
        List<PedidoEntity> pedidos = new ArrayList<>();
        pedidos.add(pedido);
        Mockito.when(pedidoService.findByStatus("Pronto")).thenReturn(pedidos);

        ResponseEntity<List<PedidoEntity>> response = pedidoController.findByStatus("Pronto");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        Mockito.verify(pedidoService, Mockito.times(1)).findByStatus("Pronto");
    }

    @Test
    @DisplayName("Validar contarPedidos")
    void scenario8() {
        Mockito.when(pedidoService.contarPedidosPorData(any(LocalDate.class))).thenReturn(10);

        ResponseEntity<Integer> response = pedidoController.contarPedidos(LocalDate.now());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(10, response.getBody());
        Mockito.verify(pedidoService, Mockito.times(1)).contarPedidosPorData(any(LocalDate.class));
    }

    @Test
    @DisplayName("Validar mediaPedidosMes")
    void scenario9() {
        Mockito.when(pedidoService.mediaPedidosPorMes(5)).thenReturn(12.5);

        ResponseEntity<Double> response = pedidoController.mediaPedidosMes(5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(12.5, response.getBody());
        Mockito.verify(pedidoService, Mockito.times(1)).mediaPedidosPorMes(5);
    }
}
