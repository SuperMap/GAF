# 单机容器环境的源码构建部署步骤

### Step 1:构建部署前准备

- Linux系统-系统要求CentOS7.5
- Docker[[帮助]](#docker)-容器运行环境
- docker-compose[[帮助]](#docker-compose)-容器启动命令
- Git[[帮助]](#git)-用与克隆代码


### Step 2:克隆代码

`git clone https://github.com/SuperMap/GAF`

### Step 3.源码构建部署
#### 3.1.进入脚本文件目录

`cd GAF/script/deploy/docker`

#### 3.2.编辑配置文件

- 配置文件名称：`.env`
- 对配置文件进行查看修改，文件内部有参数修改说明


#### 3.3.构建GAF应用镜像

`chmod +x build.sh && ./build.sh`

#### 3.4.部署GAF基础应用

`chmod +x deploy.sh && ./deploy.sh base`

- GAF监控相关应用部署（可选）[[帮助]](#GAF-MONITOR) 

### Step 4.进入GAF
- 使用`docker ps`查看各个容器服务的状态,status都为Health时表明各服务都已成功运行部署
- 当全部容器health时，就可以在客户端通过浏览器访问部署完成后提示的GAF地址

## 其它方式构建部署

### 1.通过线上镜像部署GAF基础应用(安装包方式部署)
- 1.配置文件修改：对配置文件`.env` 进行修改，配置`GAF_REGISTRY`镜像仓库地址，配置`GAF_REGISTRY_TAG`GAF镜像TAG
- 2.部署GAF基础应用，执行`./deploy.sh base`

### 2.源码构建镜像并推送镜像到镜像仓库--->从线上镜像仓库拉取镜像部署GAF基础应用
- 1.配置文件修改：对配置文件`.env` 进行修改，配置`GAF_REGISTRY`镜像仓库地址，配置`GAF_REGISTRY_TAG`GAF镜像TAG
- 2.Maven的配置文件`settings.xml`文件加上
```xml
<server>
  <id><镜像库地址></id>
  <username><镜像库账号></username>
  <password><镜像库密码></password>
  <configuration>
    <email><邮件地址></email>
  </configuration>
</server>
```
- 3.构建推送镜像到镜像仓库，执行`./build.sh push`
- 4.部署GAF基础应用，执行`./deploy.sh base`

## 环境帮助
### docker
- 安装: 
```
curl -fsSL https://get.docker.com | bash -s docker --mirror Aliyun
systemctl enable docker
systemctl start docker
```
- 验证:`docker ps`
### docker-compose
- 安装:
```
sudo curl -L "https://github.com/docker/compose/releases/download/1.29.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
```
- 验证:`docker-compose --version`
### git
- 安装:`yum install git`
- 验证:`git --version`

## 部署帮助
### 删除所有GAF应用及挂载卷
`./script/deploy/docker/deploy.sh delete-all`
### 获取命令帮助
`./script/deploy/docker/deploy.sh help`
### GAF-MONITOR
- GAF监控相关应用部署
```
./script/deploy/docker/deploy.sh monitor
```