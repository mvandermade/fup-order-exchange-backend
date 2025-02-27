FROM eclipse-temurin:21
RUN mkdir /opt/app
COPY build/libs/made-funicular-postzegel-backend-kotlin-0.0.1-SNAPSHOT.jar /opt/app
CMD ["java", "-jar", "/opt/app/made-funicular-postzegel-backend-kotlin-0.0.1-SNAPSHOT.jar"]
