FROM openjdk:11-jdk-slim

WORKDIR /opt/app/
COPY /target/springboot-playground.jar app.jar

EXPOSE 8080
EXPOSE 8090

ENTRYPOINT java -jar app.jar