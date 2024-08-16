FROM node:20-alpine AS webBuild

WORKDIR /web
COPY /web .
RUN cat schema.graphql
RUN npm ci --include=dev --force
RUN npm run compile-pipeline
RUN NODE_ENV=production npm run build

FROM gradle:jdk21-alpine AS serverBuild

WORKDIR /app
COPY . .
RUN mkdir ./src/main/resources/META-INF.resources
RUN ./gradlew build -Dquarkus.package.type=uber-jar

FROM openjdk:17-alpine
WORKDIR /app
RUN apk add openssl
COPY --from=serverBuild ./app/build/immowealth-1.0-SNAPSHOT-runner.jar ./server.jar
RUN mkdir static
COPY --from=webBuild ./web/out ./static

ENV DATABASE_PASSWORD=mysecretpassword
ENV DATABASE_USER=postgres
ENV DATABASE_URL=jdbc:postgresql://localhost:5432/immowealth
ENV ORM_GENERATION=update
ENV DEFAULT_MAIL=default@mail.com
ENV APPLICATION_HOST=http://localhost:3000
ENV JWT_ISSUER=http://localhost
ENV MAILER_FROM=noreply@immowealth.local
ENV MAILER_HOST=localhost
ENV MAILER_PORT=1025
ENV MAILER_START_TLS=REQUIRED

EXPOSE 8080
ENTRYPOINT ["runner.sh"]