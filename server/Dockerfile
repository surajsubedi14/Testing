# Use the official Maven image as the base image
FROM maven:3.8.3-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files to the container
COPY pom.xml .
COPY src ./src

# Build the Maven project
RUN mvn clean package -DskipTests

# Use the official OpenJDK image as the base image for the final runtime image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled Java application from the build stage to the container
COPY --from=build /app/target/*.jar ./app.jar

# Expose the port that your application will listen on
EXPOSE 8080

# Define the command to run your application
CMD ["java", "-jar", "app.jar"]
