## Description

A playground project for Spring Boot application, based on;

- Java 11
- Spring Boot 2.4.x
- RabbitMQ 3.8.x
- Redis 6

## Building the application

### By Maven
```shell
mvn clean install -Dmaven.test.skip=true
```

### By Docker
```docker
docker build . -t springboot-playground:0.0.1
```

## Running the application

### How to run dependencies

The easiest way to run dependencies on the local environment is to run them via Docker.

**RabbitMQ**
```docker
docker run -d -it --name local-rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

**Redis**
````docker
docker run -d -it --name local-redis -p 6379:6379 redis:6
````

**HttpBin**
````docker
docker run -d -it --name local-httpbin -p 8001:80 kennethreitz/httpbin
````

### How to run the application

### By Maven
```shell
mvn spring-boot:run
```

### By Docker
```docker
docker run -d -it --name local-springboot-playground -p 7979:7979 -p 8080:8080 springboot-playground:0.0.1
```