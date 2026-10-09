package com.example.chatbot.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração de CORS para permitir que o frontend (SPA em repositório/origem
 * separada) consuma a API deste backend.
 *
 * <p>Todos os parâmetros são externalizados em {@code application.properties}
 * (prefixo {@code app.cors.*}) e podem ser sobrescritos por variáveis de
 * ambiente em cada ambiente, sem necessidade de recompilar.
 */
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
