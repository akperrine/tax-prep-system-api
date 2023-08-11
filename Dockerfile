FROM amazoncorretto:8
VOLUME /tmp
ADD /target/tax-prep-system-api-0.0.3-SNAPSHOT.jar app.jar
ADD rds-truststore.jks /tmp/certs
CMD [ "java", "-jar", "app.jar" ]
EXPOSE 8080