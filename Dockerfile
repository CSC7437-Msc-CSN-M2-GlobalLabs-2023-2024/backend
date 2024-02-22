FROM maven:3.8.4-openjdk-17 AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN cat /app/src/main/resources/static/appPropertiesContainer.txt > /app/src/main/resources/application.properties

RUN mvn package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar ./app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]