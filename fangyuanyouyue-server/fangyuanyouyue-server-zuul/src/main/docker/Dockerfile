FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD fangyuanyouyue-server-zuul-1.0-SNAPSHOT.jar fangyuanyouyue-server-zuul.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/fangyuanyouyue-server-zuul.jar"]
EXPOSE 8761