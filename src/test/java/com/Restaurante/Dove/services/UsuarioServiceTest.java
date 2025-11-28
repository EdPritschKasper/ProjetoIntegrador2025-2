package com.Restaurante.Dove.services;

import com.Restaurante.Dove.model.PedidoEntity;
import com.Restaurante.Dove.model.TipoUsuario;
import com.Restaurante.Dove.model.UsuarioEntity;
import com.Restaurante.Dove.repository.PedidoRepository;
import com.Restaurante.Dove.repository.UsuarioRepository;
import com.Restaurante.Dove.service.UsuarioService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private UsuarioEntity usuario;
    private PedidoEntity pedido;

    @BeforeEach
    void setUp() {
        usuario = new UsuarioEntity();
        usuario.setId(1L);
        usuario.setNome("Lethicia Marques");
        usuario.setEmail("lethicia.marques@gmail.com");
        usuario.setSenha("senhaAtual123");
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuario.setPedidos(new ArrayList<>());

        pedido = new PedidoEntity();
        pedido.setId(10L);
        pedido.setHora_inicio(LocalTime.of(10, 0));
        pedido.setHora_fim(LocalTime.of(10, 30));
    }

    @Test
    @DisplayName("Deve salvar usuário com sucesso")
    void testSaveSuccess() {
        when(passwordEncoder.encode(usuario.getSenha())).thenReturn("hashedPassword");
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuario);

        UsuarioEntity saved = usuarioService.save(usuario);

        assertNotNull(saved);
        assertEquals(usuario.getNome(), saved.getNome());
        assertEquals("hashedPassword", saved.getSenha());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar usuário sem nome")
    void testSaveFailWithoutNome() {
        usuario.setNome(null);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.save(usuario));
        assertEquals("Você deve preencher o nome", ex.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve buscar todos os usuários")
    void testFindAll() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<UsuarioEntity> result = usuarioService.findAll();

        assertEquals(1, result.size());
        assertEquals(usuario.getNome(), result.get(0).getNome());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar usuário por ID existente")
    void testFindByIdSuccess() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioEntity found = usuarioService.findById(1L);

        assertNotNull(found);
        assertEquals(usuario.getId(), found.getId());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        Mockito.when(usuarioRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(jakarta.persistence.EntityNotFoundException.class, () -> {
            usuarioService.findById(999L);
        });
    }

    @Test
    @DisplayName("Deve buscar usuário por email existente")
    void testFindByEmailSuccess() {
        when(usuarioRepository.findByEmail("lethicia.marques@gmail.com")).thenReturn(Optional.of(usuario));

        Optional<UsuarioEntity> found = usuarioService.findByEmail("lethicia.marques@gmail.com");

        assertTrue(found.isPresent());
        assertEquals(usuario.getEmail(), found.get().getEmail());
        verify(usuarioRepository, times(1)).findByEmail("lethicia.marques@gmail.com");
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void testUpdateSuccess() {
        UsuarioEntity updatedData = new UsuarioEntity();
        updatedData.setNome("Lethicia Atualizada");
        updatedData.setEmail("nova.lethicia@hotmail.com");
        updatedData.setSenha("novaSenha123");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        when(usuarioRepository.save(any(UsuarioEntity.class))).thenAnswer(invocation -> {
            UsuarioEntity entity = invocation.getArgument(0);
            entity.setNome(updatedData.getNome());
            entity.setEmail(updatedData.getEmail());
            return entity;
        });

        UsuarioEntity updated = usuarioService.update(1L, updatedData);

        assertEquals("Lethicia Atualizada", updated.getNome());
        assertEquals("nova.lethicia@hotmail.com", updated.getEmail());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void testUpdateNotFound() {
        Mockito.when(usuarioRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.empty());

        UsuarioEntity usuarioMock = new UsuarioEntity();

        Assertions.assertThrows(jakarta.persistence.EntityNotFoundException.class, () -> {
            usuarioService.update(999L, usuarioMock);
        });
    }

    @Test
    @DisplayName("Deve deletar usuário existente com sucesso")
    void testDeleteSuccess() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        doNothing().when(usuarioRepository).delete(usuario);

        usuarioService.delete(1L);

        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    void testDeleteNotFound() {
        Mockito.when(usuarioRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(jakarta.persistence.EntityNotFoundException.class, () -> {
            usuarioService.delete(999L);
        });
    }

    @Test
    void testGetPedidosById() {
        Mockito.when(usuarioRepository.getPedidosById(Mockito.anyLong())).thenReturn(10L);

        long resultado = usuarioService.getPedidosById(999L);

        Assertions.assertEquals(10L, resultado);
    }

    @Test
    void testGetPedidosByIdNoPedidos() {
        Mockito.when(usuarioRepository.getPedidosById(Mockito.anyLong())).thenReturn(0L);

        long resultado = usuarioService.getPedidosById(999L);

        Assertions.assertEquals(0L, resultado);
    }

    @Test
    void testListarTempos() {
        UsuarioEntity usuarioMock = new UsuarioEntity();
        usuarioMock.setPedidos(java.util.Collections.emptyList());

        Mockito.when(usuarioRepository.listarTempos(Mockito.anyLong())).thenReturn(usuarioMock);

        java.util.List<Integer> resultado = usuarioService.listarTempos(999L);

        Assertions.assertNotNull(resultado);
        Assertions.assertTrue(resultado.isEmpty());
    }
}