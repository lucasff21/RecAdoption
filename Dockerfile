FROM ubuntu:latest AS build

LABEL authors="Milena e Lucas"

RUN apt-get update && \
    apt-get install -y openjdk-17-jdk maven && \
    rm -rf /var/lib/apt/lists/*

COPY . .

RUN mvn clean install -DskipTests

FROM openjdk:17-jdk-slim

RUN mkdir -p /tmp/certs

RUN if [ -f /etc/secrets/ca.pem ]; then \
        cp /etc/secrets/ca.pem /tmp/certs/; \
    fi && \
    chmod 644 /tmp/certs/ca.pem && \
    keytool -importcert -noprompt \
            -keystore $JAVA_HOME/lib/security/cacerts \
            -storepass changeit \
            -file /tmp/certs/ca.pem \
            -alias render-ca && \
    rm -rf /tmp/certs

EXPOSE 8080

COPY --from=build /target/recPet-0.0.1.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]