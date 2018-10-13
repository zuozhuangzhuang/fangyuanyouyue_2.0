FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD tx-manager-4.2.0-SNAPSHOT.jar tx-manager-4.2.0-SNAPSHOT.jar
RUN apk add -U tzdata
RUN cp /usr/share/zoneinfo/Asia/Shanghai  /etc/localtime
#RUN bash -c 'touch /tx-manager-4.2.0-SNAPSHOT.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/tx-manager-4.2.0-SNAPSHOT.jar"]
EXPOSE 8899
EXPOSE 9999