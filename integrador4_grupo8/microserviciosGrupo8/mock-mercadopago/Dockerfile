# Etapa de build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos pom.xml para descargar dependencias
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline

# Copiamos el código fuente y compilamos
COPY src ./src
RUN mvn -q -e -DskipTests package

# Etapa final
FROM mcr.microsoft.com/openjdk/jdk:17-ubuntu
WORKDIR /app

COPY --from=build /app/target/mock-mercadopago-1.0.0.jar app.jar

EXPOSE 8009
ENTRYPOINT ["java", "-jar", "app.jar"]
