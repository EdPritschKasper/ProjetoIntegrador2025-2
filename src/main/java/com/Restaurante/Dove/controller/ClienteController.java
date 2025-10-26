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
import java.util.Map;
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
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ClienteEntity> update(@PathVariable Long id , @RequestBody ClienteEntity cliente){
        var result = clienteService.update(id, cliente);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(path = "/senha/{id}", consumes = "application/json")
    public ResponseEntity<?> trocarSenha(@PathVariable Long id,
                                         @RequestBody Map<String, String> body) {
        String senhaAtual  = body.get("senhaAtual");
        String novaSenha   = body.get("novaSenha");
        String confirmacao = body.get("confirmacao");

        if (senhaAtual == null || senhaAtual.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Informe a senha atual."));
        }
        if (novaSenha == null || novaSenha.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Informe a nova senha."));
        }
        if (confirmacao != null && !novaSenha.equals(confirmacao)) {
            return ResponseEntity.badRequest().body(Map.of("message", "As senhas não coincidem."));
        }
        if (novaSenha.length() < 8) {
            return ResponseEntity.badRequest().body(Map.of("message", "A nova senha deve possuir ao menos 8 caracteres."));
        }

        return repo.findById(id)
                .map(c -> {
                    if (!senhaAtual.equals(c.getSenha())) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(Map.of("message", "Senha atual incorreta."));
                    }
                    c.setSenha(novaSenha);
                    repo.save(c);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Cliente não encontrado.")));

    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        clienteService.delete(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/findByNome")
    public ResponseEntity<ClienteEntity> findByNome(@RequestParam String nome) {
        return clienteService.findByEmail(nome)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<ClienteEntity> findByEmail(@RequestParam String email) {
        return clienteService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
