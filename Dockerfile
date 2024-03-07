FROM node:20-alpine AS webBuild

WORKDIR /web
COPY /web .
RUN npm ci
RUN npm run compile-pipeline
RUN npm run build

FROM gradle:jdk21-alpine AS serverBuild

WORKDIR /app
COPY . .
RUN mkdir ./src/main/resources/META-INF.resources
COPY --from=webBuild ./web/out ./src/main/resources/META-INF.resources
RUN ./gradlew build -Dquarkus.package.type=uber-jar

FROM openjdk:21
WORKDIR /app
COPY --from=serverBuild ./app/build/libs/immowealth-1.0-SNAPSHOT.jar ./server.jar
EXPOSE 8080
CMD ["java", "-jar", "server.jar"]