package com.Restaurante.Dove.repository;

import com.Restaurante.Dove.model.ClienteEntity;
import com.Restaurante.Dove.model.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long>{

    @Query("SELECT COUNT(p) FROM ClienteEntity c JOIN c.pedidos p WHERE c.id = :id")
          long getPedidosById(@Param("id") Long id );
}
