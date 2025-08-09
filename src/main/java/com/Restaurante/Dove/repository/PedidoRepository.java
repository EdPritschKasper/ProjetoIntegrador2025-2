package com.Restaurante.Dove.repository;

import com.Restaurante.Dove.model.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<PedidoEntity, Integer> {
}
