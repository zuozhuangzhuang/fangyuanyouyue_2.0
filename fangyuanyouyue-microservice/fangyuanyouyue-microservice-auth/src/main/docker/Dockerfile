FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD fangyuanyouyue-microservice-auth-1.0-SNAPSHOT.jar fangyuanyouyue-microservice-auth.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/fangyuanyouyue-microservice-auth.jar"]
#EXPOSE 8761