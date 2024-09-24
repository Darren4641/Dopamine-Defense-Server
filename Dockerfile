# OpenJDK 베이스 이미지를 사용합니다.
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 로컬에서 빌드된 JAR 파일을 Docker 이미지로 복사합니다.
COPY build/libs/*.jar app.jar

ENV DB_URL=jdbc:mysql://semtle.catholic.ac.kr:3306/dodidb
ENV DB_USERNAME=dodi
ENV DB_PASSWORD=AdjhbrSKF

# JAR 파일을 실행합니다.
ENTRYPOINT ["java", "-jar", "app.jar"]


## docker build --platform linux/amd64 -t dodiserver:0.0.19 .
## docker run -d -p 8086:8080 dodiserver:0.0.19

## docker buildx build --platform linux/amd64 -t 286306741089.dkr.ecr.ap-northeast-2.amazonaws.com/dodiserver:0.0.19 .
## docker push 286306741089.dkr.ecr.ap-northeast-2.amazonaws.com/dodiserver:0.0.19