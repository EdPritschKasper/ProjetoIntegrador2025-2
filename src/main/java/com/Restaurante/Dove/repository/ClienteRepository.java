package com.Restaurante.Dove.repository;

import com.Restaurante.Dove.model.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long>{
}
