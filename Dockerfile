# Dockerfile for a Multi-Module Maven Project using Java 21

# --- Build Stage ---
# This stage uses a full Maven and JDK image to compile the application and build the JAR file.
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set the working directory
WORKDIR /build

# --- Dependency Resolution ---
# First, copy only the pom.xml files to leverage Docker's layer caching.
# This layer will only be rebuilt if a pom.xml file changes.

# Copy the parent pom.xml
COPY pom.xml .

# Copy the pom.xml for each module
COPY core/pom.xml ./core/
COPY webhook-handler-service/pom.xml ./webhook-handler-service/
COPY pr-analyzer-service/pom.xml ./pr-analyzer-service/
COPY ai-reviewer-service/pom.xml ./ai-reviewer-service/
COPY comment-publisher-service/pom.xml ./comment-publisher-service/

# Download all project dependencies
RUN mvn dependency:go-offline

# --- Source Code Compilation ---
# Now, copy the source code and build the project.
# This layer will be rebuilt if the source code changes.

# Copy the source code for each module
COPY core/src ./core/src
COPY webhook-handler-service/src ./webhook-handler-service/src
COPY pr-analyzer-service/src ./pr-analyzer-service/src
COPY ai-reviewer-service/src ./ai-reviewer-service/src
COPY comment-publisher-service/src ./comment-publisher-service/src

# Build the entire multi-module project.
RUN mvn clean package -DskipTests

# --- Run Stage ---
# This stage starts with a fresh, lightweight Java image for the final container.
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the specific executable JAR from the correct module's target directory.
# IMPORTANT: Change 'webhook-handler-service' to the name of the service this Dockerfile is for.
# You will need a separate Dockerfile for each runnable service.
COPY --from=build /build/webhook-handler-service/target/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# The command that will be executed when the container starts.
ENTRYPOINT ["java", "-jar", "app.jar"]
