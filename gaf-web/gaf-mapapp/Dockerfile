FROM harbor.gaf.net.cn/library/nginx:1.17.10-alpine

MAINTAINER gafci "gaf@supermap.com"

COPY dist /usr/share/nginx/html

COPY default.conf /etc/nginx/conf.d

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
