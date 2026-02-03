# =========================
# Spring Boot 3.5.x + Java 17
# =========================
FROM eclipse-temurin:17-jdk-jammy

# =========================
# App directory
# =========================
WORKDIR /app

# =========================
# BootJar 결과물 복사
# (build.gradle의 bootJar 결과와 1:1 대응)
# =========================
COPY build/libs/*.jar app.jar

# =========================
# Spring Boot 기본 포트
# =========================
EXPOSE 9090

# =========================
# 실행
# =========================
ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-jar", "app.jar"]
