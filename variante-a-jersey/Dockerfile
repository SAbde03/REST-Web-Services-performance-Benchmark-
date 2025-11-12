# --- Phase 1 : Construction (Build) ---
# Utilise une image complète JDK 17 + Maven
FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /workspace

# Copie le Maven wrapper et le pom.xml
COPY . .

# Télécharge les dépendances
RUN ./mvnw -am -pl variante-c-spring-mvc package -DskipTests

# Copie le code source
COPY src ./src

# Construit l'application (.war) et saute les tests
RUN ./mvnw package -DskipTests

# Télécharge l'agent JMX Exporter dans le dossier target
RUN ./mvnw dependency:copy -Dartifact=io.prometheus.jmx:jmx_prometheus_javaagent:0.20.0 -DoutputDirectory=target

# --- Phase 2 : Exécution (Runtime) ---
# Utilise une image Tomcat 10.1 (compatible Java 17 et Jakarta EE)
FROM tomcat:10.1-jdk17

# Crée le dossier pour l'agent JMX
RUN mkdir /jmx-agent

# Copie l'agent JMX téléchargé depuis la phase de build
COPY --from=build /workspace/target/jmx_prometheus_javaagent-0.20.0.jar /jmx-agent/jmx_prometheus_javaagent.jar

COPY variante-a-jersey/jmx-config/config.yml /jmx-agent/config.yml
# Supprime l'application par défaut et déploie notre .war en tant que ROOT
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /workspace/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Expose le port de Tomcat
EXPOSE 8080

# Note : Le flag CATALINA_OPTS pour activer l'agent sera défini
# dans le docker-compose.yml pour plus de flexibilité.