package com.Restaurante.Dove.repository;

import com.Restaurante.Dove.model.IngredienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredienteRepository  extends JpaRepository<IngredienteEntity, Integer> {
}
