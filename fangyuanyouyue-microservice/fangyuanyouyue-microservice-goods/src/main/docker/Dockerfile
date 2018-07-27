FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD goods-service-0.0.1-SNAPSHOT.jar goods-service.jar
#RUN bash -c 'touch /goods-service.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/goods-service.jar"]
EXPOSE 8761