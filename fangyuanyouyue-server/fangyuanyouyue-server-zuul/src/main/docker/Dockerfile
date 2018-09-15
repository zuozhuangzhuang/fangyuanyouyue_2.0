FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD fangyuanyouyue-server-zuul-1.0-SNAPSHOT.jar fangyuanyouyue-server-zuul.jar
RUN apk add -U tzdata
RUN cp /usr/share/zoneinfo/Asia/Shanghai  /etc/localtime
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/fangyuanyouyue-server-zuul.jar"]
EXPOSE 8769