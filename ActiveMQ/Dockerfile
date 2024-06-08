FROM maven:3.9.6-eclipse-temurin-22 AS BUILDER
WORKDIR /opt/src
COPY pom.xml .
RUN mvn verify clean --fail-never
COPY src src
RUN mvn clean package -DskipTests

FROM openjdk:22 AS BUILD
COPY --from=BUILDER /opt/src/target/ActiveMQ.jar /opt/app.jar
ENTRYPOINT ["java","-jar","/opt/app.jar"]