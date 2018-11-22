FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD fangyuanyouyue-microservice-goods-1.0-SNAPSHOT.jar fangyuanyouyue-microservice-goods.jar
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Xmx256m","-jar","/fangyuanyouyue-microservice-goods.jar"]
#EXPOSE 8761