package com.Restaurante.Dove.repository;

import com.Restaurante.Dove.model.FuncionarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository <FuncionarioEntity, Long>{

    List<FuncionarioEntity> findByNome(String nome);

    Optional<FuncionarioEntity> findByCpf(String cpf);
}
