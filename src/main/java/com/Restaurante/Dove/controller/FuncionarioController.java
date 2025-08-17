package com.Restaurante.Dove.controller;

import com.Restaurante.Dove.model.FuncionarioEntity;
import com.Restaurante.Dove.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/funcionario")
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @GetMapping("/findAll")
    public ResponseEntity<List<FuncionarioEntity>> findAll() {
        try {
            var result = funcionarioService.findAll();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<FuncionarioEntity> findById(@PathVariable Long id) {
        try {
            var result = funcionarioService.findById(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<FuncionarioEntity> save(@RequestBody FuncionarioEntity funcionario) {
        try {
            var result = funcionarioService.save(funcionario);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<FuncionarioEntity> update(@PathVariable Long id, @RequestBody FuncionarioEntity funcionario) {
        try {
            var result = funcionarioService.update(id, funcionario);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            funcionarioService.delete(id);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint para gerar relatório simples dos pedidos do funcionário
    @GetMapping("/relatorio/{id}")
    public ResponseEntity<Map<String, Object>> gerarRelatorioPedidos(@PathVariable Long id) {
        try {
            var result = funcionarioService.gerarRelatorioPedidos(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/funcionarioMP")
    public ResponseEntity<Map<String, Object>> funcionarioMaisPedidos() {
        try {
            var result = funcionarioService.funcionarioMaisPedidos();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
}
