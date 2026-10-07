FROM eclipse-temurin:25
LABEL authors="HP"
COPY ./target/DevOps_Group8-1.0-SNAPSHOT.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "DevOps_Group8-1.0-SNAPSHOT.jar-with-dependencies.jar"]