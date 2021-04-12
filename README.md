### Description

A playground project for Spring Boot application, based on;

- Java 11
- Spring Boot 2.4.x
- RabbitMQ 3.8.x
- Redis 6

### Building the application

#### Building a Jar by Maven
```shell
mvn clean install -Dmaven.test.skip=true
```

#### Building a Docker image
```shell
docker build . -t springboot-playground:0.0.1
```

### Running the application

#### On Local

##### 1. Only the application

If you already have the dependencies running on your local, you can simply execute the command below and run the application. 

```shell
mvn spring-boot:run
```

##### 2. Application and its dependencies together

The easiest way to run the application on your local is to run it as a Docker container. To make that process smooth, the application comes with a shell script.

By using the `build.sh`, the application can be built and packaged into a Docker image. Then, the dependencies can be run as Docker containers. Finally, the application can be run as a Docker container.

To run the application and its dependencies as Docker containers, you can go to the main directory of the application and run the command below.

````shell
sh build.sh
````