FROM maven:3.9-eclipse-temurin-17 AS build

LABEL authors="Milena e Lucas"

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

COPY . .

RUN mvn clean install -DskipTests

FROM eclipse-temurin:17-jre-alpine

LABEL authors="Milena e Lucas"

RUN mkdir -p /usr/local/share/ca-certificates
COPY docker-entrypoint.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/docker-entrypoint.sh

EXPOSE 8080

COPY --from=build /app/target/recPet-0.0.1.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]