FROM maven:3.8.6-openjdk-8-slim AS build

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN mvn clean install spring-boot:repackage

FROM openjdk:8-jre-slim

WORKDIR /app

COPY --from=build /app/target/visual-0.0.1-SNAPSHOT.jar /app/visualData.jar

EXPOSE 8090

ENTRYPOINT ["java", "-Djavax.net.debug=ssl:handshake:verbose", "-Dhttps.protocols=TLSv1.2", "-jar", "visualData.jar"]
