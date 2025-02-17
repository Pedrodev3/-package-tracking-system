FROM amazoncorretto:21

WORKDIR /app

COPY target/*.jar app.jar
COPY .env.docker /app/.env

EXPOSE 8080

LABEL authors="Pedro"

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/app.jar"]
