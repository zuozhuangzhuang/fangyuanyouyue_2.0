FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD zuul-server-0.0.1-SNAPSHOT.jar zuul-server.jar
#RUN bash -c 'touch /zuul-server.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/zuul-server.jar"]
EXPOSE 8761