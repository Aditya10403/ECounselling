FROM openjdk:17-jdk-slim

ENV SPRING_PROFILES_ACTIVE=prod

WORKDIR /app

COPY target/ECounselling-*.jar app.jar

COPY src/main/resources/application.yml .
COPY src/main/resources/application-prod.yml .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:./application.yml,file:./application-prod.yml" ]