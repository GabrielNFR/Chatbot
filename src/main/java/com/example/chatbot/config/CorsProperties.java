package com.example.chatbot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Propriedades de configuração de CORS, mapeadas de {@code app.cors.*}
 * em {@code application.properties}.
 *
 * <p>Os valores abaixo são os <b>padrões de segurança</b>, usados caso as
 * propriedades não estejam definidas (ex.: em testes, cujo
 * {@code application.properties} não herda o do {@code src/main}).
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {

    /** Origens autorizadas. Aceita curingas, ex.: {@code https://*.dominio.com}. */
    private List<String> allowedOrigins = List.of(
            "http://localhost:5173",  // Vite dev server
            "http://localhost:4173"   // Vite preview
    );

    /** Métodos HTTP permitidos nas requisições cross-origin. */
    private List<String> allowedMethods = List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");

    /** Headers que o frontend pode enviar. */
    private List<String> allowedHeaders = List.of("Content-Type", "Accept", "Authorization", "X-Trace-Id");

    /** Headers que o frontend pode ler na resposta (o restante fica oculto). */
    private List<String> exposedHeaders = List.of("X-Trace-Id");

    /** Tempo (em segundos) que o navegador pode reaproveitar a resposta do preflight. */
    private long maxAge = 3600;

    /**
     * Permite o envio de credenciais (cookies/Authorization). Mantido desligado
     * enquanto não houver autenticação.
     */
    private boolean allowCredentials = false;
}
