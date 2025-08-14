package com.Restaurante.Dove.repository;

import com.Restaurante.Dove.model.CardapioEntity;
import com.Restaurante.Dove.model.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<PedidoEntity, Integer> {

    List<PedidoEntity> findByCardapio(CardapioEntity cardapio);

    List<PedidoEntity> findByStatus(@Param("status") String status);

    @Query("""
        SELECT COUNT(p.id)
        FROM PedidoEntity p
        JOIN p.cardapio c
        WHERE c.data = :data
    """)
    int contarPedidosPorData(@Param("data") LocalDate data);

    @Query("""
        SELECT COUNT(p.id) * 1.0 / COUNT(DISTINCT c.data)
        FROM PedidoEntity p
        JOIN p.cardapio c
        WHERE FUNCTION('MONTH', c.data) = :mes
    """)
    Double mediaPedidosPorMes(@Param("mes") int mes);
}
