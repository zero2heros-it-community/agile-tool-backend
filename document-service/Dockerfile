FROM eclipse-temurin:17-alpine

COPY target/*.jar doc.jar

EXPOSE 8081/tcp

ENTRYPOINT ["java", "-jar","doc.jar"]