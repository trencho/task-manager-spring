FROM openjdk:21-jdk-slim

LABEL authors="Aleksandar Trenchevski"

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
