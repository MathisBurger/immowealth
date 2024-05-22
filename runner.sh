openssl genrsa -out rsaPrivateKey.pem 2048
openssl rsa -pubout -in rsaPrivateKey.pem -out publicKey.pem
openssl pkcs8 -topk8 -nocrypt -inform pem -in rsaPrivateKey.pem -outform pem -out privateKey.pem
java -Dmp.jwt.verify.issuer=$JWT_ISSUER -Dquarkus.datasource.username=$DATABASE_USER -Dquarkus.datasource.password=$DATABASE_PASSWORD -Dquarkus.datasource.jdbc.url=$DATABASE_URL -Dquarkus.hibernate-orm.database.generation=$ORM_GENERATION -Dimmowealth.defaultMail=$DEFAULT_MAIL -Dimmowealth.applicationHost=$APPLICATION_HOST -jar server.jar