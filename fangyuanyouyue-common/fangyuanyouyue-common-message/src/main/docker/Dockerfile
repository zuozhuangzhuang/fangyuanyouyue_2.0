FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD message-service-0.0.1-SNAPSHOT.jar message-service.jar
#RUN bash -c 'touch /message-service.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/message-service.jar"]
EXPOSE 8761