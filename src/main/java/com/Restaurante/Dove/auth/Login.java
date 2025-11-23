package com.Restaurante.Dove.auth;

import com.Restaurante.Dove.model.UsuarioEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Login extends UsuarioEntity {

	private String username;
	private String password;
	
}
