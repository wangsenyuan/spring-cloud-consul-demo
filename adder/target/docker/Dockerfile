FROM java:8

VOLUME /tmp
ADD adder-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
ADD start.sh start.sh
RUN bash -c 'chmod +x /start.sh'
EXPOSE 3333
ENTRYPOINT ["/start.sh", " java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
