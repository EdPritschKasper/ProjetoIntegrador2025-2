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
        try {
            var result = cardapioService.findAll();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<CardapioEntity> save(@RequestBody CardapioEntity cardapio){
        try {
            var result = cardapioService.save(cardapio);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception ex){
            ex.printStackTrace(); // ou use um logger
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardapioEntity> findById(@PathVariable Integer id){
        try {
            var result = cardapioService.findById(Long.valueOf(id));
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody CardapioEntity cardapio) {
        try {
            var result = cardapioService.update(Long.valueOf(id), cardapio);

            if (result == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Cardápio com ID " + id + " não encontrado.");
            }

            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            ex.printStackTrace(); // para debug
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar cardápio: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        try {
            cardapioService.delete(Long.valueOf(id));
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findByPedidos")
    public ResponseEntity<Optional<CardapioEntity>> findByPedidos(@RequestBody PedidoEntity pedido){
        try {
            var result = cardapioService.findByPedidos(pedido);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findByIngredientes")
    public ResponseEntity<List<CardapioEntity>> findByPedidos(@RequestBody IngredienteEntity ingrediente){
        try {
            var result = cardapioService.findByIngredientes(ingrediente);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
