# Imagen base ligera con Java 21
FROM eclipse-temurin:21-jdk-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el JAR compilado (ajusta el nombre si cambia)

COPY target/microservicio-viaje-0.0.1-SNAPSHOT.jar app.jar
# Expone el puerto de la aplicación
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
