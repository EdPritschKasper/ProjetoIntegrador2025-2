package com.Restaurante.Dove.controller;

import com.Restaurante.Dove.model.CardapioEntity;
import com.Restaurante.Dove.model.IngredienteEntity;
import com.Restaurante.Dove.model.PedidoEntity;
import com.Restaurante.Dove.service.CardapioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cardapios")
@RequiredArgsConstructor
public class CardapioController {

    private final CardapioService cardapioService;

    @GetMapping
    public ResponseEntity<List<CardapioEntity>> findAll(){
        var result = cardapioService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CardapioEntity> save(@RequestBody CardapioEntity cardapio){
        var result = cardapioService.save(cardapio);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardapioEntity> findById(@PathVariable Integer id){
        var result = cardapioService.findById(Long.valueOf(id));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody CardapioEntity cardapio) {
        var result = cardapioService.update(Long.valueOf(id), cardapio);

        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cardápio com ID " + id + " não encontrado.");
        }

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        cardapioService.delete(Long.valueOf(id));
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/findByPedidos")
    public ResponseEntity<Optional<CardapioEntity>> findByPedidos(@RequestBody PedidoEntity pedido){
        var result = cardapioService.findByPedidos(pedido);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/findByIngredientes")
    public ResponseEntity<List<CardapioEntity>> findByPedidos(@RequestBody IngredienteEntity ingrediente){
        var result = cardapioService.findByIngredientes(ingrediente);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/hoje")
    public CardapioEntity getCardapioDoDia() {
        return cardapioService.getCardapioDoDia();
    }
}
