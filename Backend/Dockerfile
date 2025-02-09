# ====== STAGE 1: Build the application ======
FROM maven:3.9.8-amazoncorretto-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven configuration (pom.xml) into the container
COPY pom.xml .

# Download project dependencies to speed up subsequent builds
RUN mvn dependency:go-offline -B

# Now copy the actual source code
COPY src ./src

# Build the application (skipping tests for faster builds; remove -DskipTests to run tests)
RUN mvn clean package -DskipTests

# ====== STAGE 2: Create the runtime image ======
FROM amazoncorretto:17.0.13-alpine3.17

# Set the working directory in the runtime image
WORKDIR /app

# Copy the packaged JAR file from the 'build' stage
COPY --from=build /app/target/Backend-0.0.1-SNAPSHOT.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]