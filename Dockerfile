FROM adoptopenjdk/openjdk11
CMD ["./mvnw", "clean", "package"]
ARG JAR_FILE_PATH=target/*.jar
ENTRYPOINT ["java", "-jar", ${JAR_FILE_PATH} ]