# Etapa 1: Compilación
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copiar archivos base de Maven
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Dar permisos de ejecución explícitos al wrapper de Maven
RUN chmod +x mvnw

# Copiar el código fuente completo y compilar omitiendo tests
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Etapa 2: Ejecución ligera
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
