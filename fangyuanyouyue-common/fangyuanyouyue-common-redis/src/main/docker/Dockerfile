FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD fangyuanyouyue-common-redis-1.0-SNAPSHOT.jar fangyuanyouyue-common-redis.jar
RUN apk add -U tzdata
RUN cp /usr/share/zoneinfo/Asia/Shanghai  /etc/localtime
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/fangyuanyouyue-common-redis.jar"]
#EXPOSE 6379