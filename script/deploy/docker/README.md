# 单机容器环境的源码构建部署步骤

### Step 1:构建部署前准备

- Linux系统-系统要求CentOS7.5
- Docker[[帮助]](#docker)-容器运行环境
- docker-compose[[帮助]](#docker-compose)-容器启动命令
- psql[[帮助]](#psql)-数据库的脚本运行命令
- Git[[帮助]](#git)-用与克隆代码
- Java[[帮助]](#java)-用于Maven构建
- Maven[[帮助]](#maven)-后端应用构建工具
- node.js环境、yarn命令[[帮助]](#node-yarn)-用于构建打包前端项目

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
### psql
- 安装:
```
yum install https://download.postgresql.org/pub/repos/yum/reporpms/EL-7-x86_64/pgdg-redhat-repo-latest.noarch.rpm
yum install postgresql10
```
- 验证:`psql --version`
### git
- 安装:`yum install git`
- 验证:`git --version`
### java
- 安装:
- 客户端下载jdk1.8,地址：`http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html`
- 下载jdk-8u291-linux-x64.tar.gz，并上传到服务器
- 服务器上执行
```
mkdir /usr/local/java/
tar -zxvf jdk-8u291-linux-x64.tar.gz -C /usr/local/java/
```
- 设置环境变量,在/etc/profile文件最后，加上
```
export JAVA_HOME=/usr/local/java/jdk1.8.0_291
export JRE_HOME=${JAVA_HOME}/jre
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib
export PATH=${JAVA_HOME}/bin:$PATH
```
- 执行命令
```
source /etc/profile
ln -s /usr/local/java/jdk1.8.0_291/bin/java /usr/bin/java
```
- 验证:`java -version`
### maven
- 安装:
- 客户端下载Maven3.5.4,地址:`https://archive.apache.org/dist/maven/maven-3/3.5.4/binaries/`
- 下载apache-maven-3.5.4-bin.zip
- 解压后并上传到服务器
- 服务器上执行
```
mkdir /usr/local/maven
cp -r apache-maven-3.5.4 /usr/local/maven
chmod +x /usr/local/maven/apache-maven-3.5.4/bin/mvn
```
- 设置环境变量,在/etc/profile文件最后，加上
```
export MAVEN_HOME=/usr/local/maven/apache-maven-3.5.4
export PATH=$PATH:$MAVEN_HOME/bin
```
- 执行命令
```
source /etc/profile
```
- 验证:`mvn -v`

### node-yarn
- 安装:
```
curl --silent --location https://rpm.nodesource.com/setup_10.x | sudo bash -
sudo yum install nodejs
curl --silent --location https://dl.yarnpkg.com/rpm/yarn.repo | sudo tee /etc/yum.repos.d/yarn.repo
sudo rpm --import https://dl.yarnpkg.com/rpm/pubkey.gpg
sudo yum install yarn
```
- 验证:`yarn --version`
## 部署帮助
### 删除所有GAF应用及挂载卷
`./script/deploy/docker/deploy.sh delete-all`
### 获取命令帮助
`./script/deploy/docker/deploy.sh help`