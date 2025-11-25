FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY . .
RUN mvn -pl microservicio-viaje -am clean package -DskipTests

FROM mcr.microsoft.com/openjdk/jdk:17-ubuntu
WORKDIR /app

COPY --from=build /workspace/microservicio-viaje/target/microservicio-viaje-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8004
ENTRYPOINT ["java", "-jar", "app.jar"]
