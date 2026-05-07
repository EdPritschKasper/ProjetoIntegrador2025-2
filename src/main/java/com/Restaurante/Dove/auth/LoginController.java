package com.Restaurante.Dove.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
public class LoginController {

	@Autowired
	private LoginService loginService;

	@PostMapping
	public ResponseEntity<String> logar(@RequestBody Login login) {

		String token = loginService.logar(login);
		return new ResponseEntity<>(token, HttpStatus.OK);
		
	}

}
