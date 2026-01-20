# Base 이미지
FROM eclipse-temurin:17-jre-jammy

COPY build/libs/*.jar /app.jar

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# 애플리케이션 실행 명령
ENTRYPOINT ["java", "-Xms128m", "-Xmx256m", "-jar", "/app.jar"]
