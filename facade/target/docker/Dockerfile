FROM java:8
VOLUME /tmp
ADD facade-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
ADD wait_all_hosts.sh wait_all_hosts.sh
RUN bash -c 'chmod +x wait_all_hosts.sh'
EXPOSE 2222
CMD ['./wait_all_hosts.sh']
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
