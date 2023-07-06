FROM eclipse-temurin:20-alpine

WORKDIR /app

COPY target/blogstack-master-service.jar /app/blogstack-master-service.jar

ENTRYPOINT ["java", "-jar", "blogstack-master-service.jar"]