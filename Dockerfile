FROM eclipse-temurin:25

LABEL authors="HP"

WORKDIR /app

COPY ./target/DevOps_Group8-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar", "--batch"]