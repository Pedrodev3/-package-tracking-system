FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/*.jar app.jar
COPY .env /app/.env

EXPOSE 8080

LABEL authors="Pedro"

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/app.jar"]
