package com.example.chatbot.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tools.jackson.databind.json.JsonMapper;

@SpringBootTest
class JacksonConfigTest {

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    @DisplayName("deve serializar LocalDateTime com exatamente três casas decimais")
    void deveSerializarComTresCasasDecimais() {
        LocalDateTime comNanossegundos = LocalDateTime.of(2025, 3, 14, 9, 26, 53, 123_456_789);

        String json = jsonMapper.writeValueAsString(comNanossegundos);

        assertThat(json).isEqualTo("\"2025-03-14T09:26:53.123\"");
    }

    @Test
    @DisplayName("deve preencher as casas decimais quando a data não tem fração de segundo")
    void devePreencherCasasDecimaisQuandoSemFracao() {
        LocalDateTime semFracao = LocalDateTime.of(2025, 3, 14, 9, 26, 53);

        String json = jsonMapper.writeValueAsString(semFracao);

        assertThat(json).isEqualTo("\"2025-03-14T09:26:53.000\"");
    }
}
