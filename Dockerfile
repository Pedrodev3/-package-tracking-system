FROM amazoncorretto:21
WORKDIR /
COPY target/sist-log.jar sist-log.jar
EXPOSE 8080
LABEL authors="Pedro"
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "/sist-log.jar"]
