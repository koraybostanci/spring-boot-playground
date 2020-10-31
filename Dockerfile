FROM openjdk:11-jdk-slim

WORKDIR /opt/app/
COPY /target/*.jar app.jar

EXPOSE 8080
EXPOSE 8081

ENTRYPOINT java -jar app.jar