package com.Restaurante.Dove.config;

//JwtService.java

import com.Restaurante.Dove.model.UsuarioEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceGenerator {  

	///////////////////////////////////////////////////////
	//Parâmetros para geração do token
    public static final String SECRET_KEY = System.getenv("SECRET_KEY");

    public static final SignatureAlgorithm ALGORITMO_ASSINATURA =
            SignatureAlgorithm.forName(System.getenv("JWT_ALGORITMO"));

    public static final int HORAS_EXPIRACAO_TOKEN =
            Integer.parseInt(System.getenv("HORAS_EXPIRACAO_TOKEN"));

    public Map<String, Object> gerarPayload(UsuarioEntity usuario){
		//AQUI VOCÊ PODE COLOCAR O QUE MAIS VAI COMPOR O PAYLOAD DO TOKEN
		
		Map<String, Object> payloadData = new HashMap<>();
		payloadData.put("username", usuario.getUsername());
		payloadData.put("id", usuario.getId().toString());
		payloadData.put("tipo", usuario.getTipo());
		payloadData.put("outracoisa", "teste");
		payloadData.put("outracoisa2", "teste");
	
		return payloadData;
	}

	///////////////////////////////////////////////////////

	
	
	
	
	public String generateToken(UsuarioEntity usuario) {

		Map<String, Object> payloadData = this.gerarPayload(usuario);

		return Jwts
				.builder()
				.setClaims(payloadData)
				.setSubject(usuario.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(new Date().getTime() + 3600000 * this.HORAS_EXPIRACAO_TOKEN))
				.signWith(getSigningKey(), this.ALGORITMO_ASSINATURA)
				.compact();
	}

	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}


	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(this.SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}


	public String extractUsername(String token) {
		return extractClaim(token,Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

}
