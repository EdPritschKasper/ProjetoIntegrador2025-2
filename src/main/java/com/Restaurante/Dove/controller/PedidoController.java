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
        try {
            var result = pedidoService.save(pedido);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception ex){
            ex.printStackTrace(); // ou use um logger
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<PedidoEntity>> findAll(){
        try {
            var result = pedidoService.findAll();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoEntity> findById(@PathVariable Integer id){
        try {
            var result = pedidoService.findById(Long.valueOf(id));
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoEntity> update(@PathVariable Integer id, @RequestBody PedidoEntity pedido){
        try {
            var result = pedidoService.update(Long.valueOf(id), pedido);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        try {
            pedidoService.delete(Long.valueOf(id));
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findByCardapio")
    public ResponseEntity<List<PedidoEntity>> findByCardapio(@RequestBody CardapioEntity cardapio) {
        try {
            var result = pedidoService.findByCardapio(cardapio);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findByStatus")
    public ResponseEntity<List<PedidoEntity>> findByStatus(@RequestParam String status) {
        try {
            var result = pedidoService.findByStatus(status);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/contar")
    public ResponseEntity<Integer> contarPedidos(@RequestParam LocalDate data) {
        try {
            var result = pedidoService.contarPedidosPorData(data);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/media-mes")
    public ResponseEntity<Double> mediaPedidosMes(@RequestParam int mes) {
        try {
            var result = pedidoService.mediaPedidosPorMes(mes);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
