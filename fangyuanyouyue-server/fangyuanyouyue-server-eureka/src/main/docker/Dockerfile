FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD fangyuanyouyue-server-eureka-1.0-SNAPSHOT.jar fangyuanyouyue-server-eureka.jar
#RUN bash -c 'touch /fangyuanyouyue-server-eureka-1.0-SNAPSHOT.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/fangyuanyouyue-server-eureka.jar"]
EXPOSE 8761