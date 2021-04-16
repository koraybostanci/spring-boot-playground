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

##### 1. Running the dependencies

The application has **Postgres**, **Redis** and **RabbitMQ** as its dependencies. The easiest way to run those dependencies is to run them as Docker containers. To run the dependencies, the [dockerfiles](https://github.com/koraybostanci/dockerfiles) repository can be used. This repository provides [docker compose files](https://github.com/koraybostanci/dockerfiles/blob/a13c3b898a46c08bdfd0c6922358d7772bc2009f/run-compose.sh#L2) to run those dependencies together.

##### 2. Running the application

If you already have the dependencies running on your local, you can simply execute the command below and run the application. 

```shell
mvn spring-boot:run
```

##### 3. Running the application as a Docker container

Another way to run the application on your local is to run it as a Docker container. To make that process smooth, the application comes with a shell script.

By using the `build.sh`, the application can be built and packaged into a Docker image. Before running the script, make sure you already have the  dependencies running on your local environment.

To run the application as a Docker container, you can go to the main directory of the application and run the command below.

````shell
sh build.sh
````