FROM registry.cn-hangzhou.aliyuncs.com/supermap-gaf/backend-base:debian
MAINTAINER user "user@supermap.com"
# Java启动参数
ENV JAVA_OPTS ''
VOLUME /tmp
ADD target/*.jar app.jar
COPY src/main/k8s/docker-entrypoint.sh /
RUN chmod +x /docker-entrypoint.sh
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=5s CMD curl -fs http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["/docker-entrypoint.sh"]