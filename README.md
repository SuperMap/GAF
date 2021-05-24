# SuperMap GAF
SuperMap GIS Application Framework 超图GIS应用框架


---

## 介绍

SuperMap GAF（SuperMap GIS Application Framework，超图GIS应用框架）基于SuperMap GIS基础软件进行研发，是连接GIS基础软件与行业应用的重要纽带。帮助GIS应用开发商构建大中型GIS应用平台，支撑大规模空间数据和服务管理，支持大数据、分布式空间分析等，提供丰富的二三维地图场景展示、应用和扩展开发的GIS应用框架。标准化，降低GIS技术门槛，减轻开发和维护的工作量，提高GIS应用开发效率，提升GIS应用能力。

GAF采用微服务、云原生、持续集成等先进成熟的IT技术，实现从GIS基础软件到行业应用的快速开发，极大地缩短行业应用开发时间，持续提升行业应用技术先进性。通过开源协同的研发模式建立统一开发规范和开发框架，集成各应用单位及个人的经验成果，提高应用程序研发和项目实施效率。

## 特性

**GIS应用开发**

GAF平台提供了基础的地图应用工程，方便实现GIS应用开发者在此基础上快速扩展编码和部署自己的大型应用，并可很方便把地应用功能整合到系统菜单中。平台使用SuperMap GIS桌面产品iDesktopX和它的GAF扩展插件进行数据管理，并将数据发布成iServer上的数据服务和地图服务，并在应用中使用，开发者可以对GAF提供的基础地图应用中底图、资源目录、工具条等做配置修改，快速产出满足自己的用户需求的GIS应用。

**微服务治理**

为用户提供应用托管和微服务管理能力的PaaS平台，帮助用户简化部署、监控、运维等应用生命周期管理工作，同时提供服务部署、服务配置、路由管理、服务监控和服务链路追踪等微服务管理和运维能力。

**运维监控**

对平台硬件资源占用、服务状态及资源占用进行实时监控，运维人员可通过对资源设置预警阈值，并即时进行扩容，以防止因资源溢出导致的资源的丢失。

**权限控制**

提供组件统一授权管理，通过多租户的方式对不同应用进行集中管理。基于RBAC（Role-Based Access Control）权限管理模型，进行基于角色的权限访问控制。

**统一身份认证**

构建集中用户认证中心，提供统一访问控制与安全管理，提供多种安全认证方式和功能，满足应用安全访问需求。


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

`git clone https://gitee.com/supermapgaf/GAF`

### Step 3:源码构建部署

#### 3.1.进入脚本文件目录

`cd GAF/script/deploy/docker`

#### 3.2.编辑配置文件

- 配置文件名称：`.env`
- 对配置文件进行查看修改，文件内部有参数修改说明


#### 3.3.构建GAF应用镜像

`./build.sh`

#### 3.4.部署GAF基础应用

`./deploy.sh base`

- GAF监控相关应用部署（可选）[[帮助]](script/deploy/docker/README.md#GAF-MONITOR) 

### Step 4:进入GAF
- 使用`docker ps`查看各个容器服务的状态,status都为Health时表明各服务都已成功运行部署
- 当全部容器health时，就可以在客户端通过浏览器访问部署完成后提示的GAF地址


## 在线演示

- 演示地址:
- 

## 技术文档

开发者指南(https://gitee.com/supermapgaf/GAF/wikis)

## 开源协议
Apache Licence 2.0。

## 用户权益

- 对未经过授权和不遵循 Apache 2.0 协议二次开源或者商业化我们将追究到底。






