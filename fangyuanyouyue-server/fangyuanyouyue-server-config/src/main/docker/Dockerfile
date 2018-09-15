FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD config-server-0.0.1-SNAPSHOT.jar config-server.jar
RUN apk add -U tzdata
RUN cp /usr/share/zoneinfo/Asia/Shanghai  /etc/localtime
#RUN bash -c 'touch /config-server.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/config-server.jar"]
EXPOSE 8761