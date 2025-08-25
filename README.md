Spring Boot Shopping Cart Web Application


Overview
----------------------
This is a demo project built to practice Spring Boot and Thymeleaf by creating a simple shopping cart application.

The application uses the following technologies:
Spring Boot, Spring Security, Thymeleaf, Spring Data JPA, Spring Data REST, and Docker.
The database runs on an in-memory H2 instance.

It includes user authentication (login & registration). Each user has a personal shopping cart (session-based). Checkout operations are fully transactional.

Configuration
----------------------
Configuration Files
----------------------

The folder src/resources/ contains all configuration files for the shopping-cart application.

src/resources/application.properties – Main configuration file. You can update the admin credentials and modify the server port here.

Running the Application
----------------------
You can run the application using Maven Wrapper, Maven, or Docker.

Once started, visit:
- http://localhost:8070/home

Default credentials:
----------------------
Admin → username: admin, password: admin
User → username: user, password: password


Running with Maven Wrapper
----------------------
1. Using the Maven Plugin

From the project root, run:

$ chmod +x scripts/mvnw
$ scripts/mvnw spring-boot:run

2. Building & Running Executable JAR
$ scripts/mvnw clean package
$ java -jar target/shopping-cart-0.0.1-SNAPSHOT.jar

Running with Maven
----------------------

First, verify that Java and Maven are properly installed:

$ java -version
java version "1.8.0_102"
Java(TM) SE Runtime Environment (build 1.8.0_102-b14)
Java HotSpot(TM) 64-Bit Server VM (build 25.102-b14, mixed mode)

$ mvn -v
Apache Maven 3.3.9 (...)
Maven home: /usr/local/Cellar/maven/3.3.9/libexec
Java version: 1.8.0_102, vendor: Oracle Corporation

1. Using the Maven Plugin
$ mvn spring-boot:run

2. Building & Running Executable JAR
$ mvn clean package
$ java -jar target/shopping-cart-0.0.1-SNAPSHOT.jar


To stop the application, press CTRL + C.

Running with Docker
----------------------

You can also build and run the application inside a Docker container.

Build Docker Image
$ mvn clean package
$ docker build -t shopping-cart:dev -f docker/Dockerfile .

Run Docker Container
$ docker run --rm -i -p 8070:8070 \
      --name shopping-cart \
      shopping-cart:dev

Using Helper Script
$ chmod +x scripts/run_docker.sh
$ scripts/run_docker.sh

Project Structure
----------------------
Docker
----------------------
The docker/ folder contains:
----------------------
docker/shopping-cart/Dockerfile – Defines how the Docker image is built and how the application is started inside the container.

Utility Scripts
----------------------
scripts/run_docker.sh – Script for building and running the Docker container.

Running Tests
----------------------
From the project root, run:

$ mvn test

Tools & Interfaces
----------------------
HAL REST Browser
----------------------

Visit: http://localhost:8070/ (requires authentication).

H2 Database Console
----------------------

Visit: http://localhost:8070/h2-console

JDBC URL:
----------------------

jdbc:h2:mem:shopping_cart_db


Both the H2 console path and datasource URL can be modified in /src/main/resources/application.properties.
