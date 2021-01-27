FROM maven:3.5.2-jdk-8-alpine AS MAVEN_BUILD
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn dependency:go-offline
RUN mvn package -DskipTests

FROM openjdk:8-jre-alpine
ENV spring_profiles_active=prod
WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/fms-parser-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java", "-jar", "fms-parser-0.0.1-SNAPSHOT.jar"]