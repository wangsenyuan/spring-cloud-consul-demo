FROM java:8
VOLUME /tmp
ADD facade-0.0.1-SNAPSHOT.jar app.jar
ADD wait-for-it.sh wait-for-it.sh
RUN bash -c 'chmod +x /wait-for-it.sh'
RUN bash -c 'touch /app.jar'
EXPOSE 2222
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
