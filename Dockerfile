FROM amazoncorretto:21-al2023-jdk
RUN yum -y install findutils
WORKDIR /app
COPY . .
EXPOSE 8080
CMD ["./gradlew", "bootRun"]
