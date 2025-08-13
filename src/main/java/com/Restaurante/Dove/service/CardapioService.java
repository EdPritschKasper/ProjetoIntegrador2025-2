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
import java.util.Optional;

@Service
@Data
public class CardapioService {

    private final CardapioRepository cardapioRepository;
    private final PedidoRepository pedidoRepository;
    private final IngredienteRepository ingredienteRepository;

    public List<CardapioEntity> findAll(){
        return cardapioRepository.findAll();
    }

    public CardapioEntity save(CardapioEntity cardapio){
        return cardapioRepository.save(cardapio);
    }

    public CardapioEntity findById(Long id){
        return cardapioRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Cardapio não encontrado com id: " + id));
    }

    @Transactional
    public CardapioEntity update(Long id, CardapioEntity novoCardapio) {
        CardapioEntity existente = cardapioRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Cardápio não encontrado"));

        existente.setData(novoCardapio.getData());

        // Atualiza ingredientes
        existente.getIngredientes().clear();
        if (novoCardapio.getIngredientes() != null) {
            for (IngredienteEntity ingrediente : novoCardapio.getIngredientes()) {
                IngredienteEntity ingGerenciado = ingredienteRepository.findById(Math.toIntExact(ingrediente.getId()))
                        .orElseThrow(() -> new RuntimeException("Ingrediente não encontrado: " + ingrediente.getId()));
                existente.getIngredientes().add(ingGerenciado);
            }
        }

        // Atualiza pedidos
        existente.getPedidos().clear();
        if (novoCardapio.getPedidos() != null) {
            for (PedidoEntity pedido : novoCardapio.getPedidos()) {
                PedidoEntity pedidoGerenciado = pedidoRepository.findById(Math.toIntExact(pedido.getId()))
                        .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + pedido.getId()));
                pedidoGerenciado.setCardapio(existente); // mantém vínculo
                existente.getPedidos().add(pedidoGerenciado);
            }
        }

        return cardapioRepository.save(existente);
    }


    public void delete(Long id) {
        if (!cardapioRepository.existsById(Math.toIntExact(id))) {
            throw new RuntimeException("Cardapio não encontrado com id: " + id);
        }
        cardapioRepository.deleteById(Math.toIntExact(id));
    }

    public Optional<CardapioEntity> findByPedidos(PedidoEntity pedido){
        return cardapioRepository.findByPedidos(pedido);
    }

    public List<CardapioEntity> findByIngredientes(IngredienteEntity ingrediente){
        return cardapioRepository.findByIngredientes(ingrediente);
    }
}
