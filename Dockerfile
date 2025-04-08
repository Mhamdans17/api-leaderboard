# Stage 1 : Base Image Maven from docker hub
FROM maven:3.8.5-openjdk-17 AS builder
# Set Working Directory
WORKDIR /app
# Add or Copy file pom.xml
ADD ./pom.xml /app
# Add or Copy file source code
ADD ./src /app/src/
# Build Use Maven Clean and Package
RUN mvn clean package
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
