# ── Estágio 1: build ─────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copia pom.xml e baixa dependências antes do código-fonte.
# Se apenas o código mudar (sem alterar o pom), esta camada é reutilizada do cache.
COPY pom.xml .
RUN mvn dependency:resolve dependency:resolve-plugins -q --no-transfer-progress

# Copia fontes e gera o JAR executável
COPY src ./src
RUN mvn package -DskipTests -q --no-transfer-progress

# ── Estágio 2: runtime ────────────────────────────────────────────────────────
# Imagem mínima: apenas JRE, sem Maven ou fontes
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
