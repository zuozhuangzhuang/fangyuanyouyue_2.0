FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD fangyuanyouyue-microservice-order-1.0-SNAPSHOT.jar fangyuanyouyue-microservice-order.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/fangyuanyouyue-microservice-order.jar"]
EXPOSE 8761