package com.Restaurante.Dove.controller;

import com.Restaurante.Dove.model.ClienteEntity;
import com.Restaurante.Dove.repository.ClienteRepository;
import com.Restaurante.Dove.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cliente")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteRepository repo;
    private final PasswordEncoder encoder;

    @GetMapping("/findAll")
    public ResponseEntity<List<ClienteEntity>> findAll(){
        var result = clienteService.findAll();
        return new ResponseEntity<>(result , HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<ClienteEntity> findById(@PathVariable Long id){
        var result = clienteService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/findPedido/{id}")
    public ResponseEntity<Long> findPedidoById(@PathVariable Long id){
        var result = clienteService.getPedidosById(id);
        return new ResponseEntity<>(result , HttpStatus.OK);
    }

    @GetMapping("/listar-tempos/{id}")
    public ResponseEntity<List<Integer>> listarTempos(@PathVariable Long id) {
        var result = clienteService.listarTempos(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ClienteEntity> save(@RequestBody ClienteEntity cliente){
        var result = clienteService.save(cliente);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ClienteEntity> update(@PathVariable Long id , @RequestBody ClienteEntity cliente){
        var result = clienteService.update(id, cliente);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}/senha")
    public ResponseEntity<Object> trocarSenha(@PathVariable Long id,
                                              @RequestParam String senhaAtual,
                                              @RequestParam String novaSenha) {
        return repo.findById(id)
                .map(c -> {
                    if (!encoder.matches(senhaAtual, c.getSenha())) {
                        return ResponseEntity.status(401).build();
                    }
                    c.setSenha(encoder.encode(novaSenha));
                    repo.save(c);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        clienteService.delete(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/findByNome")
    public ResponseEntity<List<ClienteEntity>> findByNome(@RequestParam String nome) {
        var result = clienteService.findByNome(nome);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<Optional<ClienteEntity>> findByEmail(@RequestParam String email) {
        var result = clienteService.findByEmail(email);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
