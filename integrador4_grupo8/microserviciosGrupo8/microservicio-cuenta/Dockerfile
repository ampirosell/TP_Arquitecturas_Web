FROM mcr.microsoft.com/openjdk/jdk:17-ubuntu
WORKDIR /app
COPY target/microservicio-cuenta-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8005
ENTRYPOINT ["java", "-jar", "app.jar"]
