FROM gradle:8.5.0-jdk17 AS build

WORKDIR /home/app

COPY build.gradle.kts settings.gradle.kts ./
COPY src ./src

RUN gradle build

FROM amazoncorretto:17

WORKDIR /usr/local/lib

COPY --from=build /home/app/build/libs/smartpeso-0.0.1-SNAPSHOT.jar ./smartpeso.jar

EXPOSE 9090

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "smartpeso.jar"]
