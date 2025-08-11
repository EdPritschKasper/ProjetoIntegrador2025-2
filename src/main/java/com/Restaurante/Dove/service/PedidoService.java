package com.Restaurante.Dove.service;

import com.Restaurante.Dove.model.IngredienteEntity;
import com.Restaurante.Dove.model.PedidoEntity;
import com.Restaurante.Dove.repository.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CardapioRepository cardapioRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ClienteRepository clienteRepository;
    private final IngredienteRepository ingredienteRepository;

    public PedidoEntity save(PedidoEntity pedido) {
        // Buscar o cardápio pelo ID
        if (pedido.getCardapio() != null && pedido.getCardapio().getId() != null) {
            var cardapio = cardapioRepository.findById(Math.toIntExact(pedido.getCardapio().getId()))
                    .orElseThrow(() -> new RuntimeException("Cardápio não encontrado"));
            pedido.setCardapio(cardapio);
        }

        // Buscar funcionário
        if (pedido.getFuncionario() != null && pedido.getFuncionario().getId() != null) {
            var funcionario = funcionarioRepository.findById(pedido.getFuncionario().getId())
                    .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
            pedido.setFuncionario(funcionario);
        }

        // Buscar cliente
        if (pedido.getCliente() != null && pedido.getCliente().getId() != null) {
            var cliente = clienteRepository.findById(pedido.getCliente().getId())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            pedido.setCliente(cliente);
        }

        // Buscar ingredientes
        if (pedido.getIngredientes() != null && !pedido.getIngredientes().isEmpty()) {
            List<IngredienteEntity> ingredientes = new ArrayList<>();
            for (var ing : pedido.getIngredientes()) {
                var ingrediente = ingredienteRepository.findById(Math.toIntExact(ing.getId()))
                        .orElseThrow(() -> new RuntimeException("Ingrediente não encontrado: " + ing.getId()));
                ingredientes.add(ingrediente);
            }
            pedido.setIngredientes(ingredientes);
        }

        return pedidoRepository.save(pedido);
    }

    public List<PedidoEntity> findAll(){
        return pedidoRepository.findAll();
    }

    public PedidoEntity findById(Long id) {
        return pedidoRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com id: " + id));
    }

    public PedidoEntity update(Long id, PedidoEntity pedidoAtualizado) {
        return pedidoRepository.findById(Math.toIntExact(id)).map(pedidoExistente -> {
            // Atualiza os campos simples
            pedidoExistente.setMarmita(pedidoAtualizado.getMarmita());
            pedidoExistente.setStatus(pedidoAtualizado.getStatus());
            pedidoExistente.setHora_inicio(pedidoAtualizado.getHora_inicio());
            pedidoExistente.setHora_fim(pedidoAtualizado.getHora_fim());

            // Atualiza relacionamentos buscando as entidades no banco
            if (pedidoAtualizado.getCardapio() != null && pedidoAtualizado.getCardapio().getId() != null) {
                var cardapio = cardapioRepository.findById(Math.toIntExact(pedidoAtualizado.getCardapio().getId()))
                        .orElseThrow(() -> new RuntimeException("Cardapio não encontrado"));
                pedidoExistente.setCardapio(cardapio);
            }

            if (pedidoAtualizado.getFuncionario() != null && pedidoAtualizado.getFuncionario().getId() != null) {
                var funcionario = funcionarioRepository.findById(pedidoAtualizado.getFuncionario().getId())
                        .orElseThrow(() -> new RuntimeException("Funcionario não encontrado"));
                pedidoExistente.setFuncionario(funcionario);
            }

            if (pedidoAtualizado.getCliente() != null && pedidoAtualizado.getCliente().getId() != null) {
                var cliente = clienteRepository.findById(pedidoAtualizado.getCliente().getId())
                        .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
                pedidoExistente.setCliente(cliente);
            }

            // Atualiza lista de ingredientes
            if (pedidoAtualizado.getIngredientes() != null && !pedidoAtualizado.getIngredientes().isEmpty()) {
                List<IngredienteEntity> ingredientesAtualizados = new ArrayList<>();
                for (var ingrediente : pedidoAtualizado.getIngredientes()) {
                    if (ingrediente.getId() != null) {
                        var ing = ingredienteRepository.findById(Math.toIntExact(ingrediente.getId()))
                                .orElseThrow(() -> new RuntimeException("Ingrediente não encontrado: " + ingrediente.getId()));
                        ingredientesAtualizados.add(ing);
                    }
                }
                pedidoExistente.setIngredientes(ingredientesAtualizados);
            }

            // Salva e retorna o pedido atualizado
            return pedidoRepository.save(pedidoExistente);
        }).orElseThrow(() -> new RuntimeException("Pedido não encontrado com id: " + id));
    }

    public void delete(Long id) {
        if (!pedidoRepository.existsById(Math.toIntExact(id))) {
            throw new RuntimeException("Pedido não encontrado com id: " + id);
        }
        pedidoRepository.deleteById(Math.toIntExact(id));
    }
}
