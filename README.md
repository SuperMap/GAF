# SuperMap GAF（SuperMap GIS Application Framework，超图GIS应用框架）
# 项目介绍
- 采用前后端分离的模式

## 架构图

## 工程结构
```
SuperMap GAF
├── gaf-bom
├── gaf-boot
├── gaf-cloud
|   ├── gaf-biz
|   |   ├── gaf-gis
|   |       ├── gaf-data-mgt
|   |       ├── gaf-idesktopx-plugin
|   |       ├── gaf-map
|   ├── gaf-commons
|   |   ├── gaf-common-auth
|   |   ├── gaf-common-auth-starter
|   |   ├── gaf-common-boot
|   |   ├── gaf-common-cache
|   |   ├── gaf-common-cloud
|   |   ├── gaf-common-license-starter
|   |   ├── gaf-common-base-data
|   |   ├── gaf-common-log
|   |   ├── gaf-common-rest
|   |   ├── gaf-common-storage
|   |   ├── gaf-common-swagger
|   |   ├── gaf-common-data-access
|   |   ├── gaf-common-utils
|   ├── gaf-ops
|       ├── gaf-auth
|       |   ├── gaf-authentication
|       |   ├── gaf-authority
|       ├── gaf-microservice
|       |   ├── gaf-microservice-api  -- SwaggerApi
|       |   ├── gaf-microservice-conf -- 配置中心
|       |   ├── gaf-microservice-gateway  -- Spring-Cloud网关
|       |   ├── gaf-microservice-governance  --
|       |   ├── gaf-microservice-rigister  -- 注册中心
|       ├── gaf-monitor
|       ├── gaf-storage
|       ├── gaf-portal  -- 门户模块
├── gaf-web
|   ├── common-gaf
|   ├── common-mapapp
|   ├── common-webapp
|   ├── gaf-mapapp
|   ├── gaf-webapp
```
## 官网
## 在线演示
## 项目地址
- GitHub地址:https://github.com/SuperMap/GAF

# 开源协议
Apache Licence 2.0 （英文原文） Apache Licence是著名的非盈利开源组织Apache采用的协议。该协议和BSD类似，同样鼓励代码共享和尊重原作者的著作权，同样允许代码修改，再发布（作为开源或商业软件）。 需要满足的条件如下：

- 需要给代码的用户一份Apache Licence
- 如果你修改了代码，需要在被修改的文件中说明。
- 在延伸的代码中（修改和有源代码衍生的代码中）需要带有原来代码中的协议，商标，专利声明和其他原来作者规定需要包含的说明。
- 如果再发布的产品中包含一个Notice文件，则在Notice文件中需要带有Apache Licence。你可以在Notice中增加自己的许可，但不可以表- 现为对Apache Licence构成更改。 Apache Licence也是对商业应用友好的许可。使用者也可以在需要的时候修改代码来满足需要并作为开源或商业产品发布/销售。

## 用户权益

- 对未经过授权和不遵循 Apache 2.0 协议二次开源或者商业化我们将追究到底。

# 界面
# 技术文档
## 《开发者指南-后端》 
### 1. 后端技术

* 基础框架：Java8 & Spring Boot & Spring Cloud & Maven

* 数据库：Postgresql & Mysql等

* 鉴权框架：Spring Security Shiro

* 服务注册及配置中心：Spring Cloud Euraka & Spring Cloud Config

* 缓存框架；Redis
* ...

### 2. 代码拉取
### 3. 编译打包
```
mvn clean
mvn install -Dmaven.test.skip=true
```
### 4. 运行组件
* 启动顺序为 注册中心->配置中心->其他
* 配置中心配置访问格式：
* 环境变量参数参考[Docker File](./gaf-cloud/gaf-biz/gaf-gis/gaf-map/gaf-map-app/src/main/k8s/Dockerfile)
```
${spring.cloud.config.url}/${spring.application,name}-${spring.cloud.config.profile}.yml
例：
http://192.168.11.118:30131/gaf-map-prod.yml
```
#### 4.1 Jar包运行
* 进入每个组件target目录，执行命令
```
java -jar xxx.jar  --xxxx=xx
```
例:
```
java -jar gaf-map-app-2.1.0-SNAPSHOT.jar  --spring.profiles.active=prod
```
#### 4.2 Maven运行
* 跳转组件根目录(pom.xml同级)，设置环境变量运行

```
mvn spring-boot:run -Dspring-boot.run.profiles=xxx
```

### 5. 制作docker image镜像
* 将统一服务下target中Jar包和k8s中DockerFile 放到同一目录下,执行docker build命令

```
$ docker build -t xxxx(自定义镜像名称)
```

### 6. docker容器运行
* 运行镜像

```
$ docker run -p 8080:8080 -d xxx(制作镜像定义的镜像名)
```

* 环境变量参数参考组件下[Docker File](./gaf-cloud/gaf-biz/gaf-gis/gaf-map/gaf-map-app/src/main/k8s/Dockerfile)

### 7. 代码更新、提交
* 拉取
```
git pull <仓库名> <主分支名> --rebase
```
* 本地新建一个local分支
```
git checkout -b local
```
* 在local分支上进行你所需要的代码开发并本地提交
* 本地提交完成后，<主分支>拉取最新的代码
```
git pull <仓库名> <远程主分支名>:<本地主分支名>
```
* 将local分支本地提交与<主分支>进行合并
```
git rebase <主分支名>
```
注：此时可能需要解决冲突，解决完成后，使用git add .``git rebase --continue
* 切换到主分支
```
git checkout <主分支名>
```
* 主分支与local分支合并
```
git merge local
```
* 推送到远程仓库
```
git push <仓库名> <主分支名>
```
## 《开发者指南-前端》 
### 1. 前端技术
* Vue, Vuex, Vue-Router
* Nuxt.js
* Ant Design
* SuperMap iClient
* SuperMap iClient3D for WebGL
* ...
### 2. 运行启动
```
2.1 进入gaf-webapp
$ cd gaf-webapp

2.2 安装
$ yarn install

2.3 运行
$ yarn dev

2.4 打包
$ yarn build
$ yarn start

```
# 关于我们



