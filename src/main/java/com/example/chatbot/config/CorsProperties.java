package com.example.chatbot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {

    /** Origens autorizadas. Aceita curingas, ex.: {@code https://*.dominio.com}. */
    private List<String> allowedOrigins = List.of(
            "http://localhost:3000",  // pnpm dev (frontend atual)
            "http://localhost:5173",  // Vite dev server
            "http://localhost:4173"   // Vite preview
    );

    private List<String> allowedMethods = List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");

    private List<String> allowedHeaders = List.of("Content-Type", "Accept", "Authorization", "X-Trace-Id");

    private List<String> exposedHeaders = List.of("X-Trace-Id");

    private long maxAge = 3600;

    private boolean allowCredentials = false;
}
