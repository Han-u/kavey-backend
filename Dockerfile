FROM openjdk:11-jdk
ARG JAR_FILE=./build/libs/Back-End-0.0.1-SNAPSHOT.jar
# 시스템 진입점 정의
ENTRYPOINT ["java","-jar","./build/libs/Back-End-0.0.1-SNAPSHOT.jar"]