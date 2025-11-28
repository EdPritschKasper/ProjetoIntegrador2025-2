package com.Restaurante.Dove.service;

import com.Restaurante.Dove.model.PedidoEntity;
import com.Restaurante.Dove.model.TipoUsuario;
import com.Restaurante.Dove.model.UsuarioEntity;
import com.Restaurante.Dove.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    private int tempoPedidos(PedidoEntity p) {
        LocalTime ini = p.getHora_inicio();
        LocalTime fim = p.getHora_fim();
        int sIni = ini.toSecondOfDay();
        int sFim = fim.toSecondOfDay();
        int diffSeg = sFim - sIni;
        if (diffSeg < 0) diffSeg += 24 * 3600;
        return (int) Math.round(diffSeg / 60.0);
    }

    public UsuarioEntity save(UsuarioEntity usuario) {
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Você deve preencher o nome");
        }

        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty() ||
                usuario.getSenha().length() < 3) {
            throw new IllegalArgumentException("Senha deve ter mais de 3 caracteres");
        }
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));


        if (usuario.getTipo() == TipoUsuario.CLIENTE) {
            if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty() ||
                    (!usuario.getEmail().endsWith("@gmail.com") &&
                            !usuario.getEmail().endsWith("@hotmail.com"))) {
                throw new IllegalArgumentException("O email deve ser @gmail ou @hotmail");
            }
        } else if (usuario.getTipo() == TipoUsuario.FUNCIONARIO) {
            if (usuario.getCpf() == null || usuario.getCpf().trim().isEmpty()) {
                throw new IllegalArgumentException("CPF do funcionário é obrigatório.");
            }
        }

        return usuarioRepository.save(usuario);
    }

    public List<UsuarioEntity> findAll() {
        return usuarioRepository.findAll();
    }

    public UsuarioEntity findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public UsuarioEntity update(Long id, UsuarioEntity usuario) {
        UsuarioEntity update = findById(id);

        if (usuario.getNome() != null && !usuario.getNome().isBlank()) {
            update.setNome(usuario.getNome());
        }
        if (usuario.getSenha() != null && !usuario.getSenha().isBlank()) {
            update.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        return usuarioRepository.save(update);

    }

    public void delete(Long id) {
        UsuarioEntity delete = findById(id);
        usuarioRepository.delete(delete);
    }

    public long getPedidosById(long id) {
        return usuarioRepository.getPedidosById(id);
    }

    public List<Integer> listarTempos(Long id) {
        UsuarioEntity usuario = usuarioRepository.listarTempos(id);
        var pedidos = usuario.getPedidos();
        if (pedidos == null || pedidos.isEmpty()) return List.of();

        return pedidos.stream()
                .filter(p -> p.getHora_inicio() != null && p.getHora_fim() != null)
                .map(this::tempoPedidos)
                .toList();
    }

    public Optional<UsuarioEntity> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Optional<UsuarioEntity> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Optional<UsuarioEntity> findByCpf(String cpf) {
        return usuarioRepository.findByCpf(cpf);
    }

    public void trocarSenha(Long id, String senhaAtual, String novaSenha, String confirmacao) {


        if (senhaAtual == null || senhaAtual.isBlank()) {
            throw new IllegalArgumentException("Informe a senha atual.");
        }
        if (novaSenha == null || novaSenha.isBlank()) {
            throw new IllegalArgumentException("Informe a nova senha.");
        }
        if (confirmacao != null && !novaSenha.equals(confirmacao)) {
            throw new IllegalArgumentException("As senhas não coincidem.");
        }
        if (novaSenha.length() < 8) {
            throw new IllegalArgumentException("A nova senha deve possuir ao menos 8 caracteres.");
        }

        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new SecurityException("Senha atual incorreta.");
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
    }
    public Map<String, Object> gerarRelatorioPedidos(Long funcionarioId) {
        UsuarioEntity funcionario = usuarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com id: " + funcionarioId));

        List<Map<String, Object>> pedidosResumo = funcionario.getPedidos().stream()
                .map(p -> {
                    Map<String, Object> resumo = new HashMap<>();
                    resumo.put("pedidoId", p.getId());
                    resumo.put("status", p.getStatus());
                    resumo.put("horaInicio", p.getHora_inicio());
                    resumo.put("horaFim", p.getHora_fim());
                    return resumo;
                }).collect(Collectors.toList());

        Map<String, Object> relatorio = new HashMap<>();
        relatorio.put("funcionarioNome", funcionario.getNome());
        relatorio.put("funcionarioCpf", funcionario.getCpf());
        relatorio.put("quantidadePedidos", pedidosResumo.size());
        relatorio.put("pedidos", pedidosResumo);

        return relatorio;
    }

    public Map<String, Object> funcionarioMaisPedidos() {
        List<UsuarioEntity> funcionarios = usuarioRepository.findAll();

        UsuarioEntity funcionarioMax = funcionarios.stream()
                .max((f1, f2) -> Integer.compare(f1.getPedidos().size(), f2.getPedidos().size()))
                .orElseThrow(() -> new RuntimeException("Nenhum funcionário encontrado"));

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("funcionarioNome", funcionarioMax.getNome());
        resultado.put("funcionarioCpf", funcionarioMax.getCpf());
        resultado.put("quantidadePedidos", funcionarioMax.getPedidos().size());

        return resultado;
    }
}
