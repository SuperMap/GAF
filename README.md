# SuperMap GAF（SuperMap GIS Application Framework  超图GIS应用框架）


---

## 项目介绍

SuperMap GAF（SuperMap GIS Application Framework，超图GIS应用框架）基于SuperMap GIS基础软件进行研发，是连接GIS基础软件与行业应用的重要纽带。采用微服务、云原生、持续集成等先进成熟的IT技术，实现从GIS基础软件到行业应用的快速开发和自动化维护，将极大的缩短行业应用开发时间，持续提升并保持行业应用技术先进性。SuperMap GAF 通过开源协同的研发模式建立统一开发规范和开发框架，集成各应用单位及个人的经验成果，提高应用软件研发和项目实施效率。

GAF帮助GIS应用开发商构建大中型（省市级）GIS应用平台，支撑大规模空间数据和服务管理，支持大数据、分布式空间分析等，提供丰富的二三维地图场景展示、应用和扩展开发的GIS应用框架。标准化，降低GIS技术门槛，减轻开发和维护的工作量，提高GIS应用开发效率，提升GIS应用能力。

## 在线演示

- 演示地址:https://gaf.net.cn


## 快速开始



### Step 1:构建部署前准备

- Linux系统-系统要求CentOS7.5
- Docker[[帮助]](script/deploy/docker/README.md#docker)-容器运行环境
- docker-compose[[帮助]](script/deploy/docker/README.md#docker-compose)-容器启动命令
- psql[[帮助]](script/deploy/docker/README.md#psql)-数据库的脚本运行命令
- Git[[帮助]](script/deploy/docker/README.md#git)-用与克隆代码
- Java[[帮助]](script/deploy/docker/README.md#java)-用于Maven构建
- Maven[[帮助]](script/deploy/docker/README.md#maven)-后端应用构建工具
- node.js环境、yarn命令[[帮助]](script/deploy/docker/README.md#node-yarn)-用于构建打包前端项目

### Step 2:克隆代码

`git clone https://github.com/SuperMap/GAF`

### Step 3.源码构建部署
#### 3.1.编辑配置文件

- 对配置文件进行查看修改，文件内部有参数修改说明
- 配置文件位置`script/deploy/docker/.env`


#### 3.2.一键部署GAF
- 项目根目录执行：
`./script/deploy/docker/deploy.sh all`

### Step 4.进入GAF
- 使用`docker ps`查看各个容器服务的状态,status都为Health时表明各服务都已成功运行部署
- 当全部容器health时，就可以在客户端通过浏览器访问部署完成后提示的GAF地址


## 用户权益

- 对未经过授权和不遵循 Apache 2.0 协议二次开源或者商业化我们将追究到底。

## 技术文档

https://github.com/SuperMap/GAF/wiki/


[《开发者指南-后端》](https://github.com/SuperMap/GAF/wiki/%E3%80%8A%E5%BC%80%E5%8F%91%E8%80%85%E6%8C%87%E5%8D%97-%E5%90%8E%E7%AB%AF%E3%80%8B)



## 关于我们



