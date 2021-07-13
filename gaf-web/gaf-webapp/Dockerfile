FROM registry.cn-hangzhou.aliyuncs.com/supermap-gaf/nginx:1.17.10-alpine

MAINTAINER gafci "gaf@supermap.com"

COPY dist /usr/share/nginx/html

COPY default.conf /etc/nginx/conf.d

RUN chmod -R 755 /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
