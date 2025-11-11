FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY . .

RUN mvn -pl microservicio-cuenta -am clean package -DskipTests

FROM mcr.microsoft.com/openjdk/jdk:17-ubuntu
WORKDIR /app

COPY --from=build /workspace/microservicio-cuenta/target/microservicio-cuenta-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8005

ENTRYPOINT ["java", "-jar", "app.jar"]
