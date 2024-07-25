# Utiliser une image Maven pour compiler l'application
FROM maven:3.8.6-amazoncorretto-17 AS build
WORKDIR /test-mqtt-grafana

# Copier le fichier POM et les sources de l'application
COPY pom.xml .
COPY src ./src

# Compiler l'application et créer un fichier JAR
RUN mvn clean package -DskipTests

# Utiliser une image JDK pour exécuter l'application
FROM amazoncorretto:17
WORKDIR /test-mqtt-grafana

# Copier le fichier JAR depuis l'étape de compilation
COPY --from=build /test-mqtt-grafana/target/*.jar test-mqtt-grafana.jar

# Démarrer l'application
CMD ["java", "-jar", "test-mqtt-grafana.jar"]
