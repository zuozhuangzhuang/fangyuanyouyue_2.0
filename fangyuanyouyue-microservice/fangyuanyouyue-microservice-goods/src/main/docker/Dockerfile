FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD fangyuanyouyue-microservice-goods-1.0-SNAPSHOT.jar fangyuanyouyue-microservice-goods.jar
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai/etc/localtime
RUN cp /usr/share/zoneinfo/Asia/Shanghai  /etc/localtime
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/fangyuanyouyue-microservice-goods.jar"]
#EXPOSE 8761