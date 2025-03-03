# =================================
# Fase 1: BUILD
# =================================
# Usa un'immagine Maven con Java 21.
# Se preferisci un'altra immagine (OpenJDK 21), regola di conseguenza.
FROM maven:3.9.4-eclipse-temurin-21 AS builder

# Imposta la directory di lavoro nel container
WORKDIR /app

# Copia il file pom.xml
COPY pom.xml .

# Scarica le dipendenze (in modo da "cachare" i download e velocizzare build successive)
RUN mvn dependency:go-offline

# Copia il codice sorgente
COPY src ./src

# Compila e crea il JAR (salta i test se vuoi velocizzare)
RUN mvn clean package -DskipTests

# =================================
# Fase 2: RUN
# =================================
# Usa un'immagine Java 21 "slim" o "eclipse-temurin" come base per eseguire l'app
FROM eclipse-temurin:21-jdk

# Copia il JAR generato dalla fase di build
COPY --from=builder /app/target/*.jar app.jar

# Espone la porta su cui gira l'app Spring Boot (8080 di default)
EXPOSE 8080

# Comando di avvio
ENTRYPOINT ["java", "-jar", "/app.jar"]
