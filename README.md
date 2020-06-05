# springboot-starter

A starter project for Spring Boot applications.

**Based on**
- Java 11
- Spring Boot 2.3

**Dependencies**
- Micrometer with Prometheus
- Lombok
- ModelMapper

**How to**

|Action|Command|Explanation|
|---|---|---|
|build-maven|mvn clean package|Builds the application using maven|
|run-maven|mvn spring-boot:run|Runs the application using maven|
|build-docker-image|mvn spring-boot:build-image|Builds the application as a docker image|
|run-docker-container|docker run -it --rm \ <br>  --name springboot-starter \ <br>-p 7979:7979 \ <br>-p 8080:8080 \ <br>springboot-starter:0.0.1 <br>|Runs the application inside docker container|