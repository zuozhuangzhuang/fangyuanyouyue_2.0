FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD fangyuanyouyue-microservice-user-1.0-SNAPSHOT.jar fangyuanyouyue-microservice-user.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/fangyuanyouyue-microservice-user.jar"]
#EXPOSE 8761