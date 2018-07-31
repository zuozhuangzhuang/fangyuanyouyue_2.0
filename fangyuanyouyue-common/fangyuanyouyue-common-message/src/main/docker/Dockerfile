FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD fangyuanyouyue-common-message-1.0-SNAPSHOT.jar fangyuanyouyue-common-message.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/fangyuanyouyue-common-message.jar"]
#EXPOSE 6379