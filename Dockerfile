# ---------------------------------------------------------------------------
# Estagio 1: build (Maven + JDK 21)
# ---------------------------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src ./src

# O cache do BuildKit em /root/.m2 evita rebaixar todas as dependencias do
# Maven a cada build. Sem ele, cada build parte de um ~/.m2 vazio.
RUN --mount=type=cache,target=/root/.m2,sharing=locked \
    mvn -B clean package -DskipTests

# ---------------------------------------------------------------------------
# Estagio 2: runtime (JRE 21 enxuto, sem Maven nem codigo-fonte)
# ---------------------------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

COPY --from=builder /app/target/*.jar app.jar

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=40.0"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]