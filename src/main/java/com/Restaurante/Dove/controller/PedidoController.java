package com.Restaurante.Dove.controller;

import com.Restaurante.Dove.model.CardapioEntity;
import com.Restaurante.Dove.model.PedidoEntity;
import com.Restaurante.Dove.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoEntity> save(@RequestBody PedidoEntity pedido){
        var result = pedidoService.save(pedido);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PedidoEntity>> findAll(){
        var result = pedidoService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoEntity> findById(@PathVariable Integer id){
        var result = pedidoService.findById(Long.valueOf(id));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoEntity> update(@PathVariable Integer id, @RequestBody PedidoEntity pedido){
        var result = pedidoService.update(Long.valueOf(id), pedido);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        pedidoService.delete(Long.valueOf(id));
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/findByCardapio")
    public ResponseEntity<List<PedidoEntity>> findByCardapio(@RequestBody CardapioEntity cardapio) {
        var result = pedidoService.findByCardapio(cardapio);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/findByStatus")
    public ResponseEntity<List<PedidoEntity>> findByStatus(@RequestParam String status) {
        var result = pedidoService.findByStatus(status);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/contar")
    public ResponseEntity<Integer> contarPedidos(@RequestParam LocalDate data) {
        var result = pedidoService.contarPedidosPorData(data);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/media-mes")
    public ResponseEntity<Double> mediaPedidosMes(@RequestParam int mes) {
        var result = pedidoService.mediaPedidosPorMes(mes);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
