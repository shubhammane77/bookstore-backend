# BookStore backend Application

Welcome to the README for the BookStore backend Application repository. This repository houses a Spring Boot application with spring security for authentication, h2 as database and Swagger Apis.

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)

## Installation

To get started with this project, follow these steps:

1. Clone the repository to your local machine:


   git clone  https://github.com/shubhammane77/bookstore-backend.git
   cd bookstore-backend

2. Install all the dependencies or import project and Intellij

   mvn clean package

3. To start the app on 8089 port.

   cd target 
   
   java -jar  bookstore-0.0.1-SNAPSHOT.jar

4. Access the swagger menu and h2 database

   Open a web browser and go to http://localhost:8089/swagger-ui/index.html#/  to see swagger apis.
   h2 database can be accessed via http://localhost:8089/h2-console


## Project Structure

bookstore-backend/
README.md
controller/ // controller apis
dao/        // JPA dao interfaces
model/      // Model files
security/   // Spring Security configuration
service/   // Service components


Technologies Used
Java 17
Spring Boot
Spring Security
H2 Database
Swagger
Maven


