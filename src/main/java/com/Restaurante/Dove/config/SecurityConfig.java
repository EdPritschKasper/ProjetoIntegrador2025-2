package com.Restaurante.Dove.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http    
		.csrf(AbstractHttpConfigurer::disable)
		.cors(AbstractHttpConfigurer::disable)
		.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/api/login").permitAll()
				.requestMatchers("/api/usuario").permitAll()
                        .requestMatchers("/api/usuario/findByUsername").permitAll()
                        .requestMatchers("/api/usuario/findByEmail").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/usuario/senha/**").permitAll()
                        .anyRequest().authenticated()
        )
		.oauth2ResourceServer(oauth2 -> 
			oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
		)
		.sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}

	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		converter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
		return converter;
	}

	static class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
		@Override
		@SuppressWarnings("unchecked")
		public Collection<GrantedAuthority> convert(Jwt jwt) {
			if (jwt.getClaims() == null) {
				return Collections.emptyList();
			}
			final Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
			if (realmAccess == null || !realmAccess.containsKey("roles")) {
				return Collections.emptyList();
			}
			final List<String> roles = (List<String>) realmAccess.get("roles");
			return roles.stream()
					.map(roleName -> "ROLE_" + roleName) // Adiciona prefixo ROLE_ para funcionar com @PreAuthorize("hasRole('dove_admin')")
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());
		}
	}

	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.setAllowedOriginPatterns(Arrays.asList("*"));
		config.setAllowedHeaders(Arrays.asList(HttpHeaders.AUTHORIZATION,HttpHeaders.CONTENT_TYPE,HttpHeaders.ACCEPT));
		config.setAllowedMethods(Arrays.asList(HttpMethod.GET.name(),HttpMethod.POST.name(),HttpMethod.PUT.name(),HttpMethod.DELETE.name()));
		config.setMaxAge(3600L);
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(source));
		bean.setOrder(-102);
		return bean;
	}
}
