package com.Restaurante.Dove.controller;

import com.Restaurante.Dove.model.UsuarioEntity;
import com.Restaurante.Dove.repository.UsuarioRepository;
import com.Restaurante.Dove.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
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

    @PutMapping("/senha/{id}")
    public ResponseEntity<?> trocarSenha(@PathVariable Long id,
                                         @RequestBody Map<String, String> body) {
        try {
            usuarioService.trocarSenha(
                    id,
                    body.get("senhaAtual"),
                    body.get("novaSenha"),
                    body.get("confirmacao")
            );
            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("", e.getMessage()));

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("", e.getMessage()));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("", e.getMessage()));
        }
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

    @GetMapping("/relatorio/{id}")
    public ResponseEntity<Map<String, Object>> gerarRelatorioPedidos(@PathVariable Long id) {
        var result = usuarioService.gerarRelatorioPedidos(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/funcionarioMP")
    public ResponseEntity<Map<String, Object>> funcionarioMaisPedidos() {
        var result = usuarioService.funcionarioMaisPedidos();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
