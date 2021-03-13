FROM openjdk:11-jdk-slim

WORKDIR /opt/app/
COPY /target/springboot-boilderplate.jar app.jar

EXPOSE 7979
EXPOSE 8080

ENTRYPOINT java -jar app.jar