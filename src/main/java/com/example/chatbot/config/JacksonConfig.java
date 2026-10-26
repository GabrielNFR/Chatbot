package com.example.chatbot.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.module.SimpleModule;

/**
 * Configuração global de serialização de datas.
 *
 * <p>As datas são expostas em ISO-8601 com exatamente 3 casas decimais
 * (milissegundos), por exemplo {@code 2025-03-14T09:26:53.123}.
 *
 * <p>Motivo: o Jackson 3 serializa {@link LocalDateTime} preservando até 9 casas
 * (nanossegundos) quando o valor as possui. O parser de datas do JavaScript aceita
 * no máximo 3 casas, então valores com mais precisão podem falhar no navegador.
 *
 * <p>A configuração é global (aplicada ao {@code JsonMapper} da aplicação) para que
 * qualquer DTO criado no futuro já nasça com o formato correto, sem precisar de
 * anotação campo a campo.
 */
@Configuration(proxyBeanMethods = false)
public class JacksonConfig {

    /** Padrão de data/hora exposto pela API. */
    public static final String PADRAO_DATA_HORA = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    @Bean
    JsonMapperBuilderCustomizer datasEmIsoComMilissegundos() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern(PADRAO_DATA_HORA);

        return builder -> builder.addModule(new SimpleModule("datas-iso-com-milissegundos")
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formato)));
    }
}
