# Chatbot 

Aplicação backend **Spring Boot**, construída com Java 21 e Maven. API REST em camadas (Controller → Service → Repository), persistência via JPA/Hibernate com banco **H2** em memória para desenvolvimento.

---

## Tech Stack

| Camada | Tecnologia | Versão | Finalidade |
|---|---|---|---|
| Linguagem | Java (JDK) | 21 | Runtime da aplicação |
| Framework | Spring Boot | 4.1.1 | Base da aplicação / autoconfiguração |
| Build | Maven (com Wrapper `mvnw`) | 3.3.4 | Build e gerenciamento de dependências |
| Web | Spring Web MVC | starter `spring-boot-starter-webmvc` | API REST (controllers, HTTP) |
| Persistência | Spring Data JPA | starter `spring-boot-starter-data-jpa` | Camada de acesso a dados / repositórios |
| ORM | Hibernate ORM | 7.4.5.Final* | Mapeamento objeto-relacional |
| Banco de dados | H2 | 2.4.240* | Banco em memória (desenvolvimento) |
| Console BD | H2 Console | starter `spring-boot-h2console` | Interface web do H2 em `/h2-console` |
| Validação | Bean Validation | starter `spring-boot-starter-validation` | Validação de DTOs/entidades (`@Valid`, `@NotBlank`…) |
| Redução de código | Lombok | (plugin) | Getters/setters/construtores automáticos |
| Dev | Spring Boot DevTools | starter `spring-boot-devtools` | Auto-restart / hot reload em desenvolvimento |
| Servidor embutido | Apache Tomcat | 11.0.24* | Servlet container embutido (porta 8080) |
| Testes | Spring Boot Test Starters | `-data-jpa-test`, `-validation-test`, `-webmvc-test` | Testes de contexto/Web/Data/Validação |

> \* Versões reportadas em tempo de execução pelos logs do Spring Boot (gerenciadas pelo BOM do Spring Boot 4.1.1 — não fixadas manualmente no `pom.xml`).

### Starter usados no `pom.xml`

- `spring-boot-starter-webmvc` — API REST
- `spring-boot-starter-data-jpa` — JPA + Hibernate
- `spring-boot-starter-validation` — Bean Validation
- `spring-boot-h2console` — H2 Console
- `spring-boot-devtools` — hot reload (runtime, opcional)
- `com.h2database:h2` — banco em memória (runtime)
- `org.projectlombok:lombok` — código boilerplate
- `*-test` starters — testes (JUnit + Spring Test)

---

## Estrutura do projeto

```
src/main/java/com/example/chatbot/
├── ChatbotApplication.java      # Classe principal (@SpringBootApplication)
src/main/resources/
└── application.properties       # Configurações (spring.application.name=chatbot)
src/test/java/...                # Testes automatizados
pom.xml                          # Build Maven / dependências
mvnw / mvnw.cmd                  # Maven Wrapper
```

---

## Como executar

Pré-requisito: **JDK 21+** configurado no ambiente.

```bash
# Windows (PowerShell)
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

A aplicação sobe em **http://localhost:8080** (Tomcat, porta 8080).

> Alternativa via VS Code: abra a **Spring Boot Dashboard** → seção **Apps** → botão **▶ Run** no item `chatbot`.

---

## Testes

```bash
.\mvnw.cmd test
```

---

## Banco de dados (H2 em memória)

- URL JDBC: gerada a cada execução (ex.: `jdbc:h2:mem:<uuid>`)
- Console web: **http://localhost:8080/h2-console**
  - JDBC URL, usuário `sa` e senha em branco (padrão)

> Como o banco é **em memória**, os dados são perdidos ao reiniciar a aplicação. Para persistência real, basta adicionar uma dependência de banco (ex.: PostgreSQL/MySQL) e configurar em `application.properties`.

---
