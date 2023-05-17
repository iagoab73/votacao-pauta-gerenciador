FROM adoptopenjdk:11-jdk-hotspot
WORKDIR /app
COPY target/votacao-pauta-gerenciador-1.0.0.jar app.jar
COPY src/main/resources/application.properties /
COPY src/main/resources/db/changelog/ /changelog/
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]