package com.Restaurante.Dove.controller;

import com.Restaurante.Dove.model.UsuarioEntity;
import com.Restaurante.Dove.repository.UsuarioRepository;
import com.Restaurante.Dove.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository repo;

    @GetMapping
    public ResponseEntity<List<UsuarioEntity>> findAll() {
        var result = usuarioService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioEntity> findById(@PathVariable Long id) {
        var result = usuarioService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/findPedido/{id}")
    public ResponseEntity<Long> findPedidoById(@PathVariable Long id) {
        var result = usuarioService.getPedidosById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/listar-tempos/{id}")
    public ResponseEntity<List<Integer>> listarTempos(@PathVariable Long id) {
        var result = usuarioService.listarTempos(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UsuarioEntity> save(@RequestBody UsuarioEntity usuario) {
        var result = usuarioService.save(usuario);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioEntity> update(@PathVariable Long id, @RequestBody UsuarioEntity usuario) {
        var result = usuarioService.update(id, usuario);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(path = "/senha/{id}", consumes = "application/json")
    public ResponseEntity<?> trocarSenha(@PathVariable Long id,
                                         @RequestBody Map<String, String> body) {
        String senhaAtual = body.get("senhaAtual");
        String novaSenha = body.get("novaSenha");
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
                .map(u -> {
                    if (!senhaAtual.equals(u.getSenha())) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(Map.of("message", "Senha atual incorreta."));
                    }
                    u.setSenha(novaSenha);
                    repo.save(u);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Usuário não encontrado.")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/findByNome")
    public ResponseEntity<List<UsuarioEntity>> findByNome(@RequestParam String nome) {
        var result = usuarioService.findAll().stream()
                .filter(u -> u.getNome() != null && u.getNome().equalsIgnoreCase(nome))
                .toList();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<UsuarioEntity> findByEmail(@RequestParam String email) {
        return usuarioService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
