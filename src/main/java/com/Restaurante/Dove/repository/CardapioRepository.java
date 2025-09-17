package com.Restaurante.Dove.repository;

import com.Restaurante.Dove.model.CardapioEntity;
import com.Restaurante.Dove.model.IngredienteEntity;
import com.Restaurante.Dove.model.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CardapioRepository  extends JpaRepository<CardapioEntity, Integer> {

    public Optional<CardapioEntity> findByPedidos(PedidoEntity pedido);

    public List<CardapioEntity> findByIngredientes(IngredienteEntity ingrediente);

    Optional<CardapioEntity> findByData(LocalDate data);
}
