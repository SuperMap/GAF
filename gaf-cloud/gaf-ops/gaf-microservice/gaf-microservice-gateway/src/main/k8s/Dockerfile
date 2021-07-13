FROM registry.cn-hangzhou.aliyuncs.com/supermap-gaf/backend-base:debian
MAINTAINER gafci "gaf@supermap.com"
# Java启动参数
ENV JAVA_OPTS ''
# 需要配置数据库
ENV GAF_ENV_DATASOURCE_URL 'jdbc:postgresql://localhost:5432/postgres'
ENV GAF_ENV_DATASOURCE_DRIVER 'org.postgresql.Driver'
ENV GAF_ENV_DATASOURCE_PASSWORD ''
ENV GAF_ENV_DATASOURCE_USERNAME 'postgres'
# 需要配置zipkin服务器
ENV GAF_ENV_ZIPKIN_URL 'http//localhost:9411'
# 需要配置fluentd服务器
ENV GAF_ENV_FLUENTD_PORT 24224
ENV GAF_ENV_FLUENTD_HOST 'localhost'
VOLUME /tmp
ADD target/*.jar app.jar
COPY src/main/k8s/docker-entrypoint.sh /
RUN chmod +x /docker-entrypoint.sh
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=5s CMD curl -fs http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["/docker-entrypoint.sh"]