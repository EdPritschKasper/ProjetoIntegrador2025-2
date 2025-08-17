package com.Restaurante.Dove.service;

import com.Restaurante.Dove.model.FuncionarioEntity;
import com.Restaurante.Dove.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    // Criar funcionário
    public FuncionarioEntity save(FuncionarioEntity funcionario) {
        if (funcionario == null) {
            throw new RuntimeException("Funcionário não pode ser nulo.");
        }
        if (funcionario.getNome() == null || funcionario.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome do funcionário é obrigatório.");
        }
        if (funcionario.getCpf() == null || funcionario.getCpf().trim().isEmpty()) {
            throw new RuntimeException("CPF do funcionário é obrigatório.");
        }
        return funcionarioRepository.save(funcionario);
    }

    // Listar todos
    public List<FuncionarioEntity> findAll() {
        return funcionarioRepository.findAll();
    }

    // Buscar por ID
    public FuncionarioEntity findById(Long id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com id: " + id));
    }

    // Atualizar
    public FuncionarioEntity update(Long id, FuncionarioEntity funcionarioAtualizado) {
        return funcionarioRepository.findById(id).map(funcionario -> {
            funcionario.setNome(funcionarioAtualizado.getNome());
            funcionario.setCpf(funcionarioAtualizado.getCpf());
            return funcionarioRepository.save(funcionario);
        }).orElseThrow(() -> new RuntimeException("Funcionário não encontrado com id: " + id));
    }

    // Deletar
    public void delete(Long id) {
        if (!funcionarioRepository.existsById(id)) {
            throw new RuntimeException("Funcionário não encontrado com id: " + id);
        }
        funcionarioRepository.deleteById(id);
    }

    // Gerar relatório de pedidos do funcionário
    public Map<String, Object> gerarRelatorioPedidos(Long funcionarioId) {
        FuncionarioEntity funcionario = funcionarioRepository.findById(funcionarioId)
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

    //Relatorio com Funcionario com mais pedidos
    public Map<String, Object> funcionarioMaisPedidos() {
        List<FuncionarioEntity> funcionarios = funcionarioRepository.findAll();

        // Encontrar o funcionário com mais pedidos
        FuncionarioEntity funcionarioMax = funcionarios.stream()
                .max((f1, f2) -> Integer.compare(f1.getPedidos().size(), f2.getPedidos().size()))
                .orElseThrow(() -> new RuntimeException("Nenhum funcionário encontrado"));

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("funcionarioNome", funcionarioMax.getNome());
        resultado.put("funcionarioCpf", funcionarioMax.getCpf());
        resultado.put("quantidadePedidos", funcionarioMax.getPedidos().size());

        return resultado;
    }
}
