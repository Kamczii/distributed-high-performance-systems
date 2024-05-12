FROM maven:3.9.6-eclipse-temurin-17 AS DEPS
ENV HOME=/usr/src/app
ENV SERVICE=TourOperator
WORKDIR $HOME
COPY ./Common/ $HOME/Common
RUN mvn -f $HOME/Common/pom.xml clean install

COPY ./${SERVICE}/pom.xml $HOME/pom.xml
RUN mvn dependency:go-offline -pl :${SERVICE}

FROM maven:3.9.6-eclipse-temurin-17 AS BUILDER
ENV HOME=/usr/src/app
ENV SERVICE=TourOperator

WORKDIR $HOME

COPY --from=deps /root/.m2/ /root/.m2/
COPY --from=deps $home/ $home/

COPY ./${SERVICE}/ $HOME/

RUN mvn package -DskipTests -T 1.5C -am -q -pl :${SERVICE}

FROM openjdk:17 AS BUILD
ENV HOME=/usr/src/app
ENV SERVICE=TourOperator

COPY --from=builder $HOME/target/${SERVICE}.jar /app/${SERVICE}.jar
WORKDIR /app
EXPOSE 8080
EXPOSE 5003
ENTRYPOINT java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5003 -jar ${SERVICE}.jar