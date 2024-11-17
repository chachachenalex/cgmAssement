CGM Test Application
This is a Spring Boot application for managing patients and their medical visits. The application uses PostgreSQL as the database and includes functionality for CRUD operations, mapping using Lombok, and API documentation using Swagger.

Features
Patient Management

Add, update, delete, and retrieve patient information.
Each patient has the following attributes:
Name
Surname
Date of Birth
Social Security Number (unique).
Visit Management

Add, update, delete, and retrieve visits for a patient.
Each visit includes:
Date and time of visit.
Type of visit (at home or at the doctor's office).
Reason for the visit (first visit, recurring, or urgent).
Family history (a free text section).
API Documentation

Integrated with Swagger for API exploration and testing.
Accessible at /swagger-ui.html.
Prerequisites
Java 21 or higher
Maven
Docker (for PostgreSQL setup)
Tech Stack
Backend: Spring Boot 3.3.5
Database: PostgreSQL
ORM: Hibernate/JPA
Object Mapping: Lombok, MapStruct
API Documentation: Swagger (Springdoc)
Testing: JUnit 5, Mockito
Containerization: Docker
Project Setup
1. Clone the Repository
bash
Copier le code
git clone https://github.com/chachachenalex/cgmAssement.git
cd cgmtest
2. Configure Database
The application uses PostgreSQL by default. To run the PostgreSQL instance using Docker:

bash
Copier le code
docker run --name cgm-postgres -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password -e POSTGRES_DB=cgmdb -p 5432:5432 -d postgres
Update the application.yml file if needed:

yaml
Copier le code
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/cgmdb
    username: user
    password: password
3. Build and Run the Application
bash
Copier le code
mvn clean install
mvn spring-boot:run
The application will be available at http://localhost:8080.

Dockerizing the Application
The Dockerfile for this project is located in the root directory. To build and run the Docker image:

Build the Docker image:

bash
Copier le code
docker build -t cgm-test-app .
Run the Docker container:

bash
Copier le code
docker run -p 8080:8080 --link cgm-postgres:postgres cgm-test-app
API Endpoints
Patient Endpoints
Method	Endpoint	Description
GET	/api/patients	Get all patients
GET	/api/patients/{id}	Get patient by ID
POST	/api/patients	Create a new patient
PUT	/api/patients/{id}	Update patient information
DELETE	/api/patients/{id}	Delete a patient
Visit Endpoints
Method	Endpoint	Description
GET	/api/visits/{visitId}	Get visit by ID
GET	/api/visits/patients/{patientId}	Get all visits for a specific patient
POST	/api/visits/patients/{patientId}	Create a new visit for a patient
PUT	/api/visits/{visitId}	Update a visit
PATCH	/api/visits/patients/{patientId}/visits/{visitId}	Update visit by patient ID
DELETE	/api/visits/{visitId}	Delete a visit
Testing
Run the tests using Maven:

bash
Copier le code
mvn test
Unit Tests: Covers service and controller layers.
MockMVC Tests: Tests the API endpoints using mocked services.
Swagger API Documentation
Swagger UI is available at:

bash
Copier le code
http://localhost:8080/swagger-ui.html
Known Issues
PostgreSQL setup might require additional configuration for SSL connections.
Lombok requires proper IDE configuration for annotation processing. Make sure to enable this in your IDE.
License
This project is licensed under the MIT License.

