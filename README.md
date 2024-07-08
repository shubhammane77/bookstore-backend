Project Title
Brief description of the project.

Table of Contents
Introduction
Technologies Used
Setup
Usage
Contributing
License
Introduction
Provide a brief introduction to the project. What does it do? Why was it created?

Technologies Used
List the technologies/frameworks/libraries used in the project. For example:

Spring Boot
Maven
Java 17
Spring Data JPA
H2 Database (or any other database used)
Swagger Apis


Setup
Prerequisites
Make sure you have the following installed:

Java JDK 17
Maven
Installation
Clone the repository:

git clone  https://github.com/shubhammane77/bookstore-backend.git

Navigate into the project directory:

cd bookstore-backend

Build the project using Maven or import the project in Intellij:

mvn clean package

Run the project with

cd target
java -jar  bookstore-0.0.1-SNAPSHOT.jar

Access the application:
Open a web browser and go to http://localhost:8089/swagger-ui/index.html#/  to see swagger apis.
h2 database can be accessed via http://localhost:8089/h2-console
