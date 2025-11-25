FROM mcr.microsoft.com/openjdk/jdk:17-ubuntu
WORKDIR /app

# Copiamos el JAR ya construido
COPY target/groq-client-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8011
ENTRYPOINT ["java", "-jar", "app.jar"]
