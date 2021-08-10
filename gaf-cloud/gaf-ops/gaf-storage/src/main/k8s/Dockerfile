FROM registry.cn-hangzhou.aliyuncs.com/supermap-gaf/backend-base:debian
MAINTAINER gafci "gaf@supermap.com"
# Java启动参数
ENV JAVA_OPTS ''
# 需要配置数据库
ENV server_port 8080
ENV GAF_STORAGE_TENANTID_HEADER TENANT_ID
ENV GAF_STORAGE_PERMISSION_HEADER PERMISSION
ENV GAF_STORAGE_PERMISSION_ENABLE false
ENV GAF_STORAGE_SUPER_OWER admin
#ENV GAF_STORAGE_DB_URL ${default_db}
ENV GAF_STORAGE_DB_URL jdbc:postgresql://localhost:5432/gaf-storage
ENV GAF_STORAGE_DB_USERNAME postgres
ENV GAF_STORAGE_DB_PASSWORD root
ENV GAF_STORAGE_DATA_PATH storage.db
ENV GAF_STORAGE_EUREKA_ENABLE false
ENV GAF_STORAGE_EUREKA_ADDR http://127.0.0.1:8761/eureka/
ENV GAF_STORAGE_AUTHENTICATION_ENABLE true
VOLUME /tmp
ADD target/*.jar app.jar
COPY src/main/k8s/docker-entrypoint.sh /
RUN chmod +x /docker-entrypoint.sh
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=5s CMD curl -fs http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["/docker-entrypoint.sh"]