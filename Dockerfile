FROM openjdk:11

VOLUME /tmp

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENV JAVA_OPTS=""

ENTRYPOINT ["java", "-jar","/app.jar"]