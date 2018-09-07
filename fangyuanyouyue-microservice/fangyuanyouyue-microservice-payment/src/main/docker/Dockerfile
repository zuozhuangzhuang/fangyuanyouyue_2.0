FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD fangyuanyouyue-microservice-payment-1.0-SNAPSHOT.jar fangyuanyouyue-microservice-payment.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/fangyuanyouyue-microservice-payment.jar"]
#EXPOSE 8761