package com.Restaurante.Dove.services;

import com.Restaurante.Dove.model.*;
import com.Restaurante.Dove.repository.*;
import com.Restaurante.Dove.service.PedidoService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @InjectMocks
    PedidoService pedidoService;

    @Mock
    PedidoRepository pedidoRepository;
    @Mock
    CardapioRepository cardapioRepository;
    @Mock
    FuncionarioRepository funcionarioRepository;
    @Mock
    ClienteRepository clienteRepository;
    @Mock
    IngredienteRepository ingredienteRepository;

    PedidoEntity pedido;
    CardapioEntity cardapio;

    @BeforeEach
    void setUp() {
        cardapio = new CardapioEntity();
        cardapio.setId(1L);

        pedido = new PedidoEntity();
        pedido.setId(1L);
        pedido.setMarmita("Grande");
        pedido.setStatus("Pendente");
        pedido.setHora_inicio(LocalTime.of(10, 0));
        pedido.setHora_fim(LocalTime.of(10, 30));
        pedido.setCardapio(cardapio);
    }

    @Test
    @DisplayName("Validar save com sucesso")
    void scenario1() {
        Mockito.when(cardapioRepository.findById(1)).thenReturn(Optional.of(cardapio));
        Mockito.when(pedidoRepository.save(pedido)).thenReturn(pedido);

        PedidoEntity result = pedidoService.save(pedido);

        assertEquals(pedido, result);
        Mockito.verify(pedidoRepository, Mockito.times(1)).save(pedido);
    }

    @Test
    @DisplayName("Validar save sem cardápio informado")
    void scenario2() {
        pedido.setCardapio(null);
        Exception error = assertThrows(RuntimeException.class, () -> pedidoService.save(pedido));

        assertEquals("Nenhum cardápio informado para o pedido", error.getMessage());
    }

    @Test
    @DisplayName("Validar findAll")
    void scenario3() {
        List<PedidoEntity> pedidos = List.of(pedido);
        Mockito.when(pedidoRepository.findAll()).thenReturn(pedidos);

        List<PedidoEntity> result = pedidoService.findAll();

        assertEquals(1, result.size());
        assertEquals(pedido, result.get(0));
        Mockito.verify(pedidoRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Validar findById existente")
    void scenario4() {
        Mockito.when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedido));

        PedidoEntity result = pedidoService.findById(1L);

        assertEquals(pedido, result);
        Mockito.verify(pedidoRepository, Mockito.times(1)).findById(1);
    }

    @Test
    @DisplayName("Validar findById inexistente")
    void scenario5() {
        Mockito.when(pedidoRepository.findById(1)).thenReturn(Optional.empty());

        Exception error = assertThrows(RuntimeException.class, () -> pedidoService.findById(1L));

        assertTrue(error.getMessage().contains("Pedido não encontrado"));
    }

    @Test
    @DisplayName("Validar delete existente")
    void scenario6() {
        Mockito.when(pedidoRepository.existsById(1)).thenReturn(true);
        Mockito.doNothing().when(pedidoRepository).deleteById(1);

        pedidoService.delete(1L);

        Mockito.verify(pedidoRepository, Mockito.times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Validar delete inexistente")
    void scenario7() {
        Mockito.when(pedidoRepository.existsById(1)).thenReturn(false);

        Exception error = assertThrows(RuntimeException.class, () -> pedidoService.delete(1L));

        assertTrue(error.getMessage().contains("Pedido não encontrado"));
    }

    @Test
    @DisplayName("Validar findByStatus")
    void scenario8() {
        List<PedidoEntity> pedidos = List.of(pedido);
        Mockito.when(pedidoRepository.findByStatus("Pendente")).thenReturn(pedidos);

        List<PedidoEntity> result = pedidoService.findByStatus("Pendente");

        assertEquals(1, result.size());
        assertEquals(pedido, result.get(0));
        Mockito.verify(pedidoRepository, Mockito.times(1)).findByStatus("Pendente");
    }

    @Test
    @DisplayName("Validar contarPedidosPorData")
    void scenario9() {
        Mockito.when(pedidoRepository.contarPedidosPorData(any(LocalDate.class))).thenReturn(5);

        int result = pedidoService.contarPedidosPorData(LocalDate.now());

        assertEquals(5, result);
        Mockito.verify(pedidoRepository, Mockito.times(1)).contarPedidosPorData(any(LocalDate.class));
    }

    @Test
    @DisplayName("Validar mediaPedidosPorMes")
    void scenario10() {
        Mockito.when(pedidoRepository.mediaPedidosPorMes(10)).thenReturn(12.5);

        Double result = pedidoService.mediaPedidosPorMes(10);

        assertEquals(12.5, result);
        Mockito.verify(pedidoRepository, Mockito.times(1)).mediaPedidosPorMes(10);
    }

    @Test
    @DisplayName("Validar findByCardapio existente")
    void scenario11() {
        List<PedidoEntity> pedidos = List.of(pedido);
        Mockito.when(cardapioRepository.findById(1)).thenReturn(Optional.of(cardapio));
        Mockito.when(pedidoRepository.findByCardapio(cardapio)).thenReturn(pedidos);

        List<PedidoEntity> result = pedidoService.findByCardapio(cardapio);

        assertEquals(1, result.size());
        Mockito.verify(pedidoRepository, Mockito.times(1)).findByCardapio(cardapio);
    }

    @Test
    @DisplayName("Validar findByCardapio inexistente")
    void scenario12() {
        Mockito.when(cardapioRepository.findById(1)).thenReturn(Optional.empty());

        Exception error = assertThrows(RuntimeException.class, () -> pedidoService.findByCardapio(cardapio));

        assertTrue(error.getMessage().contains("Cardapio não encontrado"));
    }
}
