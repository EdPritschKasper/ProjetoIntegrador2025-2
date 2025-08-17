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
        try {
            var result = ingredienteService.findAll();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<IngredienteEntity> save(@RequestBody IngredienteEntity ingrediente) {
        try {
            var result = ingredienteService.save(ingrediente);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredienteEntity> findById(@PathVariable Integer id) {
        try {
            var result = ingredienteService.findById(Long.valueOf(id));
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody IngredienteEntity ingrediente) {
        try {
            var result = ingredienteService.update(Long.valueOf(id), ingrediente);

            if (result == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Ingrediente com ID " + id + " não encontrado.");
            }

            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar ingrediente: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            ingredienteService.delete(Long.valueOf(id));
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findByCardapios")
    public ResponseEntity<List<IngredienteEntity>> findByCardapios(@RequestBody CardapioEntity cardapio) {
        try {
            var result = ingredienteService.findByCardapios(cardapio);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findByPedidos")
    public ResponseEntity<List<IngredienteEntity>> findByPedidos(@RequestBody PedidoEntity pedido) {
        try {
            var result = ingredienteService.findByPedidos(pedido);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
