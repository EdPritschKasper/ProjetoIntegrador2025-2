package com.Restaurante.Dove.services;

import com.Restaurante.Dove.model.ClienteEntity;
import com.Restaurante.Dove.model.PedidoEntity;
import com.Restaurante.Dove.repository.ClienteRepository;
import com.Restaurante.Dove.service.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class ClienteServiceIntegrationTest {

    @Autowired
    ClienteService clienteService;

    @Autowired
    ClienteRepository clienteRepository;

    ClienteEntity cliente;

    @BeforeEach
    void setUp() {
        clienteRepository.deleteAll();

        cliente = new ClienteEntity();
        cliente.setNome("NomeTeste");
        cliente.setEmail("test@gmail.com");
        cliente.setSenha("1111111");

        clienteRepository.save(cliente);
    }

    @Test
    @DisplayName("1. Deve salvar o cliente com sucesso")
    void save() {
        var novo = new ClienteEntity();
        novo.setNome("Rogerio");
        novo.setEmail("teste@gmail.com");
        novo.setSenha("123123123");

        var response = clienteService.save(novo);

        assertNotNull(response.getId());
        assertEquals("Rogerio", response.getNome());
        assertEquals("teste@gmail.com", response.getEmail());
        assertEquals("123123123", response.getSenha());
    }

    @Test
    @DisplayName("2. Deve lançar exceção ao salvar com nome vazio")
    void saveComNomeVazio() {
        var novo = new ClienteEntity();
        novo.setNome("");
        novo.setEmail("valid@gmail.com");
        novo.setSenha("1234");

        assertThrows(IllegalArgumentException.class, () -> clienteService.save(novo));
    }

//    @Test
//    @DisplayName("3. Deve lançar exceção ao salvar com email inválido")
//    void saveComEmailInvalido() {
//        var novo = new ClienteEntity();
//        novo.setNome("Carlos");
//        novo.setEmail("carlos@empresa.com");
//        novo.setSenha("1234");
//
//        assertThrows(IllegalArgumentException.class, () -> clienteService.save(novo));
//    }

    @Test
    @DisplayName("4. Deve buscar cliente por ID com sucesso")
    void findById() {
        var found = clienteService.findById(cliente.getId());
        assertNotNull(found);
        assertEquals(cliente.getNome(), found.getNome());
    }

    @Test
    @DisplayName("5. Deve lançar exceção ao buscar ID inexistente")
    void findByIdInexistente() {
        assertThrows(EntityNotFoundException.class, () -> clienteService.findById(9999L));
    }

    @Test
    @DisplayName("6. Deve listar todos os clientes")
    void findAll() {
        var result = clienteService.findAll();
        assertEquals(1, result.size());
        assertEquals(cliente.getNome(), result.get(0).getNome());
    }

    @Test
    @DisplayName("7. Deve atualizar nome e senha do cliente")
    void update() {
        var updateData = new ClienteEntity();
        updateData.setNome("NovoNome");
        updateData.setSenha("novaSenha");

        var atualizado = clienteService.update(cliente.getId(), updateData);

        assertEquals("NovoNome", atualizado.getNome());
        assertEquals("novaSenha", atualizado.getSenha());
    }

    @Test
    @DisplayName("8. Deve deletar cliente por ID")
    void delete() {
        clienteService.delete(cliente.getId());
        var result = clienteRepository.findById(cliente.getId());
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("9. Deve encontrar cliente por nome")
    void findByNome() {
        var result = clienteService.findByNome("NomeTeste");
        assertFalse(result.isEmpty());
        assertEquals("NomeTeste", result.get(0).getNome());
    }

    @Test
    @DisplayName("10. Deve encontrar cliente por email")
    void findByEmail() {
        var result = clienteService.findByEmail("test@gmail.com");
        assertTrue(result.isPresent());
        assertEquals(cliente.getNome(), result.get().getNome());
    }

//    @Test
//    @DisplayName("11. Deve retornar tempos médios dos pedidos")
//    void listarTempos() {
//        PedidoEntity pedido = new PedidoEntity();
//        pedido.setHora_inicio(LocalTime.of(10, 0));
//        pedido.setHora_fim(LocalTime.of(10, 30));
//
//        cliente.setPedidos(List.of(pedido));
//        clienteRepository.save(cliente);
//
//        var tempos = clienteService.listarTempos(cliente.getId());
//        assertEquals(1, tempos.size());
//        assertEquals(30, tempos.get(0));
//    }

    @Test
    @DisplayName("12. Deve retornar lista vazia se cliente não tiver pedidos")
    void listarTemposSemPedidos() {
        cliente.setPedidos(List.of());
        clienteRepository.save(cliente);

        var tempos = clienteService.listarTempos(cliente.getId());
        assertTrue(tempos.isEmpty());
    }

    @Test
    @DisplayName("13. Deve lançar exceção ao atualizar cliente inexistente")
    void updateClienteInexistente() {
        var update = new ClienteEntity();
        update.setNome("Novo");
        assertThrows(EntityNotFoundException.class, () -> clienteService.update(999L, update));
    }

}
