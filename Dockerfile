FROM eclipse-temurin:21 as builder
COPY . /opt/repo
WORKDIR /opt/repo
RUN chmod +x ./gradlew && ./gradlew bootJar

FROM eclipse-temurin:21 as runner
RUN mkdir /opt/app
COPY --from=builder /opt/repo/build/libs/fup-order-exchange-0.0.1-SNAPSHOT.jar /opt/app/fup-order-exchange-backend-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "/opt/app/fup-order-exchange-backend-0.0.1-SNAPSHOT.jar"]
