#!/usr/bin/env bash

build_jar() {
  echo "Building Jar"
  mvn clean install -Dmaven.test.skip=true
}

build_docker() {
  echo "Building Docker image"
  docker build -f Dockerfile .
}

docker_compose_down() {
  echo "docker_compose_down"
  docker-compose down
}

docker_compose_up() {
  echo "docker_compose_up"
  docker-compose up -d --build
}

build_jar
#build_docker
#docker_compose_down
docker_compose_up