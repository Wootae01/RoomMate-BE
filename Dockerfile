
# 기반 이미지 지정
FROM eclipse-temurin:21-jdk-jammy

# 컨테이너 내부에서 작업할 디렉토리 지정
WORKDIR /app

# 빌드된 Jar 파일을 app.jar로 복사
COPY build/libs/*.jar app.jar

# 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]
