package com.Restaurante.Dove.services;

import com.Restaurante.Dove.model.FuncionarioEntity;
import com.Restaurante.Dove.repository.FuncionarioRepository;
import com.Restaurante.Dove.service.FuncionarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Usa H2
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class FuncionarioServiceIntegrationTest {

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    private FuncionarioEntity funcionario;

    @BeforeEach
    void setUp() {
        funcionario = new FuncionarioEntity();
        funcionario.setNome("Bruno Silva");
        funcionario.setCpf("12345678900");
    }


    // SAVE

    @Test
    @DisplayName("Deve salvar funcionário no banco H2")
    void testSave() {
        FuncionarioEntity salvo = funcionarioService.save(funcionario);

        assertNotNull(salvo.getId());
        assertEquals("Bruno Silva", salvo.getNome());

        Optional<FuncionarioEntity> encontrado = funcionarioRepository.findById(salvo.getId());
        assertTrue(encontrado.isPresent());
        assertEquals("12345678900", encontrado.get().getCpf());
    }


    // FIND ALL

    @Test
    @DisplayName("Deve listar funcionários do banco")
    void testFindAll() {
        funcionarioRepository.save(funcionario);

        List<FuncionarioEntity> funcionarios = funcionarioService.findAll();

        assertFalse(funcionarios.isEmpty());
        assertEquals(1, funcionarios.size());
    }


    // FIND BY ID

    @Test
    @DisplayName("Deve encontrar funcionário por ID")
    void testFindById() {
        FuncionarioEntity salvo = funcionarioRepository.save(funcionario);

        FuncionarioEntity encontrado = funcionarioService.findById(salvo.getId());

        assertEquals("Bruno Silva", encontrado.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar ID inexistente")
    void testFindByIdNotFound() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> funcionarioService.findById(999L));
        assertTrue(ex.getMessage().contains("Funcionário não encontrado"));
    }


    // UPDATE

    @Test
    @DisplayName("Deve atualizar funcionário existente")
    void testUpdate() {
        FuncionarioEntity salvo = funcionarioRepository.save(funcionario);

        FuncionarioEntity atualizado = new FuncionarioEntity();
        atualizado.setNome("Novo Nome");
        atualizado.setCpf("99999999999");

        FuncionarioEntity result = funcionarioService.update(salvo.getId(), atualizado);

        assertEquals("Novo Nome", result.getNome());
        assertEquals("99999999999", result.getCpf());
    }


    // DELETE

    @Test
    @DisplayName("Deve deletar funcionário existente")
    void testDelete() {
        FuncionarioEntity salvo = funcionarioRepository.save(funcionario);

        funcionarioService.delete(salvo.getId());

        assertFalse(funcionarioRepository.existsById(salvo.getId()));
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar deletar funcionário inexistente")
    void testDeleteNotFound() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> funcionarioService.delete(999L));
        assertTrue(ex.getMessage().contains("Funcionário não encontrado"));
    }


    // FIND BY NOME / CPF

    @Test
    @DisplayName("Deve buscar funcionário por nome")
    void testFindByNome() {
        funcionarioRepository.save(funcionario);

        List<FuncionarioEntity> result = funcionarioService.findByNome("Bruno Silva");

        assertEquals(1, result.size());
        assertEquals("12345678900", result.get(0).getCpf());
    }

    @Test
    @DisplayName("Deve buscar funcionário por CPF")
    void testFindByCpf() {
        funcionarioRepository.save(funcionario);

        Optional<FuncionarioEntity> result = funcionarioService.findByCpf("12345678900");

        assertTrue(result.isPresent());
        assertEquals("Bruno Silva", result.get().getNome());
    }
}
