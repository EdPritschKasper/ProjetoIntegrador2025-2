package com.Restaurante.Dove.controllers;

import com.Restaurante.Dove.controller.UsuarioController;
import com.Restaurante.Dove.model.TipoUsuario;
import com.Restaurante.Dove.model.UsuarioEntity;
import com.Restaurante.Dove.repository.UsuarioRepository;
import com.Restaurante.Dove.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UsuarioControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioController usuarioController;

    private UsuarioEntity usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        usuario = new UsuarioEntity();
        usuario.setId(1L);
        usuario.setNome("Lethicia Marques");
        usuario.setEmail("lethicia.marques@teste.com");
        usuario.setSenha("senhaAtual123");
        usuario.setUsername("lethicia.marques");
        usuario.setTipo(TipoUsuario.CLIENTE);
    }

    @Test
    @DisplayName("GET /api/usuarios - Deve retornar lista de usuários")
    void testFindAll() throws Exception {
        when(usuarioService.findAll()).thenReturn(List.of(usuario));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value(usuario.getNome()));

        verify(usuarioService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/usuarios/{id} - Deve buscar usuário por ID")
    void testFindById() throws Exception {
        when(usuarioService.findById(1L)).thenReturn(usuario);

        mockMvc.perform(get("/api/usuarios/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(usuario.getId()))
                .andExpect(jsonPath("$.nome").value(usuario.getNome()));

        verify(usuarioService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("POST /api/usuarios - Deve salvar usuário com sucesso")
    void testSave() throws Exception {
        when(usuarioService.save(any(UsuarioEntity.class))).thenReturn(usuario);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(usuario.getId()))
                .andExpect(jsonPath("$.email").value(usuario.getEmail()));

        verify(usuarioService, times(1)).save(any(UsuarioEntity.class));
    }

    @Test
    @DisplayName("PUT /api/usuarios/{id} - Deve atualizar usuário existente")
    void testUpdate() throws Exception {
        UsuarioEntity usuarioAtualizado = new UsuarioEntity();
        usuarioAtualizado.setId(1L);
        usuarioAtualizado.setNome("Lethicia Marques Atualizada");
        usuarioAtualizado.setEmail("lethicia.marques.atualizada@teste.com");

        when(usuarioService.update(eq(1L), any(UsuarioEntity.class))).thenReturn(usuarioAtualizado);

        mockMvc.perform(put("/api/usuarios/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Lethicia Marques Atualizada"));

        verify(usuarioService, times(1)).update(eq(1L), any(UsuarioEntity.class));
    }

    @Test
    @DisplayName("DELETE /api/usuarios/{id} - Deve deletar usuário")
    void testDelete() throws Exception {
        doNothing().when(usuarioService).delete(1L);

        mockMvc.perform(delete("/api/usuarios/{id}", 1))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("GET /api/usuarios/findByNome?nome=Lethicia Marques - Deve buscar usuário por nome")
    void testFindByNome() throws Exception {
        when(usuarioService.findAll()).thenReturn(List.of(usuario));

        mockMvc.perform(get("/api/usuarios/findByNome")
                        .param("nome", "Lethicia Marques"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value(usuario.getNome()));

        verify(usuarioService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/usuarios/findByEmail?email=lethicia.marques@teste.com - Deve buscar usuário por email")
    void testFindByEmail_sucesso() throws Exception {
        when(usuarioService.findByEmail("lethicia.marques@teste.com")).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/usuarios/findByEmail")
                        .param("email", "lethicia.marques@teste.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(usuario.getEmail()));

        verify(usuarioService, times(1)).findByEmail("lethicia.marques@teste.com");
    }

    @Test
    @DisplayName("GET /api/usuarios/findByEmail - Deve retornar 404 se email não encontrado")
    void testFindByEmail_naoEncontrado() throws Exception {
        when(usuarioService.findByEmail("nao.existe@teste.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/findByEmail")
                        .param("email", "nao.existe@teste.com"))
                .andExpect(status().isNotFound());

        verify(usuarioService, times(1)).findByEmail("nao.existe@teste.com");
    }

    @Test
    @DisplayName("GET /api/usuarios/findPedido/{id} - Deve retornar o ID do Pedido")
    void testFindPedidoById() {
        Long pedidoIdEsperado = 5L;
        when(usuarioService.getPedidosById(1L)).thenReturn(pedidoIdEsperado);

        ResponseEntity<Long> response = usuarioController.findPedidoById(1L);

        assertEquals(org.springframework.http.HttpStatus.OK, response.getStatusCode());
        assertEquals(pedidoIdEsperado, response.getBody());
        verify(usuarioService, times(1)).getPedidosById(1L);
    }

    @Test
    @DisplayName("GET /api/usuarios/listar-tempos/{id} - Deve listar tempos")
    void testListarTempos() {
        List<Integer> temposEsperados = List.of(30, 45, 60);
        when(usuarioService.listarTempos(1L)).thenReturn(temposEsperados);

        ResponseEntity<List<Integer>> response = usuarioController.listarTempos(1L);

        assertEquals(org.springframework.http.HttpStatus.OK, response.getStatusCode());
        assertEquals(temposEsperados, response.getBody());
        verify(usuarioService, times(1)).listarTempos(1L);
    }

    @Test
    @DisplayName("PUT /api/usuarios/senha/{id} - Troca de senha bem-sucedida (204)")
    void testTrocarSenha_sucesso() throws Exception {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Map<String, String> body = Map.of(
                "senhaAtual", "senhaAtual123",
                "novaSenha", "novaSenha456",
                "confirmacao", "novaSenha456"
        );

        mockMvc.perform(put("/api/usuarios/senha/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNoContent());

        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(any(UsuarioEntity.class));
    }

    @Test
    @DisplayName("PUT /api/usuarios/senha/{id} - Falha: Usuário não encontrado (404)")
    void testTrocarSenha_usuarioNaoEncontrado() throws Exception {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        Map<String, String> body = Map.of(
                "senhaAtual", "qualquer",
                "novaSenha", "novaSenha456",
                "confirmacao", "novaSenha456"
        );

        mockMvc.perform(put("/api/usuarios/senha/{id}", 99)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Usuário não encontrado."));

        verify(usuarioRepository, times(1)).findById(99L);
        verify(usuarioRepository, never()).save(any(UsuarioEntity.class));
    }

    @Test
    @DisplayName("PUT /api/usuarios/senha/{id} - Falha: Senha atual incorreta (401)")
    void testTrocarSenha_senhaAtualIncorreta() throws Exception {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Map<String, String> body = Map.of(
                "senhaAtual", "senhaIncorreta",
                "novaSenha", "novaSenha456",
                "confirmacao", "novaSenha456"
        );

        mockMvc.perform(put("/api/usuarios/senha/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Senha atual incorreta."));

        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, never()).save(any(UsuarioEntity.class));
    }

    @Test
    @DisplayName("PUT /api/usuarios/senha/{id} - Falha: Nova senha e confirmação não coincidem (400)")
    void testTrocarSenha_senhasNaoCoincidem() throws Exception {
        Map<String, String> body = Map.of(
                "senhaAtual", "senhaAtual123",
                "novaSenha", "novaSenha456",
                "confirmacao", "naoCoincide"
        );

        mockMvc.perform(put("/api/usuarios/senha/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("As senhas não coincidem."));

        verify(usuarioRepository, never()).findById(anyLong());
        verify(usuarioRepository, never()).save(any(UsuarioEntity.class));
    }

    @Test
    @DisplayName("PUT /api/usuarios/senha/{id} - Falha: Nova senha muito curta (400)")
    void testTrocarSenha_senhaMuitoCurta() throws Exception {
        Map<String, String> body = Map.of(
                "senhaAtual", "senhaAtual123",
                "novaSenha", "curta",
                "confirmacao", "curta"
        );

        mockMvc.perform(put("/api/usuarios/senha/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("A nova senha deve possuir ao menos 8 caracteres."));

        verify(usuarioRepository, never()).findById(anyLong());
        verify(usuarioRepository, never()).save(any(UsuarioEntity.class));
    }

}