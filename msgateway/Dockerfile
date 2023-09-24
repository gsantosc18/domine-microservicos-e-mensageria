FROM openjdk:17-oracle

WORKDIR /app

COPY .mvn ./.mvn
COPY src ./src
COPY mvnw .
COPY pom.xml .
RUN chmod +x mvnw
RUN ls -lah

EXPOSE 8080

ENTRYPOINT /app/mvnw clean spring-boot:run
