FROM alpine:3.4
VOLUME /tmp
# Install root filesystem
ADD fangyuanyouyue-microservice-forum-1.0-SNAPSHOT.jar fangyuanyouyue-microservice-forum.jar

# Install base packages
RUN apk update && apk add curl bash tree tzdata \
    && cp -r -f /usr/share/zoneinfo/Hongkong /etc/localtime \
    && echo -ne "Alpine Linux 3.4 image. (`uname -rsv`)\n" >> /root/.built
# Define bash as default command
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/fangyuanyouyue-microservice-forum.jar"]