FROM registry.cn-hangzhou.aliyuncs.com/supermap-gaf/boot-base:2.0
MAINTAINER user "user@supermap.com"
# 需要配置数据库
ENV GAF_ENV_DATASOURCE_URL 'jdbc:postgresql://localhost:5432/postgres'
ENV GAF_ENV_DATASOURCE_DRIVER 'org.postgresql.Driver'
ENV GAF_ENV_DATASOURCE_PASSWORD ''
ENV GAF_ENV_DATASOURCE_USERNAME 'postgres'
# 需要配置redis服务器
ENV GAF_ENV_REDIS_HOST 'localhost'
ENV GAF_ENV_REDIS_PORT 6379
ENV GAF_ENV_REDIS_PASSWORD ''
# 需要配置zipkin服务器
ENV GAF_ENV_ZIPKIN_URL 'http//localhost:9411'
# 需要配置fluentd服务器
ENV GAF_ENV_FLUENTD_PORT 24224
ENV GAF_ENV_FLUENTD_HOST 'localhost'
VOLUME /tmp
# 需要超图许可文件
VOLUME /usr/local/supermap/Bin/supermap_any_2020.lic9d
# 需要minio挂载
VOLUME /data-s3fs

ADD target/*.jar /app.jar
COPY src/main/k8s/docker-entrypoint.sh /
COPY src/main/k8s/default.conf /etc/nginx/conf.d
COPY dist_gaf-webapp /usr/share/nginx/html/dist_gaf-webapp
COPY dist_gaf-mapapp /usr/share/nginx/html/dist_gaf-mapapp

RUN chmod -R 755 /usr/share/nginx/html
RUN chmod +x /docker-entrypoint.sh
EXPOSE 8080
ENTRYPOINT ["/docker-entrypoint.sh"]
