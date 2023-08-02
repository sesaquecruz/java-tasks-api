FROM gradle:8.2-jdk17-alpine as build
WORKDIR /opt/app
COPY . .
RUN gradle bootJar

FROM eclipse-temurin:17.0.7_7-jre-alpine
COPY --from=build /opt/app/infrastructure/build/libs/*.jar /opt/app/application.jar
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
CMD java -jar /opt/app/application.jar
