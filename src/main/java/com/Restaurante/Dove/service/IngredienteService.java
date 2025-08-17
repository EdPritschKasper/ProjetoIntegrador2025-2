package com.Restaurante.Dove.service;

import com.Restaurante.Dove.model.CardapioEntity;
import com.Restaurante.Dove.model.IngredienteEntity;
import com.Restaurante.Dove.model.PedidoEntity;
import com.Restaurante.Dove.repository.CardapioRepository;
import com.Restaurante.Dove.repository.IngredienteRepository;
import com.Restaurante.Dove.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class IngredienteService {

    private final IngredienteRepository ingredienteRepository;
    private final CardapioRepository cardapioRepository;
    private final PedidoRepository pedidoRepository;

    public List<IngredienteEntity> findAll() {
        return ingredienteRepository.findAll();
    }

    public IngredienteEntity save(IngredienteEntity ingrediente) {
        if (ingrediente.getDescricao() == null || ingrediente.getDescricao().isBlank()) {
            throw new RuntimeException("A descrição do ingrediente é obrigatória");
        }
        return ingredienteRepository.save(ingrediente);
    }

    public IngredienteEntity findById(Long id) {
        return ingredienteRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Ingrediente não encontrado com id: " + id));
    }

    @Transactional
    public IngredienteEntity update(Long id, IngredienteEntity novoIngrediente) {
        IngredienteEntity existente = ingredienteRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Ingrediente não encontrado"));

        existente.setDescricao(novoIngrediente.getDescricao());

        // Atualiza cardápios
        existente.getCardapios().clear();
        if (novoIngrediente.getCardapios() != null) {
            for (CardapioEntity cardapio : novoIngrediente.getCardapios()) {
                CardapioEntity cardapioGerenciado = cardapioRepository.findById(Math.toIntExact(cardapio.getId()))
                        .orElseThrow(() -> new RuntimeException("Cardápio não encontrado: " + cardapio.getId()));
                existente.getCardapios().add(cardapioGerenciado);
            }
        }

        // Atualiza pedidos
        existente.getPedidos().clear();
        if (novoIngrediente.getPedidos() != null) {
            for (PedidoEntity pedido : novoIngrediente.getPedidos()) {
                PedidoEntity pedidoGerenciado = pedidoRepository.findById(Math.toIntExact(pedido.getId()))
                        .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + pedido.getId()));
                existente.getPedidos().add(pedidoGerenciado);
            }
        }

        return ingredienteRepository.save(existente);
    }

    public void delete(Long id) {
        if (!ingredienteRepository.existsById(Math.toIntExact(id))) {
            throw new RuntimeException("Ingrediente não encontrado com id: " + id);
        }
        ingredienteRepository.deleteById(Math.toIntExact(id));
    }

    public List<IngredienteEntity> findByCardapios(CardapioEntity cardapio) {
        return ingredienteRepository.findByCardapios(cardapio);
    }

    public List<IngredienteEntity> findByPedidos(PedidoEntity pedido) {
        return ingredienteRepository.findByPedidos(pedido);
    }
}

