# --- Phase 1: Build ---
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /workspace

# 1. Copie TOUT le contexte du projet parent (common-model, variante-c, etc.)
COPY . .

# 2. Exécute le build depuis la racine, mais ne construit que le module requis.
#    -am = construit aussi les dépendances (common-model)
#    -pl = ne construit que le projet 'variante-c-spring-mvc'
RUN ./mvnw -am -pl variante-c-spring-mvc package -DskipTests

# --- Phase 2: Runtime ---
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# 3. Copie le JAR final depuis le bon dossier 'target'
COPY --from=build /workspace/variante-c-spring-mvc/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]