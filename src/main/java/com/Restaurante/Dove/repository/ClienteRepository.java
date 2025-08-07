package com.Restaurante.Dove.repository;

import com.Restaurante.Dove.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{
}
