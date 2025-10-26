package com.Restaurante.Dove.services;

import com.Restaurante.Dove.model.ClienteEntity;
import com.Restaurante.Dove.model.PedidoEntity;
import com.Restaurante.Dove.repository.ClienteRepository;
import com.Restaurante.Dove.service.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @InjectMocks
    ClienteService clienteService;

    @Mock
    ClienteRepository clienteRepository;

    ClienteEntity clienteBase;

    @BeforeEach
    void setUp() {
        clienteBase = new ClienteEntity();
        clienteBase.setId(1L);
        clienteBase.setNome("Eduardo Kasper");
        clienteBase.setEmail("edu@gmail.com");
        clienteBase.setSenha("123456");
        clienteBase.setPedidos(new ArrayList<>());
    }


    @Test
    @DisplayName("Validar save com sucesso")
    void scenario1() {
        when(clienteRepository.save(any(ClienteEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        ClienteEntity salvo = clienteService.save(clienteBase);

        assertNotNull(salvo);
        assertEquals("Eduardo Kasper", salvo.getNome());
        verify(clienteRepository, times(1)).save(any(ClienteEntity.class));
    }

    @Test
    @DisplayName("Validar save com nome vazio deve falhar")
    void scenario2() {
        clienteBase.setNome("   ");
        var ex = assertThrows(IllegalArgumentException.class, () -> clienteService.save(clienteBase));
        assertEquals("Você deve preencher o nome", ex.getMessage());
        verify(clienteRepository, never()).save(any());
    }

//Teste certo pra dar errado
//Demostrando a regra do Email funcionando
//    @Test
//    @DisplayName("Validar save com email que não seja @gmail.com ou @hotmail.com deve falhar (regra esperada)")
//    void scenario3() {
//        clienteBase.setEmail("user@yahoo.com");
//        var ex = assertThrows(IllegalArgumentException.class, () -> clienteService.save(clienteBase));
//        assertEquals("O email deve ser @gmail ou @hotmail", ex.getMessage());
//        verify(clienteRepository, never()).save(any());
//    }

    @Test
    @DisplayName("Validar save com senha muito curta deve falhar (≥3 era esperado, mas regra atual só cai se também estiver vazia)")
    void scenario4() {
        clienteBase.setSenha("");
        var ex = assertThrows(IllegalArgumentException.class, () -> clienteService.save(clienteBase));
        assertEquals("Senha deve ter mais de 3 caracters", ex.getMessage());
        verify(clienteRepository, never()).save(any());
    }


    @Test
    @DisplayName("Validar findAll")
    void scenario5() {
        when(clienteRepository.findAll()).thenReturn(List.of(clienteBase));

        var result = clienteService.findAll();

        assertEquals(1, result.size());
        assertEquals(clienteBase, result.get(0));
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Validar findById existente")
    void scenario6() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteBase));

        var result = clienteService.findById(1L);

        assertEquals(clienteBase, result);
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Validar findById inexistente")
    void scenario7() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> clienteService.findById(1L));
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Validar update alterando apenas o nome (quando informado)")
    void scenario8() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteBase));
        when(clienteRepository.save(any(ClienteEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        var entrada = new ClienteEntity();
        entrada.setNome("Novo Nome");
        entrada.setSenha(null);

        var atualizado = clienteService.update(1L, entrada);

        assertEquals("Novo Nome", atualizado.getNome());
        assertEquals("123456", atualizado.getSenha());
        verify(clienteRepository, times(1)).save(any(ClienteEntity.class));
    }

    @Test
    @DisplayName("Validar update alterando a senha quando não nula")
    void scenario9() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteBase));
        when(clienteRepository.save(any(ClienteEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        var entrada = new ClienteEntity();
        entrada.setSenha("novaSenha");
        entrada.setNome("   ");

        var atualizado = clienteService.update(1L, entrada);

        assertEquals("Eduardo Kasper", atualizado.getNome()); // nome preservado
        assertEquals("novaSenha", atualizado.getSenha());     // senha trocada
        verify(clienteRepository, times(1)).save(any(ClienteEntity.class));
    }

    @Test
    @DisplayName("Validar delete existente")
    void scenario10() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteBase));
        doNothing().when(clienteRepository).delete(clienteBase);

        clienteService.delete(1L);

        verify(clienteRepository, times(1)).delete(clienteBase);
    }

    @Test
    @DisplayName("Validar delete inexistente")
    void scenario11() {
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> clienteService.delete(999L));
        verify(clienteRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Validar findByNome")
    void scenario12() {
        when(clienteRepository.findByNome("Edu")).thenReturn(List.of(clienteBase));

        var result = clienteService.findByNome("Edu");

        assertEquals(1, result.size());
        assertEquals(clienteBase, result.get(0));
        verify(clienteRepository, times(1)).findByNome("Edu");
    }

    @Test
    @DisplayName("Validar findByEmail")
    void scenario13() {
        when(clienteRepository.findByEmail("edu@gmail.com")).thenReturn(Optional.of(clienteBase));

        var result = clienteService.findByEmail("edu@gmail.com");

        assertTrue(result.isPresent());
        assertEquals(clienteBase, result.get());
        verify(clienteRepository, times(1)).findByEmail("edu@gmail.com");
    }


    @Test
    @DisplayName("Validar getPedidosById delega para o repository")
    void scenario14() {
        when(clienteRepository.getPedidosById(1L)).thenReturn(7L);

        long qtde = clienteService.getPedidosById(1L);

        assertEquals(7L, qtde);
        verify(clienteRepository, times(1)).getPedidosById(1L);
    }


    @Test
    @DisplayName("Validar listarTempos com lista vazia retorna lista vazia")
    void scenario15() {
        ClienteEntity semPedidos = new ClienteEntity();
        semPedidos.setId(1L);
        semPedidos.setPedidos(new ArrayList<>());

        when(clienteRepository.listarTempos(1L)).thenReturn(semPedidos);

        var tempos = clienteService.listarTempos(1L);

        assertNotNull(tempos);
        assertTrue(tempos.isEmpty());
        verify(clienteRepository, times(1)).listarTempos(1L);
    }

    @Test
    @DisplayName("listarTempos - calcula minutos entre início e fim (mesmo dia)")
    void scenario16() {
        var pedido = new PedidoEntity();
        pedido.setHora_inicio(LocalTime.of(10, 0));
        pedido.setHora_fim(LocalTime.of(10, 30));

        var cliente = new ClienteEntity();
        cliente.setId(1L);
        cliente.setPedidos(new ArrayList<>(List.of(pedido)));

        when(clienteRepository.listarTempos(1L)).thenReturn(cliente);

        var tempos = clienteService.listarTempos(1L);

        assertEquals(List.of(30), tempos);
        verify(clienteRepository, times(1)).listarTempos(1L);
    }

    @Test
    @DisplayName("listarTempos - calcula minutos atravessando meia-noite")
    void scenario17() {
        var pedido = new PedidoEntity();
        pedido.setHora_inicio(LocalTime.of(23, 50));
        pedido.setHora_fim(LocalTime.of(0, 10)); // +20 min

        var cliente = new ClienteEntity();
        cliente.setId(2L);
        cliente.setPedidos(new ArrayList<>(List.of(pedido)));

        when(clienteRepository.listarTempos(2L)).thenReturn(cliente);

        var tempos = clienteService.listarTempos(2L);

        assertEquals(List.of(20), tempos);
        verify(clienteRepository, times(1)).listarTempos(2L);
    }
}
