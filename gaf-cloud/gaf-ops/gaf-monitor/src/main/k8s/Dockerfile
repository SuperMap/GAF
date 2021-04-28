FROM harbor.gaf.net.cn/library/backend-base:debian
MAINTAINER user "user@supermap.com"
VOLUME /tmp
ADD target/*.jar app.jar
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=5s CMD curl -fs http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.profiles.active=prod"]