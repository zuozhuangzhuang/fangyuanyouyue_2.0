FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD user-service-0.0.1-SNAPSHOT.jar user-service.jar
#RUN bash -c 'touch /user-service.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/user-service.jar"]
EXPOSE 8761