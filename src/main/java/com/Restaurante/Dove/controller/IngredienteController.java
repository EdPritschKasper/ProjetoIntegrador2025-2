package com.Restaurante.Dove.controller;

import com.Restaurante.Dove.model.CardapioEntity;
import com.Restaurante.Dove.model.IngredienteEntity;
import com.Restaurante.Dove.model.PedidoEntity;
import com.Restaurante.Dove.service.IngredienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredientes")
@RequiredArgsConstructor
public class IngredienteController {

    private final IngredienteService ingredienteService;

    @GetMapping
    public ResponseEntity<List<IngredienteEntity>> findAll() {
        var result = ingredienteService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<IngredienteEntity> save(@RequestBody IngredienteEntity ingrediente) {
        var result = ingredienteService.save(ingrediente);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredienteEntity> findById(@PathVariable Integer id) {
        var result = ingredienteService.findById(Long.valueOf(id));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody IngredienteEntity ingrediente) {
        var result = ingredienteService.update(Long.valueOf(id), ingrediente);

        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ingrediente com ID " + id + " não encontrado.");
        }

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        ingredienteService.delete(Long.valueOf(id));
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/findByCardapios")
    public ResponseEntity<List<IngredienteEntity>> findByCardapios(@RequestBody CardapioEntity cardapio) {
        var result = ingredienteService.findByCardapios(cardapio);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/findByPedidos")
    public ResponseEntity<List<IngredienteEntity>> findByPedidos(@RequestBody PedidoEntity pedido) {
        var result = ingredienteService.findByPedidos(pedido);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
