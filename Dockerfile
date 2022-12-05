FROM openjdk:11-jdk
# JAR_FILE 변수 정의 -> 기본적으로 jar file이 2개이기 때문에 이름을 특정해야함
ARG JAR_FILE=./build/libs/Back-End-0.0.1-SNAPSHOT.jar
# 시스템 진입점 정의
ENTRYPOINT ["java","-jar",${JAR_FILE}]