FROM openjdk:8-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
ENTRYPOINT ["java","-jar",${JAR_FILE}]