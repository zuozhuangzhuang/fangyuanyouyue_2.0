FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD fangyuanyouyue-microservice-forum-1.0-SNAPSHOT.jar fangyuanyouyue-microservice-forum.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/fangyuanyouyue-microservice-forum.jar"]
#EXPOSE 8761