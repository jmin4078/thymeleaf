# Dockerfile

# 1. Build Stage (Gradle 전용 이미지 — JDK 17 툴체인에 맞춤)
FROM gradle:jdk17-alpine AS builder
WORKDIR /app

COPY settings.gradle build.gradle ./
COPY src ./src

# bootJar: spring-boot-gradle-plugin이 repackage한 실행형 JAR 생성
#   - test 태스크에 의존하지 않으므로 별도 스킵 옵션 없이도 테스트를 돌지 않음
#   - plain JAR(-plain.jar)은 assemble/build에서만 생기므로 산출물은 항상 하나
# --no-daemon: 일회성 빌드 컨테이너이므로 데몬 상주 메모리를 낭비하지 않음
RUN gradle bootJar --no-daemon && cp build/libs/*.jar /app/app.jar

# 2. Run Stage (경량 JRE 17 Alpine 기반 멀티 스테이징)
# 의존성 클래스 파일 버전이 major 61(Java 17)이므로 JRE 17로 구동 가능
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 빌드 스테이지에서 패키징된 JAR 파일 복사
COPY --from=builder /app/app.jar app.jar

# Render는 런타임에 PORT 환경변수를 주입하므로 그 값을 그대로 따라감 (로컬 실행 시 기본값 8080)
ENV PORT=8080
EXPOSE ${PORT}

# 프로파일은 이미지에 고정하지 않고 런타임 환경변수로 조작
#   - SPRING_PROFILES_ACTIVE=prod 형태로 주입하면 spring.profiles.active에 그대로 바인딩됨
#     (Spring Boot의 relaxed binding: 점/하이픈 → 언더스코어, 대문자)
#   - 로컬:  docker run -e SPRING_PROFILES_ACTIVE=dev -p 8080:8080 leaf
#   - Render: 대시보드의 Environment 탭에서 SPRING_PROFILES_ACTIVE=prod 추가
#   - 기본값이 필요하면 아래 주석을 해제 (환경변수로 주입한 값이 이 ENV보다 우선함)
# ENV SPRING_PROFILES_ACTIVE=prod

# JVM 실행 옵션
# -XX:MaxRAMPercentage=75.0: 컨테이너 메모리 한도(Render 인스턴스) 기준으로 힙 상한을 잡아 OOM Kill 방지
# --server.port=${PORT}: PORT를 Spring 설정으로 직접 주입 (쉘 폼으로 실행해 변수 치환, exec로 PID 1 유지)
ENTRYPOINT exec java -XX:MaxRAMPercentage=75.0 -jar app.jar --server.port=${PORT}