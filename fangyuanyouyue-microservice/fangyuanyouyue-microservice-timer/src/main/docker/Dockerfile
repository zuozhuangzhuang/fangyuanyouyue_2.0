FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD fangyuanyouyue-microservice-timer-1.0-SNAPSHOT.jar fangyuanyouyue-microservice-timer.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/fangyuanyouyue-microservice-timer.jar"]
#EXPOSE 8761