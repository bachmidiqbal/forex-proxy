FROM gradle:7.4.2-jdk11 AS builder
WORKDIR /app
COPY . ./
RUN gradle test --info
RUN gradle shadowJar

FROM openjdk:16-alpine3.13
WORKDIR /app
COPY --from=builder /app/app/build/libs/app-all.jar ./
CMD ["java", "-jar", "app-all.jar"]
