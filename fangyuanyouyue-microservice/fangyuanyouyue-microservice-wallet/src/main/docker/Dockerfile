FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD fangyuanyouyue-microservice-wallet-1.0-SNAPSHOT.jar fangyuanyouyue-microservice-wallet.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/fangyuanyouyue-microservice-wallet.jar"]
#EXPOSE 8761