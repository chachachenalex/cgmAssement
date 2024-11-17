# Use an official OpenJDK image as the base image
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the application JAR file into the container
COPY target/cgmtest-0.0.1-SNAPSHOT.jar app.jar

# Expose the application's port
EXPOSE 8080

# Environment variables for PostgreSQL
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/cgmdb
ENV SPRING_DATASOURCE_USERNAME=user
ENV SPRING_DATASOURCE_PASSWORD=password

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
