# springboot-starter

A starter project for Spring Boot applications.

**Based on**
- Java 11
- Spring Boot 2.4.0
- RabbitMQ 3.8.7

**How to**

|Action|Command|
|---|---|
|Build | mvn clean install -Dmaven.test.skip=true |
|Run | mvn spring-boot:run |
|Build a Docker image | docker build . -t springboot-starter:0.0.1 |
|Run as Docker container | docker run -d -it --name springboot-starter -p 8080:8080 -p 8081:8081 springboot-starter:0.0.1 |