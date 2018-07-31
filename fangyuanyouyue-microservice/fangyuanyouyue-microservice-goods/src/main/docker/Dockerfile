FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD fangyuanyouyue-microservice-goods-1.0-SNAPSHOT.jar fangyuanyouyue-microservice-goods.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/fangyuanyouyue-microservice-goods.jar"]
#EXPOSE 8761