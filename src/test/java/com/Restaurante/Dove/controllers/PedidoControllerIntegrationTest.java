//package com.Restaurante.Dove.controllers;
//
//import com.Restaurante.Dove.model.CardapioEntity;
//import com.Restaurante.Dove.model.PedidoEntity;
//import com.Restaurante.Dove.repository.CardapioRepository;
//import com.Restaurante.Dove.repository.PedidoRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.*;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@Transactional
//@Rollback
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class PedidoControllerIntegrationTest {
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Autowired
//    private PedidoRepository pedidoRepository;
//
//    @Autowired
//    private CardapioRepository cardapioRepository;
//
//    private CardapioEntity cardapio;
//    private PedidoEntity pedido;
//
//    @BeforeEach
//    void setup() {
////        pedidoRepository.deleteAll();
////        cardapioRepository.deleteAll();
//
//        cardapio = new CardapioEntity();
//        cardapio.setData(LocalDate.now());
//        cardapio = cardapioRepository.save(cardapio);
//
//        pedido = new PedidoEntity();
//        pedido.setMarmita("Marmita Média");
//        pedido.setStatus("ABERTO");
//        pedido.setHora_inicio(LocalTime.of(11, 0));
//        pedido.setHora_fim(LocalTime.of(11, 30));
//        pedido.setCardapio(cardapio);
//        pedido = pedidoRepository.save(pedido);
//    }
//
//    @Test
//    @DisplayName("1. Deve salvar um novo pedido via controller")
//    void testSavePedido() {
//        PedidoEntity novo = new PedidoEntity();
//        novo.setMarmita("Marmita Grande");
//        novo.setStatus("ABERTO");
//        novo.setHora_inicio(LocalTime.of(12, 0));
//        novo.setHora_fim(LocalTime.of(12, 30));
//        novo.setCardapio(cardapio);
//
//        ResponseEntity<PedidoEntity> response = restTemplate.postForEntity("/api/pedidos", novo, PedidoEntity.class);
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals("Marmita Grande", response.getBody().getMarmita());
//    }
//
//    @Test
//    @DisplayName("2. Deve buscar todos os pedidos")
//    void testFindAll() {
//        ResponseEntity<PedidoEntity[]> response = restTemplate.getForEntity("/api/pedidos", PedidoEntity[].class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertTrue(response.getBody().length > 0);
//    }
//
//    @Test
//    @DisplayName("3. Deve buscar pedido por ID")
//    void testFindById() {
//        ResponseEntity<PedidoEntity> response = restTemplate.getForEntity("/api/pedidos/" + pedido.getId(), PedidoEntity.class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(pedido.getMarmita(), response.getBody().getMarmita());
//    }
//
//    @Test
//    @DisplayName("4. Deve atualizar pedido")
//    void testUpdatePedido() {
//        pedido.setMarmita("Marmita Atualizada");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<PedidoEntity> entity = new HttpEntity<>(pedido, headers);
//
//        ResponseEntity<PedidoEntity> response = restTemplate.exchange("/api/pedidos/" + pedido.getId(), HttpMethod.PUT, entity, PedidoEntity.class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Marmita Atualizada", response.getBody().getMarmita());
//    }
//
//    @Test
//    @DisplayName("5. Deve deletar pedido")
//    void testDeletePedido() {
//        ResponseEntity<Void> response = restTemplate.exchange("/api/pedidos/" + pedido.getId(), HttpMethod.DELETE, null, Void.class);
//
//        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//        assertFalse(pedidoRepository.existsById(Math.toIntExact(pedido.getId())));
//    }
//
//    @Test
//    @DisplayName("6. Deve buscar pedidos por status")
//    void testFindByStatus() {
//        ResponseEntity<PedidoEntity[]> response = restTemplate.getForEntity("/api/pedidos/findByStatus?status=ABERTO", PedidoEntity[].class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertTrue(response.getBody().length > 0);
//    }
//
//    @Test
//    @DisplayName("7. Deve contar pedidos por data")
//    void testContarPedidos() {
//        String url = "/api/pedidos/contar?data=" + LocalDate.now();
//        ResponseEntity<Integer> response = restTemplate.getForEntity(url, Integer.class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertTrue(response.getBody() > 0);
//    }
//
//    @Test
//    @DisplayName("8. Deve calcular média de pedidos por mês")
//    void testMediaPedidosMes() {
//        String url = "/api/pedidos/media-mes?mes=" + LocalDate.now().getMonthValue();
//        ResponseEntity<Double> response = restTemplate.getForEntity(url, Double.class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//    }
//
////    @Test
////    @DisplayName("Salvar pedido com dados inválidos deve retornar 500")
////    void testSavePedidoInvalid() {
////        PedidoEntity invalido = new PedidoEntity();
////        invalido.setMarmita(null); // inválido
////        invalido.setStatus("ABERTO");
////        invalido.setHora_inicio(LocalTime.of(12, 0));
////        invalido.setHora_fim(LocalTime.of(11, 0)); // hora início > hora fim
////        invalido.setCardapio(cardapio);
////
////        ResponseEntity<String> response = restTemplate.postForEntity("/api/pedidos", invalido, String.class);
////        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
////    }
////
////    @Test
////    @DisplayName("Atualizar pedido inexistente deve retornar 500")
////    void testUpdatePedidoNotFound() {
////        PedidoEntity inexistente = new PedidoEntity();
////        inexistente.setMarmita("Teste");
////
////        HttpHeaders headers = new HttpHeaders();
////        headers.setContentType(MediaType.APPLICATION_JSON);
////        HttpEntity<PedidoEntity> entity = new HttpEntity<>(inexistente, headers);
////
////        ResponseEntity<String> response = restTemplate.exchange("/api/pedidos/999", HttpMethod.PUT, entity, String.class);
////        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
////    }
////
////    @Test
////    @DisplayName("Deletar pedido inexistente retorna 500")
////    void testDeletePedidoNotFound() {
////        ResponseEntity<String> response = restTemplate.exchange("/api/pedidos/999", HttpMethod.DELETE, null, String.class);
////        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
////    }
////
////    @Test
////    @DisplayName("Buscar pedidos por cardápio sem pedidos")
////    void testFindByCardapioEmpty() {
////        CardapioEntity novoCardapio = new CardapioEntity();
////        novoCardapio.setData(LocalDate.now().plusDays(1));
////        novoCardapio = cardapioRepository.save(novoCardapio);
////
////        HttpEntity<CardapioEntity> entity = new HttpEntity<>(novoCardapio);
////        ResponseEntity<PedidoEntity[]> response = restTemplate.postForEntity("/api/pedidos/findByCardapio", entity, PedidoEntity[].class);
////
////        assertEquals(HttpStatus.OK, response.getStatusCode());
////        assertEquals(0, response.getBody().length);
////    }
//
//}
