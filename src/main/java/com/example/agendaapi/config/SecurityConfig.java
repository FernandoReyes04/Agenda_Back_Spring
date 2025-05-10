package com.example.agendaapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import java.util.List;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desactiva protección CSRF
            .cors(cors -> cors.configurationSource(request -> {
                var config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:5174")); // Puerto de tu frontend
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(List.of("Content-Type", "Authorization"));
                config.setExposedHeaders(List.of("X-Custom-Header")); // opcional
                config.setMaxAge(3600L);
                return config;
            }))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sin estado
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/contact/**").permitAll() // Permitir acceso a contactos
                .requestMatchers("/api/event/**").permitAll() // Permitir acceso a eventos
                .requestMatchers("/api/reminder/**").permitAll() // Permitir acceso a recordatorios
                .anyRequest().permitAll() // O dejar todo abierto temporalmente
            );

        return http.build();
    }
}