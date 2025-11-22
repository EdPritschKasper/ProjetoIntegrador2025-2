package com.Restaurante.Dove.repository;

import com.Restaurante.Dove.model.TipoUsuario;
import com.Restaurante.Dove.model.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    List<UsuarioEntity> findByNome(String nome);

    Optional<UsuarioEntity> findByEmail(String email);

    Optional<UsuarioEntity> findByCpf(String cpf);

    List<UsuarioEntity> findByTipo(TipoUsuario tipo);

    @Query("SELECT COUNT(p) FROM UsuarioEntity u JOIN u.pedidos p WHERE u.id = :id")
    long getPedidosById(@Param("id") Long id);

    @Query("SELECT DISTINCT u FROM UsuarioEntity u LEFT JOIN FETCH u.pedidos p WHERE u.id = :id")
    UsuarioEntity listarTempos(@Param("id") Long id);
}
