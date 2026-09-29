# Etapa 1: Compilación usando el Wrapper del proyecto y Java 17
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copiar archivos de configuración y dependencias primero
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

# Copiar el código fuente y empaquetar
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Etapa 2: Imagen ligera de ejecución
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
