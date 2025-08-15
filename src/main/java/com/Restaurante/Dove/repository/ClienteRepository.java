package com.Restaurante.Dove.repository;

import com.Restaurante.Dove.model.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long>{

    @Query("SELECT COUNT(p) FROM ClienteEntity c JOIN c.pedidos p WHERE c.id = :id")
          long getPedidosById(@Param("id") Long id );


    @Query("SELECT DISTINCT c FROM ClienteEntity c LEFT JOIN FETCH c.pedidos p WHERE c.id = :id")
    ClienteEntity listarTempos(@Param("id") Long id);
}
