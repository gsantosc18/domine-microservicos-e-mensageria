FROM openjdk:17-oracle

WORKDIR /app

COPY .mvn ./.mvn
COPY src ./src
COPY mvnw .
COPY pom.xml .
RUN chmod +x mvnw
RUN ls -lah

ENTRYPOINT /app/mvnw clean spring-boot:run
