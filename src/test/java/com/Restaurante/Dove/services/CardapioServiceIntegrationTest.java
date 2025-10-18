package com.Restaurante.Dove.services;

import com.Restaurante.Dove.model.CardapioEntity;
import com.Restaurante.Dove.repository.CardapioRepository;
import com.Restaurante.Dove.repository.IngredienteRepository;
import com.Restaurante.Dove.repository.PedidoRepository;
import com.Restaurante.Dove.service.CardapioService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class CardapioServiceIntegrationTest {

    @Autowired
    private CardapioService cardapioService;

    @Autowired
    private CardapioRepository cardapioRepository;

    @Autowired
    private IngredienteRepository ingredienteRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    private CardapioEntity cardapio;

    @BeforeEach
    void setUp() {
        cardapioRepository.deleteAll();

        cardapio = new CardapioEntity();
        cardapio.setData(LocalDate.now());
        cardapio.setIngredientes(List.of());
        cardapio.setPedidos(List.of());

        cardapioRepository.save(cardapio);
    }

    @Test
    @DisplayName("1. Deve salvar cardápio com sucesso")
    void save() {
        CardapioEntity novo = new CardapioEntity();
        novo.setData(LocalDate.now().plusDays(1));
        novo.setIngredientes(List.of());
        novo.setPedidos(List.of());

        CardapioEntity saved = cardapioService.save(novo);

        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(novo.getData(), saved.getData());
    }

    @Test
    @DisplayName("2. Deve buscar cardápio por ID")
    void findById() {
        CardapioEntity found = cardapioService.findById(cardapio.getId());
        Assertions.assertNotNull(found);
        Assertions.assertEquals(cardapio.getData(), found.getData());
    }

    @Test
    @DisplayName("3. Deve deletar cardápio existente")
    void delete() {
        cardapioService.delete(cardapio.getId());
        Assertions.assertTrue(cardapioRepository.findById(Math.toIntExact(cardapio.getId())).isEmpty());
    }
}
