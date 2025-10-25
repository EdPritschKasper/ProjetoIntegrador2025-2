package com.Restaurante.Dove.services;

import com.Restaurante.Dove.model.ClienteEntity;
import com.Restaurante.Dove.repository.ClienteRepository;
import com.Restaurante.Dove.service.ClienteService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


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
    @DisplayName("1. Salvando o cliente com sucesso")
    void save() {
       var cNovo = new ClienteEntity();

       cNovo.setNome("Rogerio");
       cNovo.setEmail("teste@gmail.com");
       cNovo.setSenha("123123123");

        var response = clienteService.save(cNovo);

        Assertions.assertNotNull(response.getId());
        Assertions.assertEquals("Rogerio", response.getNome());
        Assertions.assertEquals("teste@gmail.com", response.getEmail());
        Assertions.assertEquals("123123123", response.getSenha());
    }

    @Test
    @DisplayName("2. Busca o cliente por Id")
    void findById() {
        var found = clienteService.findById(cliente.getId());
        Assertions.assertNotNull(found);
        Assertions.assertEquals(cliente.getNome(), found.getNome());
    }

    @Test
    @DisplayName("3.Deve deletar a conta do cliente")
    void delete() {
        clienteService.delete(cliente.getId());
        var result = clienteRepository.findById(cliente.getId());
        Assertions.assertTrue(result.isEmpty());
    }


}
