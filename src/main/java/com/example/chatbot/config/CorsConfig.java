package com.example.chatbot.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties(CorsProperties.class)
@RequiredArgsConstructor
public class CorsConfig implements WebMvcConfigurer {

    private final CorsProperties cors;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                // Usamos allowedOriginPatterns (em vez de allowedOrigins) por aceitar
                // curingas (ex.: https://*.dominio.com) e por ser compatível com
                // allowCredentials(true) — combinação inválida com allowedOrigins("*").
                .allowedOriginPatterns(cors.getAllowedOrigins().toArray(String[]::new))
                .allowedMethods(cors.getAllowedMethods().toArray(String[]::new))
                .allowedHeaders(cors.getAllowedHeaders().toArray(String[]::new))
                .exposedHeaders(cors.getExposedHeaders().toArray(String[]::new))
                .maxAge(cors.getMaxAge())
                .allowCredentials(cors.isAllowCredentials());
    }
}
