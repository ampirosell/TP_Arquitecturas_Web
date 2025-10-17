# Dockerfile para la aplicación Spring Boot
FROM openjdk:17-jdk-slim

# Crear directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY inte/pom.xml .
COPY inte/mvnw .
COPY inte/mvnw.cmd .
COPY inte/.mvn .mvn

# Descargar dependencias (para cache de Docker)
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY inte/src ./src

# Construir la aplicación
RUN ./mvnw clean package -DskipTests

# Exponer puerto
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "target/inte-0.0.1-SNAPSHOT.jar"]
