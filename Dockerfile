FROM eclipse-temurin:25

LABEL authors="HP"

WORKDIR /tmp

COPY ./target/DevOps_Group8-1.0-SNAPSHOT-jar-with-dependencies.jar /tmp/

ENTRYPOINT ["java", "-jar", "DevOps_Group8-1.0-SNAPSHOT-jar-with-dependencies.jar"]