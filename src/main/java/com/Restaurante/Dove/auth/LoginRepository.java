package com.Restaurante.Dove.auth;

import com.Restaurante.Dove.model.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface LoginRepository extends JpaRepository<UsuarioEntity, Long>{

	public Optional<UsuarioEntity> findByUsername(String login);
	
}
