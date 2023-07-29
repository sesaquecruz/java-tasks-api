package com.task.api.infrastructure.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@TestConfiguration
public class TestJwtConfig {
    public static final String SUB = UUID.randomUUID().toString();
    public static final String TOKEN = UUID.randomUUID().toString();
    private static final Jwt JWT = new Jwt(
            TOKEN,
            Instant.now(),
            Instant.now().plusSeconds(600),
            Map.of("alg", "none"),
            Map.of(
                    "sub", SUB,
                    "realm_access", Map.of("roles", List.of("TASK_API"))
            )
    );

    @Bean
    public static JwtDecoder jwtDecoder() {
        return TestJwtConfig::jwt;
    }

    private static Jwt jwt(String token) {
        assertThat(token).isEqualTo(TOKEN);
        return JWT;
    }
}
