FROM openjdk:11-jdk-slim

LABEL maintainer="Koray Bostanci <bostanci.koray@gmail.com>"

WORKDIR /opt/app/
COPY /target/springboot-playground.jar app.jar

EXPOSE 8080
EXPOSE 8090

ENTRYPOINT java -XX:MaxRAMPercentage=70 -XX:MinRAMPercentage=70 -XX:InitialRAMPercentage=70 -jar app.jar