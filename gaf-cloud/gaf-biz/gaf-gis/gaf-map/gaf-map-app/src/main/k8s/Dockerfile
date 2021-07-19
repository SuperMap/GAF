FROM registry.cn-hangzhou.aliyuncs.com/supermap-gaf/ugo-base:2.0
MAINTAINER user "gaf@supermap.com"
# Java启动参数
ENV JAVA_OPTS ''
# 需要配置数据库
ENV GAF_ENV_DATASOURCE_URL 'jdbc:postgresql://localhost:5432/postgres'
ENV GAF_ENV_DATASOURCE_DRIVER 'org.postgresql.Driver'
ENV GAF_ENV_DATASOURCE_PASSWORD ''
ENV GAF_ENV_DATASOURCE_USERNAME 'postgres'
# 需要配置fluentd服务器
ENV GAF_ENV_FLUENTD_PORT 24224
ENV GAF_ENV_FLUENTD_HOST 'localhost'

VOLUME /tmp
# 需要minio挂载
VOLUME /data-s3fs
ADD target/*.jar app.jar
COPY src/main/k8s/docker-entrypoint.sh /
RUN chmod +x /docker-entrypoint.sh
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=5s CMD curl -fs http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["/docker-entrypoint.sh"]
#CMD /opt/init.sh && java -jar app.jar --spring.profiles.active=prod