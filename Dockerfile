FROM ubuntu:latest AS build

LABEL authors="Milena e Lucas"

RUN apt-get update && \
    apt-get install -y openjdk-17-jdk maven && \
    rm -rf /var/lib/apt/lists/*

COPY . .

RUN mvn clean install -DskipTests

FROM openjdk:17-jdk-slim

RUN mkdir -p /etc/secrets && \
    chmod 755 /etc/secrets

COPY src/main/resources/certs/ca.pem /etc/secrets/

RUN chmod 644 /etc/secrets/ca.pem && \
    keytool -importcert -noprompt \
            -keystore $JAVA_HOME/lib/security/cacerts \
            -storepass changeit \
            -file /etc/secrets/ca.pem \
            -alias render-ca && \
    rm -rf /var/lib/apt/lists/*

EXPOSE 8080

COPY --from=build /target/recPet-0.0.1.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]