package com.Restaurante.Dove.repository;

import com.Restaurante.Dove.model.FuncionarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository <FuncionarioEntity, Long>{
}
