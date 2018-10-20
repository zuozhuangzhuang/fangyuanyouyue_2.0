FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD config-server-0.0.1-SNAPSHOT.jar config-server.jar
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
#RUN bash -c 'touch /config-server.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/config-server.jar"]
EXPOSE 8761