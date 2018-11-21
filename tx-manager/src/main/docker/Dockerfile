FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD tx-manager-4.2.0-SNAPSHOT.jar tx-manager-4.2.0-SNAPSHOT.jar
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
#RUN bash -c 'touch /tx-manager-4.2.0-SNAPSHOT.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Xmx256m","-jar","/tx-manager-4.2.0-SNAPSHOT.jar"]
EXPOSE 8899
EXPOSE 9999