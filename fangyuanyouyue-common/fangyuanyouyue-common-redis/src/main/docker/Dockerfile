FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD fangyuanyouyue-common-redis-1.0-SNAPSHOT.jar fangyuanyouyue-common-redis.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/fangyuanyouyue-common-redis.jar"]
EXPOSE 6379