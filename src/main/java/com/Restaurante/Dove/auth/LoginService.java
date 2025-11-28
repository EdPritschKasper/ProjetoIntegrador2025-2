//AuthenticationService.java
package com.Restaurante.Dove.auth;

import com.Restaurante.Dove.config.JwtServiceGenerator;
import com.Restaurante.Dove.model.UsuarioEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

	@Autowired
	private LoginRepository repository;
	@Autowired
	private JwtServiceGenerator jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;


	
	public String logar(Login login) {

		String token = this.gerarToken(login);
		return token;

	}



	public String gerarToken(Login login) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						login.getUsername(),
						login.getPassword()
						)
				);
		UsuarioEntity user = repository.findByUsername(login.getUsername()).get();
		String jwtToken = jwtService.generateToken(user);
		return jwtToken;
	}


}
