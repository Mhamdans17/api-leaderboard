# Stage 1: Build with Maven
FROM maven:3.8.5-openjdk-17 AS builder

WORKDIR /app
# Copy ONLY pom.xml first (untuk caching layer)
COPY pom.xml .
# Download dependencies (cached selama pom.xml tidak berubah)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src
# Build aplikasi
RUN mvn clean package -DskipTests

# Stage 2: Run with JRE
FROM openjdk:17-slim
WORKDIR /app
# Stage 2 : Base Image SDK Java:17 from docker hub
FROM openjdk:17-slim
# Set Working Directory
WORKDIR /app
# Copy .jar From Builder
COPY --from=builder /app/target/Apiliga-0.0.1-SNAPSHOT.jar /app/Apiliga-0.0.1-SNAPSHOT.jar
# Set Port to Expose
EXPOSE 8080
# Set Time Zone
ENV TZ=Asia/Jakarta
# RUN or execute app
ENTRYPOINT ["java","-jar","/app/Apiliga-0.0.1-SNAPSHOT.jar"]
