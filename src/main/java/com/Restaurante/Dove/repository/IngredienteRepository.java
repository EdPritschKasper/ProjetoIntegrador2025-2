package com.Restaurante.Dove.repository;

import com.Restaurante.Dove.model.CardapioEntity;
import com.Restaurante.Dove.model.IngredienteEntity;
import com.Restaurante.Dove.model.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredienteRepository extends JpaRepository<IngredienteEntity, Integer> {

    public List<IngredienteEntity> findByCardapios(CardapioEntity cardapio);

    public List<IngredienteEntity> findByPedidos(PedidoEntity pedido);
}
