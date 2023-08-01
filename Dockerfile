FROM amazoncorretto:8
VOLUME /tmp
ADD /target/tax-prep-system-api-0.0.1-SNAPSHOT.jar app.jar
CMD [ "java", "-jar", "app.jar" ]
EXPOSE 8080