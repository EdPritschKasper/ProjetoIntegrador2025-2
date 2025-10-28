package com.Restaurante.Dove.controller;

import com.Restaurante.Dove.model.FuncionarioEntity;
import com.Restaurante.Dove.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/funcionario")
@RequiredArgsConstructor
@CrossOrigin("*")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @GetMapping("/findAll")
    public ResponseEntity<List<FuncionarioEntity>> findAll() {
        var result = funcionarioService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<FuncionarioEntity> findById(@PathVariable Long id) {
        var result = funcionarioService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<FuncionarioEntity> save(@RequestBody FuncionarioEntity funcionario) {
        var result = funcionarioService.save(funcionario);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<FuncionarioEntity> update(@PathVariable Long id, @RequestBody FuncionarioEntity funcionario) {
        var result = funcionarioService.update(id, funcionario);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        funcionarioService.delete(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    // Endpoint para gerar relatório simples dos pedidos do funcionário
    @GetMapping("/relatorio/{id}")
    public ResponseEntity<Map<String, Object>> gerarRelatorioPedidos(@PathVariable Long id) {
        var result = funcionarioService.gerarRelatorioPedidos(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/funcionarioMP")
    public ResponseEntity<Map<String, Object>> funcionarioMaisPedidos() {
        var result = funcionarioService.funcionarioMaisPedidos();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/findByNome")
    public ResponseEntity<List<FuncionarioEntity>> findByNome(@RequestParam String nome) {
        var result = funcionarioService.findByNome(nome);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/findByCpf")
    public ResponseEntity<Optional<FuncionarioEntity>> findByCpf(@RequestParam String cpf) {
        var result = funcionarioService.findByCpf(cpf);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
