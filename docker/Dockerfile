FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} backend.jar
ENTRYPOINT ["java", "-jar", "backend.jar"]
