package com.Restaurante.Dove.services;

import com.Restaurante.Dove.model.CardapioEntity;
import com.Restaurante.Dove.model.PedidoEntity;
import com.Restaurante.Dove.repository.CardapioRepository;
import com.Restaurante.Dove.repository.PedidoRepository;
import com.Restaurante.Dove.service.PedidoService;
//import com.Restaurante.Dove.config.exceptions.FailedSaveException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class PedidoServiceIntegrationTest {

    @Autowired
    PedidoService pedidoService;

    @Autowired
    PedidoRepository repository;

    @Autowired
    CardapioRepository cardapioRepository;

    PedidoEntity pedido;
    CardapioEntity cardapio;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        cardapioRepository.deleteAll();

        // Criando e salvando um cardápio
        cardapio = new CardapioEntity();
        cardapio.setData(LocalDate.now());
        cardapio = cardapioRepository.save(cardapio);

        // Criando e salvando um pedido
        pedido = new PedidoEntity();
        pedido.setMarmita("Marmita Média");
        pedido.setStatus("ABERTO");
        pedido.setHora_inicio(LocalTime.of(11, 30));
        pedido.setHora_fim(LocalTime.of(12, 0));
        pedido.setCardapio(cardapio);

        repository.save(pedido);
    }

    @Test
    @DisplayName("1. Deve salvar um pedido com sucesso")
    void save() {
        var novo = new PedidoEntity();
        novo.setMarmita("Marmita Grande");
        novo.setStatus("ABERTO");
        novo.setHora_inicio(LocalTime.of(12, 30));
        novo.setHora_fim(LocalTime.of(13, 0));
        novo.setCardapio(cardapio);

        var response = pedidoService.save(novo);

        Assertions.assertNotNull(response.getId());
        Assertions.assertEquals("Marmita Grande", response.getMarmita());
        Assertions.assertEquals("ABERTO", response.getStatus());
    }

//    @Test
//    @DisplayName("2. Deve lançar FailedSaveException ao tentar salvar pedido duplicado (simulação)")
//    void cenario02() {
//        var duplicado = new PedidoEntity();
//        duplicado.setMarmita(pedido.getMarmita());
//        duplicado.setStatus(pedido.getStatus());
//        duplicado.setHora_inicio(pedido.getHora_inicio());
//        duplicado.setHora_fim(pedido.getHora_fim());
//        duplicado.setCardapio(cardapio);
//
//        Assertions.assertThrows(FailedSaveException.class, () -> {
//            pedidoService.save(duplicado);
//        });
//    }

    @Test
    @DisplayName("3. Deve buscar pedido por ID com sucesso")
    void findById() {
        var encontrado = pedidoService.findById(pedido.getId());
        Assertions.assertNotNull(encontrado);
        Assertions.assertEquals(pedido.getMarmita(), encontrado.getMarmita());
    }

    @Test
    @DisplayName("4. Deve deletar pedido existente")
    void delete() {
        pedidoService.delete(pedido.getId());
        var result = repository.findById(Math.toIntExact(pedido.getId()));
        Assertions.assertTrue(result.isEmpty());
    }
}
