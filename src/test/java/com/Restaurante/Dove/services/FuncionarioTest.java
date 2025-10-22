package com.Restaurante.Dove.services;

import com.Restaurante.Dove.model.FuncionarioEntity;
import com.Restaurante.Dove.model.PedidoEntity;
import com.Restaurante.Dove.repository.FuncionarioRepository;
import com.Restaurante.Dove.service.FuncionarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FuncionarioTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @InjectMocks
    private FuncionarioService funcionarioService;

    private FuncionarioEntity funcionario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        funcionario = new FuncionarioEntity();
        funcionario.setId(1L);
        funcionario.setNome("Bruno Silva");
        funcionario.setCpf("12345678900");
        funcionario.setPedidos(new ArrayList<>());
    }


    // SAVE

    @Test
    @DisplayName("Deve salvar funcionário com sucesso")
    void testSaveSuccess() {
        when(funcionarioRepository.save(any(FuncionarioEntity.class))).thenReturn(funcionario);

        FuncionarioEntity result = funcionarioService.save(funcionario);

        assertNotNull(result);
        assertEquals("Bruno Silva", result.getNome());
        verify(funcionarioRepository, times(1)).save(any(FuncionarioEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar funcionário nulo")
    void testSaveNullFuncionario() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> funcionarioService.save(null));
        assertEquals("Funcionário não pode ser nulo.", ex.getMessage());
        verify(funcionarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome for vazio")
    void testSaveNomeVazio() {
        funcionario.setNome(" ");
        RuntimeException ex = assertThrows(RuntimeException.class, () -> funcionarioService.save(funcionario));
        assertEquals("Nome do funcionário é obrigatório.", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF for vazio")
    void testSaveCpfVazio() {
        funcionario.setCpf(" ");
        RuntimeException ex = assertThrows(RuntimeException.class, () -> funcionarioService.save(funcionario));
        assertEquals("CPF do funcionário é obrigatório.", ex.getMessage());
    }


    // FIND ALL

    @Test
    @DisplayName("Deve retornar lista de funcionários")
    void testFindAll() {
        when(funcionarioRepository.findAll()).thenReturn(List.of(funcionario));

        List<FuncionarioEntity> result = funcionarioService.findAll();

        assertEquals(1, result.size());
        verify(funcionarioRepository, times(1)).findAll();
    }


    // FIND BY ID

    @Test
    @DisplayName("Deve retornar funcionário por ID")
    void testFindByIdSuccess() {
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));

        FuncionarioEntity result = funcionarioService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao não encontrar funcionário por ID")
    void testFindByIdNotFound() {
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> funcionarioService.findById(1L));
        assertEquals("Funcionário não encontrado com id: 1", ex.getMessage());
    }


    // UPDATE

    @Test
    @DisplayName("Deve atualizar funcionário com sucesso")
    void testUpdateSuccess() {
        FuncionarioEntity novo = new FuncionarioEntity();
        novo.setNome("Novo Nome");
        novo.setCpf("11122233344");

        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));
        when(funcionarioRepository.save(any(FuncionarioEntity.class))).thenReturn(novo);

        FuncionarioEntity result = funcionarioService.update(1L, novo);

        assertEquals("Novo Nome", result.getNome());
        assertEquals("11122233344", result.getCpf());
        verify(funcionarioRepository, times(1)).save(any(FuncionarioEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar funcionário inexistente")
    void testUpdateNotFound() {
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> funcionarioService.update(1L, funcionario));
        assertEquals("Funcionário não encontrado com id: 1", ex.getMessage());
    }


    // DELETE

    @Test
    @DisplayName("Deve deletar funcionário existente")
    void testDeleteSuccess() {
        when(funcionarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(funcionarioRepository).deleteById(1L);

        funcionarioService.delete(1L);

        verify(funcionarioRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar funcionário inexistente")
    void testDeleteNotFound() {
        when(funcionarioRepository.existsById(1L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> funcionarioService.delete(1L));
        assertEquals("Funcionário não encontrado com id: 1", ex.getMessage());
    }


    // RELATÓRIO DE PEDIDOS

    @Test
    @DisplayName("Deve gerar relatório de pedidos do funcionário")
    void testGerarRelatorioPedidos() {
        PedidoEntity pedido = new PedidoEntity();
        pedido.setId(1L);
        pedido.setStatus("FINALIZADO");
        pedido.setHora_inicio(LocalTime.now().minusHours(1));
        pedido.setHora_fim(LocalTime.from(LocalDateTime.now()));

        funcionario.getPedidos().add(pedido);

        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));

        Map<String, Object> relatorio = funcionarioService.gerarRelatorioPedidos(1L);

        assertEquals("Bruno Silva", relatorio.get("funcionarioNome"));
        assertEquals(1, relatorio.get("quantidadePedidos"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao gerar relatório de funcionário inexistente")
    void testGerarRelatorioFuncionarioNotFound() {
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> funcionarioService.gerarRelatorioPedidos(1L));
        assertEquals("Funcionário não encontrado com id: 1", ex.getMessage());
    }

    // FUNCIONÁRIO COM MAIS PEDIDOS

    @Test
    @DisplayName("Deve retornar funcionário com mais pedidos")
    void testFuncionarioMaisPedidos() {
        FuncionarioEntity f1 = new FuncionarioEntity();
        f1.setNome("João");
        f1.setCpf("111");
        f1.setPedidos(List.of(new PedidoEntity(), new PedidoEntity()));

        FuncionarioEntity f2 = new FuncionarioEntity();
        f2.setNome("Maria");
        f2.setCpf("222");
        f2.setPedidos(List.of(new PedidoEntity()));

        when(funcionarioRepository.findAll()).thenReturn(List.of(f1, f2));

        Map<String, Object> resultado = funcionarioService.funcionarioMaisPedidos();

        assertEquals("João", resultado.get("funcionarioNome"));
        assertEquals(2, resultado.get("quantidadePedidos"));
    }

    @Test
    @DisplayName("Deve lançar exceção se não houver funcionários")
    void testFuncionarioMaisPedidosVazio() {
        when(funcionarioRepository.findAll()).thenReturn(Collections.emptyList());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> funcionarioService.funcionarioMaisPedidos());
        assertEquals("Nenhum funcionário encontrado", ex.getMessage());
    }


    // FIND BY NOME / CPF

    @Test
    @DisplayName("Deve buscar funcionário por nome")
    void testFindByNome() {
        when(funcionarioRepository.findByNome("Bruno")).thenReturn(List.of(funcionario));

        List<FuncionarioEntity> result = funcionarioService.findByNome("Bruno");

        assertEquals(1, result.size());
        verify(funcionarioRepository, times(1)).findByNome("Bruno");
    }

    @Test
    @DisplayName("Deve buscar funcionário por CPF")
    void testFindByCpf() {
        when(funcionarioRepository.findByCpf("12345678900")).thenReturn(Optional.of(funcionario));

        Optional<FuncionarioEntity> result = funcionarioService.findByCpf("12345678900");

        assertTrue(result.isPresent());
        assertEquals("12345678900", result.get().getCpf());
    }
}
