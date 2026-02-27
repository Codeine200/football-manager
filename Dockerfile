# ==============================
# Build stage
# ==============================
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# ==============================
# Runtime stage
# ==============================
FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENV UPLOADS_TEAMS_PHOTOS_FOLDER=/football-manager/uploads/teams
ENV UPLOADS_PLAYERS_PHOTOS_FOLDER=/football-manager/uploads/players

RUN addgroup spring \
    && adduser -G spring -s /bin/sh -D spring \
    && install -d -m 750 -o spring -g spring "$UPLOADS_TEAMS_PHOTOS_FOLDER" \
    && install -d -m 750 -o spring -g spring "$UPLOADS_PLAYERS_PHOTOS_FOLDER"
USER spring

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]