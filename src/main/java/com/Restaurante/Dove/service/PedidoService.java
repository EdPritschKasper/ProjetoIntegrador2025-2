package com.Restaurante.Dove.service;

import com.Restaurante.Dove.model.CardapioEntity;
import com.Restaurante.Dove.model.IngredienteEntity;
import com.Restaurante.Dove.model.PedidoEntity;
import com.Restaurante.Dove.repository.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CardapioRepository cardapioRepository;
    private final UsuarioRepository usuarioRepository;
    private final IngredienteRepository ingredienteRepository;

    public PedidoEntity save(PedidoEntity pedido) {
        // Buscar o cardápio pelo ID
        if (pedido.getCardapio() != null && pedido.getCardapio().getId() != null) {
            var cardapio = cardapioRepository.findById(Math.toIntExact(pedido.getCardapio().getId()))
                    .orElseThrow(() -> new RuntimeException("Cardápio não encontrado"));
            pedido.setCardapio(cardapio);
        } else {
            throw new RuntimeException("Nenhum cardápio informado para o pedido");
        }

        // Buscar usuário (cliente ou funcionário)
        if (pedido.getUsuario() != null && pedido.getUsuario().getId() != null) {
            var usuario = usuarioRepository.findById(pedido.getUsuario().getId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            pedido.setUsuario(usuario);
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
            } else {
                throw new RuntimeException("Nenhum cardápio informado para o pedido");
            }

            if (pedidoAtualizado.getUsuario() != null && pedidoAtualizado.getUsuario().getId() != null) {
                var usuario = usuarioRepository.findById(pedidoAtualizado.getUsuario().getId())
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
                pedidoExistente.setUsuario(usuario);
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

    public List<PedidoEntity> findByCardapio(CardapioEntity cardapio){
        if (cardapioRepository.findById(cardapio.getId().intValue()).isEmpty()) {
            throw new RuntimeException("Cardapio não encontrado com id: " + cardapio.getId());
        }
        return pedidoRepository.findByCardapio(cardapio);
    }

    public List<PedidoEntity> findByStatus(String status) {
        return pedidoRepository.findByStatus(status);
    }

    public int contarPedidosPorData(LocalDate data) {
        return pedidoRepository.contarPedidosPorData(data);
    }

    public Double mediaPedidosPorMes(int mes) {
        return pedidoRepository.mediaPedidosPorMes(mes);
    }
}
