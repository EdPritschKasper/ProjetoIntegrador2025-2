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
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteRepository repo;
    private final PasswordEncoder encoder;

    @GetMapping("/findAll")
    public ResponseEntity<List<ClienteEntity>> findAll(){

        try{
             var result = clienteService.findAll();
             return new ResponseEntity<>(result , HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(null , HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<ClienteEntity> findById(@PathVariable Long id){
        try {
            var result = clienteService.findById(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/findPedido/{id}")
    public ResponseEntity<Long> findPedidoById(@PathVariable Long id){

        try {
            var result = clienteService.getPedidosById(id);
            return new ResponseEntity<>(result , HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>(null , HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listar-tempos/{id}")
    public ResponseEntity<List<Integer>> listarTempos(@PathVariable Long id) {
        try {
            var result = clienteService.listarTempos(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<ClienteEntity> save(@RequestBody ClienteEntity cliente){

        try {
            var result = clienteService.save(cliente);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ClienteEntity> update(@PathVariable Long id , @RequestBody ClienteEntity cliente){

        try {
            var result = clienteService.update(id, cliente);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

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
        try {
            clienteService.delete(id);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            return new ResponseEntity<>(null , HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findByNome")
    public ResponseEntity<List<ClienteEntity>> findByNome(@RequestParam String nome) {
        try {
            var result = clienteService.findByNome(nome);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<Optional<ClienteEntity>> findByEmail(@RequestParam String email) {
        try {
            var result = clienteService.findByEmail(email);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
