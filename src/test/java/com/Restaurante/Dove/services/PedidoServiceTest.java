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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @InjectMocks
    PedidoService pedidoService;

    @Mock PedidoRepository pedidoRepository;
    @Mock CardapioRepository cardapioRepository;
    @Mock UsuarioRepository usuarioRepository;
    @Mock IngredienteRepository ingredienteRepository;

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

    // ---------- SAVE ----------

    @Test
    @DisplayName("Deve lançar exceção se ingrediente não for encontrado")
    void saveIngredienteNotFound() {
        IngredienteEntity ing = new IngredienteEntity();
        ing.setId(3L);
        pedido.setIngredientes(List.of(ing));

        when(cardapioRepository.findById(1)).thenReturn(Optional.of(cardapio));
        when(ingredienteRepository.findById(3)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () -> pedidoService.save(pedido));
        assertTrue(ex.getMessage().contains("Ingrediente não encontrado"));
    }

    @Test
    @DisplayName("Validar save sem cardápio informado")
    void saveWithoutCardapio() {
        pedido.setCardapio(null);
        Exception error = assertThrows(RuntimeException.class, () -> pedidoService.save(pedido));
        assertEquals("Nenhum cardápio informado para o pedido", error.getMessage());
    }

    // ---------- UPDATE ----------
    @Test
    @DisplayName("Deve atualizar pedido com sucesso")
    void updateSuccess() {
        PedidoEntity atualizado = new PedidoEntity();
        atualizado.setMarmita("Média");
        atualizado.setStatus("Finalizado");
        atualizado.setHora_inicio(LocalTime.of(9, 0));
        atualizado.setHora_fim(LocalTime.of(9, 30));
        atualizado.setCardapio(cardapio);

        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedido));
        when(cardapioRepository.findById(1)).thenReturn(Optional.of(cardapio));
        when(pedidoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        PedidoEntity result = pedidoService.update(1L, atualizado);

        assertEquals("Média", result.getMarmita());
        assertEquals("Finalizado", result.getStatus());
        verify(pedidoRepository).save(any(PedidoEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção se pedido não existir ao atualizar")
    void updatePedidoNotFound() {
        when(pedidoRepository.findById(1)).thenReturn(Optional.empty());
        Exception ex = assertThrows(RuntimeException.class, () -> pedidoService.update(1L, pedido));
        assertTrue(ex.getMessage().contains("Pedido não encontrado"));
    }

    @Test
    @DisplayName("Deve lançar exceção se cardápio não for encontrado ao atualizar")
    void updateCardapioNotFound() {
        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedido));
        when(cardapioRepository.findById(1)).thenReturn(Optional.empty());
        Exception ex = assertThrows(RuntimeException.class, () -> pedidoService.update(1L, pedido));
        assertTrue(ex.getMessage().contains("Cardapio não encontrado"));
    }

    @Test
    @DisplayName("Deve lançar exceção se ingrediente não for encontrado ao atualizar")
    void updateIngredienteNotFound() {
        IngredienteEntity ing = new IngredienteEntity();
        ing.setId(3L);
        pedido.setIngredientes(List.of(ing));

        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedido));
        when(cardapioRepository.findById(1)).thenReturn(Optional.of(cardapio));
        when(ingredienteRepository.findById(3)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () -> pedidoService.update(1L, pedido));
        assertTrue(ex.getMessage().contains("Ingrediente não encontrado"));
    }

    // ---------- FIND ----------
    @Test
    @DisplayName("Validar findAll")
    void findAll() {
        when(pedidoRepository.findAll()).thenReturn(List.of(pedido));
        List<PedidoEntity> result = pedidoService.findAll();
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Validar findById existente")
    void findByIdSuccess() {
        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedido));
        PedidoEntity result = pedidoService.findById(1L);
        assertEquals(pedido, result);
    }

    @Test
    @DisplayName("Validar findById inexistente")
    void findByIdNotFound() {
        when(pedidoRepository.findById(1)).thenReturn(Optional.empty());
        Exception ex = assertThrows(RuntimeException.class, () -> pedidoService.findById(1L));
        assertTrue(ex.getMessage().contains("Pedido não encontrado"));
    }

    // ---------- DELETE ----------
    @Test
    @DisplayName("Validar delete existente")
    void deleteSuccess() {
        when(pedidoRepository.existsById(1)).thenReturn(true);
        pedidoService.delete(1L);
        verify(pedidoRepository).deleteById(1);
    }

    @Test
    @DisplayName("Validar delete inexistente")
    void deleteNotFound() {
        when(pedidoRepository.existsById(1)).thenReturn(false);
        Exception ex = assertThrows(RuntimeException.class, () -> pedidoService.delete(1L));
        assertTrue(ex.getMessage().contains("Pedido não encontrado"));
    }

    // ---------- FIND BY ----------
    @Test
    @DisplayName("Validar findByCardapio existente")
    void findByCardapio() {
        when(cardapioRepository.findById(1)).thenReturn(Optional.of(cardapio));
        when(pedidoRepository.findByCardapio(cardapio)).thenReturn(List.of(pedido));
        List<PedidoEntity> result = pedidoService.findByCardapio(cardapio);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Validar findByCardapio inexistente")
    void findByCardapioNotFound() {
        when(cardapioRepository.findById(1)).thenReturn(Optional.empty());
        Exception ex = assertThrows(RuntimeException.class, () -> pedidoService.findByCardapio(cardapio));
        assertTrue(ex.getMessage().contains("Cardapio não encontrado"));
    }

    @Test
    @DisplayName("Validar findByStatus")
    void findByStatus() {
        when(pedidoRepository.findByStatus("Pendente")).thenReturn(List.of(pedido));
        List<PedidoEntity> result = pedidoService.findByStatus("Pendente");
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Validar contarPedidosPorData")
    void contarPedidosPorData() {
        when(pedidoRepository.contarPedidosPorData(any())).thenReturn(5);
        assertEquals(5, pedidoService.contarPedidosPorData(LocalDate.now()));
    }

    @Test
    @DisplayName("Validar mediaPedidosPorMes")
    void mediaPedidosPorMes() {
        when(pedidoRepository.mediaPedidosPorMes(10)).thenReturn(12.5);
        assertEquals(12.5, pedidoService.mediaPedidosPorMes(10));
    }
}
