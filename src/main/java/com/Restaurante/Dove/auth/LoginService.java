package com.Restaurante.Dove.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class LoginService {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    public String logar(Login login) {
        RestTemplate restTemplate = new RestTemplate();
        
        String tokenEndpoint = issuerUri + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("username", login.getUsername());
        formData.add("password", login.getPassword());
        formData.add("grant_type", "password");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        try {
            @SuppressWarnings("rawtypes")
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenEndpoint, request, Map.class);
            var body = response.getBody();
            if (response.getStatusCode().is2xxSuccessful() && body != null) {
                return (String) body.get("access_token");
            }
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Credenciais inválidas ou erro no Keycloak: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao comunicar com o Keycloak: " + e.getMessage());
        }

        throw new RuntimeException("Erro ao autenticar com Keycloak");
    }
}
