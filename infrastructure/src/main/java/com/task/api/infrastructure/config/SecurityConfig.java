package com.task.api.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/tasks*").authenticated();
                    auth.requestMatchers("/tasks/*").authenticated();
                    auth.anyRequest().denyAll();
                })
                .oauth2ResourceServer(oauth ->
                        oauth.jwt(cfg -> cfg.jwtAuthenticationConverter(SecurityConfig::jwtConverter))
                )
                .build();
    }

    public static JwtAuthenticationToken jwtConverter(Jwt jwt) {
        var grantedAuthorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return new JwtAuthenticationToken(jwt, grantedAuthorities);
    }
}
