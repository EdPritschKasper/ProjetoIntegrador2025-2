package com.Restaurante.Dove.service;

import com.Restaurante.Dove.model.CardapioEntity;
import com.Restaurante.Dove.repository.CardapioRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Data
public class CardapioService {

    private final CardapioRepository cardapioRepository;

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

    public CardapioEntity update(Long id, CardapioEntity cardapioAtualizado){
        Optional<CardapioEntity> cardapio = cardapioRepository.findById(Math.toIntExact(id));

        cardapio.get().setData(cardapioAtualizado.getData());

        return cardapio.get();
    }

    public void delete(Long id) {
        if (!cardapioRepository.existsById(Math.toIntExact(id))) {
            throw new RuntimeException("Cardapio não encontrado com id: " + id);
        }
        cardapioRepository.deleteById(Math.toIntExact(id));
    }
}
