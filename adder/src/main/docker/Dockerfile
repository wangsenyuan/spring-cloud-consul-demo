FROM java:8
VOLUME /tmp
ADD adder-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 3333
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
