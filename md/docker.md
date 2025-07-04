# 关于 docker 安装以及部署项目
## 1. CentOS 中安装 Docker
 [Docker安装教程](https://blog.csdn.net/github_36665118/article/details/129462784)

## 2. VMware中镜像拉取失败

```bash
vim resolv.conf 
nameserver 8.8.8.8
systemctl daemon-reload
systemctl restart docker

```
```bash
 # 保证文件被修改后重启虚拟机不会被自动修改回去
 chattr +i /etc/resolv.conf
```


## 3. idea 远程连接Docker

```bash
vim /usr/lib/systemd/system/docker.service
# 在ExecStart=/usr/bin/dockerd追加
-H tcp://0.0.0.0:2375 -H unix://var/run/docker.sock
systemctl stop firewalld.service //关闭防火墙（切记切记，端口无法访问要检查防火墙）

```

[idea远程连接Docker](https://blog.csdn.net/weixin_53742691/article/details/129977018)

docker desktop windows 远程连接

## 4. dockerfile 部署 springboot 项目

```dockerfile
FROM eclipse-temurin:17
LABEL maintainer="xh-test"
RUN mkdir -p /root/test # 将jar文件复制到工作目录
ADD ./target/springmp-0.1.7.jar /root/test/springmp-0.1.7.jar
WORKDIR /root/test
EXPOSE 8088
ENTRYPOINT ["java","-jar","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005","springmp-0.1.7.jar"]
```



## 5. 创建JDK运行环境配置

```dockerfile
FROM centos:latest
COPY ./zsb-1.0.jar /app.jar
RUN mkdir /usr/local/docker
RUN cd /usr/local/docker
ADD jdk-17_linux-x64_bin.tar.gz /usr/local/docker
ENV JAVA_HOME=/usr/local/docker/jdk-17.0.10
ENV PATH=$PATH:$JAVA_HOME/bin
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

```dockerfile
# 使用maven作为基础镜像
FROM maven:3.8.4-openjdk-17 AS build
# 设置基础镜像
FROM openjdk:17
### 指定存储在容器内的目录
WORKDIR  /usr/local/anytext
#ADD any-text-0.0.1-SNAPSHOT.jar app/keyword-server.jar
# 创建log4日志输出位置
#RUN mkdir  /log4
#RUN mkdir  /log4/sysLog
#RUN mkdir  /log4/errorLog
# 拷贝jar包到容器 取名为app.jar。  注意：copy 宿主机的目录只能是相对路径，不能使用绝对路径
COPY  ./keyword-server.jar  /usr/local/anytext/keyword-server.jar
# 拷贝 配置文件
COPY  ./application.yml  /usr/local/anytext/application.yml
EXPOSE 8092
ENTRYPOINT ["java","-jar","keyword-server.jar","--spring.config.location=/usr/local/anytext/application.yml"]
```





**add 和 copy的区别**

相同点：
复制文件或目录： 无论是 COPY 还是 ADD 都可以将文件或目录从构建上下文复制到容器中。
支持源路径和目标路径： 两者都需要指定源路径和目标路径，用于指定要复制的文件或目录在主机上的位置以及在容器中的目标路径。
不同点：
自动解压缩：

COPY：仅用于复制本地文件到镜像，不涉及解压缩操作。
ADD：除了复制文件外，还具有自动解压缩的功能。如果源路径为 URL 地址或压缩文件，ADD 会尝试自动解压缩文件到目标路径。
构建上下文影响：

COPY：只能复制构建上下文中的文件，不能复制 URL 地址或压缩文件。
ADD：可以复制构建上下文中的文件，同时也支持复制远程 URL 地址和解压缩压缩文件。
最佳实践：

通常情况下，推荐使用 COPY 来简单地复制本地文件到容器中，这样可以避免意外的解压缩行为，也更符合直觉。
使用 ADD 时，需要谨慎处理，避免不必要的自动解压缩带来的安全风险。


## 6. docker 查询容器运行日志

```bash
docker log [containername]
```

## 7. docker 容器将镜像中的文件拷贝到宿主机

```bash
docker cp 容器id 容器内目录 宿主机目录
```

## 8. docker 部署mysql
```bash
docker run -d --name mysql-1 \
-p 3306:3306 \
-e TZ=Asia/Shanghai \
-e MYSQL_ROOT_PASSWORD=123 \
mysql \
--character-set-server=utf8mb4 \
--collation-server=utf8mb4_general_ci
```

## 9. 容器中下载 vim

```bash

yum install vim
apt-get install vim
apk add vim
//docker mysql镜像版本7和8中使用
microdnf install -y vim

```

## 10. docker logs 日志文件位置

    Docker 容器的日志默认情况下保存在宿主机的 /var/lib/docker/containers 目录下，每个容器有一个单独的子目录，目录名即为容器的 ID。在容器对应的子目录内，日志文件通常是 containerID-json.log 这样的形式。

# docker 常用命令

## 1. 容器生命周期管理

### 1.1 docker run 

```bash
docker run [OPTIONS] IMAGE [COMMAND] [ARG...]
``` 
**OPTIONS说明：**

    -a stdin: 指定标准输入输出内容类型，可选 STDIN/STDOUT/STDERR 三项；

    -d: 后台运行容器，并返回容器ID；

    -i: 以交互模式运行容器，通常与 -t 同时使用；

    -P: 随机端口映射，容器内部端口随机映射到主机的端口

    -p: 指定端口映射，格式为：主机(宿主)端口:容器端口

    -t: 为容器重新分配一个伪输入终端，通常与 -i 同时使用；

    --name="nginx-lb": 为容器指定一个名称；

    --dns 8     .8.8.8: 指定容器使用的DNS服务器，默认和宿主一致；

    --dns-search example.com: 指定容器DNS搜索域名，默认和宿主一致；

    -h "mars": 指定容器的hostname；

    -e username="ritchie": 设置环境变量；

    --env-file=[]: 从指定文件读入环境变量；

    --cpuset="0-2" or --cpuset="0,1,2": 绑定容器到指定CPU运行；

    -m :设置容器使用内存最大值；

    --net="bridge": 指定容器的网络连接类型，支持 bridge/host/none/container: 四种类型；

    --link=[]: 添加链接到另一个容器；

    --expose=[]: 开放一个端口或一组端口；

    --volume , -v: 绑定一个卷

### 1.2 docker start/stop/restart

```bash
docker start/stop/restart [OPTIONS] CONTAINER [CONTAINER...]
```        
**OPTIONS**

    -a 参数用于将当前shell连接到指定容器的STDIN、STDOUT或STDERR。可以使用docker attach命令来连接到正在运行的容器，并与其进行交互。通过--attach参数，可以实时查看容器的输出或向容器发送输入。
    -i 实际效果和-i参数的作用是保持容器的STDIN（标准输入）打开，即使没有附加也保持打开状态。这通常与-t参数（为容器重新分配一个伪输入终端）一起使用，以便在创建容器后自动进入容器内部。当-i和-t一起使用时，可以与容器进行交互，例如执行命令或查看输出。

### 1.3 docker kill

```bash
docker kill [OPTIONS] CONTAINER [CONTAINER...]
```
**OPTIONS**
    -s, --signal: 指定要发送的信号。默认是 SIGKILL。可以使用其他信号，如 SIGTERM，来优雅地停止容器。
    --timeout: 指定在发送 SIGKILL 信号前等待容器自行停止的时间（以秒为单位）。如果容器在这段时间内没有停止，将发送 SIGKILL 信号。
            
**注意事项**
- 使用 docker kill 命令时，容器中的进程将立即被终止，没有机会执行任何清理操作，如关闭文件描述符或保存状态。因此，在可能的情况下，最好使用 docker stop 命令来停止容器，它会首先发送 SIGTERM 信号，等待一段时间后再发送 SIGKILL 信号。
- 如果容器已经停止或不存在，docker kill 命令将不会执行任何操作，并可能返回一个错误消息。
- 总的来说，docker kill 命令提供了一种强制停止容器的方法，但在使用时需要谨慎，确保不会意外地中断正在执行的重要任务或导致数据丢失。


### 1.4 docker rm

```bash
docker rm [OPTIONS] CONTAINER [CONTAINER...]
```
**OPTIONS**
    -f 或 --force：强制删除一个运行中的容器。使用此选项时，Docker 会发送 SIGKILL 信号来终止容器中的进程，并立即删除容器。
    -l：移除容器间的网络连接，而非容器本身。可以让保留容器但删除其与其他容器或网络的连接。
    -v 或 --volumes：删除与容器关联的卷（数据卷）。这将同时删除容器挂载的所有数据卷。

**注意事项**
- 使用 docker rm 命令删除容器时，该容器中的所有数据都将丢失，除非这些数据被存储在外部卷中。
- 如果尝试删除一个正在运行的容器而不使用 -f 选项，Docker 会返回一个错误，并提示先停止容器。

### 1.5 docker pause/unpause

docker pause 和 docker unpause 是 Docker 中用于控制容器运行状态的命令。这些命令允许暂停和恢复容器的执行，而不会终止容器内的进程。

```bash
docker pause CONTAINER [CONTAINER...]
```
**注意事项**
- 当容器被暂停时，它仍然会占用系统资源，比如内存和磁盘空间。
- 暂停和恢复操作是很快的，通常不会导致数据丢失或损坏。
- 使用这些命令时要小心，确保不会意外地暂停或恢复重要的容器。

### 1.6 docker create

Docker create命令用于基于指定的镜像创建一个新的容器，但它并不会启动这个容器。与docker run -d命令相似，docker create创建的容器需要在之后通过docker start命令或docker run命令来启动。此外，docker create命令常用于在启动容器之前进行必要的设置。

```bash
docker create [OPTIONS] IMAGE [COMMAND] [ARG...]
```
**OPTIONS**
    -d, --detach=true|false：在后台运行容器，并返回容器ID。默认情况下，容器会在前台运行。

    --name=NAME：为容器指定一个名称。如果不指定名称，Docker 会自动为容器生成一个随机的名称。

    -e, --env=[] 或 --environment=[]：设置环境变量。可以使用多次来设置多个环境变量。

    -p, --publish=[]：发布容器的端口到主机上。格式为 hostPort:containerPort。

    -P, --publish-all=true|false：将容器内所有暴露的端口都发布到主机上。

    --network=NETWORK：指定容器要连接的网络。

    --volumes-from=CONTAINER：挂载来自另一个容器的卷。

    --restart=POLICY：容器的重启策略。可以是 no（默认，不重启）、on-failure[:max-retries]（仅在容器退出状态非0时重启）、always（总是重启）、unless-stopped（除非被停止，否则总是重启）。

    --user="" 或 -u ""：设置容器运行时的用户名或UID和组名或GID。

    --workdir="" 或 -w ""：设置容器内的工作目录。

    --entrypoint=""：覆盖镜像的默认入口点命令。

    --cap-add=[] 和 --cap-drop=[]：添加或删除容器的 Linux 功能。

    --privileged=true|false：给予容器扩展权限。

    --security-opt=[]：设置容器的安全选项。

    --storage-opt=[]：设置容器的存储驱动选项。

    --device=[]：添加主机设备到容器中。

    --memory=""：设置容器使用的内存大小限制。

    --memory-swap=""：设置容器使用的总内存大小，包括交换分区。

    --cpu-shares=0：设置 CPU 使用权重。

    --cpuset-cpus=""：限制容器可以使用的 CPU 核心。·
    --cpuset-mems=""：限制容器可以使用的内存节点。

    --label=[]：为容器设置元数据。

### 1.7 docker exec

docker exec 是一个用于在正在运行的 Docker 容器中执行命令的命令。通过 docker exec，可以在容器的上下文中运行命令，而无需重新启动容器。

**常见用法**
1. 运行交互式 shell：
使用 -it 参数启动一个交互式 shell，如 /bin/bash 或 /bin/sh，然后在容器内部执行命令。

```bash
docker exec -it <container_name> /bin/bash
```
2. 运行单个命令：
使用 docker exec 运行单个命令，而不需要启动交互式 shell。

```bash 
docker exec <container_name> <command>
```

3. 在后台运行命令：
如果不想在终端中看到命令的输出，可以使用 -d 参数在后台运行命令。

```bash
docker exec -d <container_name> <command>
```

sudo virt-install  --name myvm   --memory 1024  --vcpus 1 --disk path=/var/lib/libvirt/images/myvm.img,size=8  --network bridge=br0 --graphics vnc,listen=0.0.0.0,port=5900   --location=/path/to/iso/or/qcow2/image --os-type=linux --os-variant=rhel7.0    --autoconsole

**OPTIONS**

    -d: 在后台运行命令。·
    -i: 即使没有附加也保持 STDIN 打开，通常与 -t 配合使用。
    -t: 进入容器的 CLI 模式，为命令分配一个伪终端。
    -e: 设置环境变量。
    --env-file: 读入环境变量文件。
    -w: 需要执行命令的目录。
    -u: 指定访问容器的用户名。

**注意事项**
    只能在正在运行的容器中使用 docker exec 命令。


## 2. 容器操作
### 2.1 docker ps

docker ps 是 Docker 的一个命令，用于列出当前正在运行的容器。通过该命令，可以查看容器的 ID、使用的镜像、创建时间、状态、端口映射以及名称等信息。

```bash
docker ps [OPTIONS]
```
**OPTIONS**

    -a, --all: 显示所有的容器，包括已经停止的容器。
    -q, --quiet: 仅显示容器的 ID。
    --format: 使用 Go 模板输出格式化的信息。
    --no-trunc: 不截断输出。
    -l, --latest: 显示最新创建的容器（包括停止的容器）。
    -n, --last int: 显示最新创建的 n 个容器。
    -s, --size: 显示容器的总大小。

**输出列说明**

    CONTAINER ID: 容器的唯一标识符。
    IMAGE: 容器所使用的镜像。
    COMMAND: 当容器启动时运行的命令。
    CREATED: 容器的创建时间。
    STATUS: 容器的当前状态（例如：Up 或 Exited）。
    PORTS: 容器映射到主机的端口。
    NAMES: 容器的名称。

### 2.2 docker inspect

docker inspect 是一个 Docker 命令，用于获取容器或镜像的元数据。这个命令返回 JSON 格式的数据，包含了容器或镜像的详细信息，如配置、状态、网络设置、卷挂载等。

```bash
docker inspect [OPTIONS] NAME|ID [NAME|ID...]
```

**OPTIONS**

    -f, --format: 指定返回值的模板文件，使用 Go 模板语法。通过此选项，可以定制输出的格式和内容。
    -s, --size: 显示容器或镜像的总大小（如果是容器类型）。
    --type: 为指定类型返回 JSON 数据

### 2.3 docker top

docker top 是一个 Docker 命令，用于查看容器中运行的进程。该命令提供了容器内部正在运行的进程的实时快照，这有助于了解容器内部的工作负载和正在执行的任务。
这将列出指定容器内所有正在运行的进程及其相关信息，如 PID（进程 ID）、USER（用户）、TTY（控制终端）、TIME（CPU 时间）、CMD（命令）。

```bash
docker top [OPTIONS] CONTAINER [ps OPTIONS]
```
### 2.4 docker attach

当前终端或命令行会话附加到正在运行的容器上。这样，就可以在容器内部运行命令、查看容器的输出，以及进行其他交互式操作

```bash
docker attach <容器ID或名称>
```

**OPTIONS**

    -i, --interactive：保持标准输入（STDIN）打开，即使没有附加到容器。这允许与容器进行交互。
    -t, --tty：分配一个伪终端（pseudo-TTY），通常与 -i 一起使用，以提供一个完整的终端会话。
    --detach-keys：覆盖用于从容器分离的键序列。默认情况下，按 Ctrl-p, Ctrl-q 可以从容器分离，但可以使用此选项来指定其他键序列。
    --sig-proxy：默认情况下，当在 docker attach 会话中按下 Ctrl-C 或 Ctrl-D 时，这些信号会被发送到容器中的进程。如果设置 --sig-proxy=false，则这些信号不会被代理到容器，从而允许更好地控制会话的结束。

### 2.5 docker events

用于实时输出 Docker 服务器端的事件。这些事件包括容器的创建、启动、关闭等。通过该命令，可以监控 Docker 容器的生命周期中的各种活动。

```bash
docker events [OPTIONS]
```
**OPTIONS**

    -f 或 --filter：根据配置条件过滤输出。例如，可以根据容器的名称、ID、状态或类型来过滤事件。
    --format：通过给定的模板格式化输出。这允许自定义事件的显示格式。
    --since：展示从某个时间节点开始的所有事件。时间节点应是以秒为单位的时间戳。
    --until：展示截止到某个时间节点的所有事件。时间节点同样是以秒为单位的时间戳。

如果发现 docker events 没有反应，可能是因为当前没有容器事件正在发生，或者存在权限、Docker 守护进程状态、客户端与服务端连接等其他问题。确保 Docker 服务正在运行，并且有足够的权限来查看事件。

### 2.6 docker logs

用于获取容器的日志输出。通过该命令，可以查看容器在运行过程中生成的标准输出（stdout）和标准错误输出（stderr），这有助于诊断容器的行为和排查问题。

```bash
docker logs [OPTIONS] CONTAINER
```
sudo yum install -y libvirt-dev
**OPTIONS**

    --details：显示日志的详细信息，包括容器名称或 ID 和时间戳。
    -f 或 --follow：实时跟踪容器的日志输出。当使用这个选项时，docker logs 会持续输出新的日志直到停止命令（通常是按 Ctrl+C）。
    --since：显示从某个时间节点开始的所有日志。时间节点可以是具体的日期时间（如 2023-06-30T13:10:39Z）或相对时间（如 50m 表示 50 分钟前）。
    --tail：仅显示最后几行的日志。例如，--tail 10 将显示最后 10 行的日志。
    --stderr 或 --stdout：分别只显示标准错误输出或标准输出。
    -t 或 --timestamps：在每条日志前添加时间戳。

结合 grep 或其他文本处理工具，可以进一步过滤和格式化 docker logs 的输出，以满足特定的需求。
请注意，如果遇到权限问题或容器没有运行，docker logs 命令可能无法正常工作。确保有足够的权限访问容器的日志，并且容器正在运行。如果容器未运行，需要先启动容器才能查看其日志。

### 2.7 docker wait

用于阻塞直到容器停止运行，然后返回容器的退出代码。这个命令在脚本中非常有用，因为它允许等待容器完成其任务，然后再进行下一步操作。

```bash
docker wait [OPTIONS] CONTAINER [CONTAINER...]
```

### 2.8 docker export

用于将 Docker 容器的文件系统导出为一个 tar 归档文件。这个命令在需要将容器的文件系统备份、迁移到其他系统或进行其他形式的离线分析时非常有用。

```bash
docker export [OPTIONS] CONTAINER
```

**OPTIONS**

    -o 或 --output：指定导出的 tar 文件的保存路径和文件名。如果不指定，则输出到标准输出（通常是终端或命令行界面），这通常不适合于大文件，因为它会导致数据难以管理。
    --quiet 或 -q：仅显示导出的容器的摘要信息，不显示详细输出。这有助于在脚本或自动化流程中减少不必要的输出。

导出的 tar 文件包含了容器的整个文件系统，可以用于在另一台机器上通过 docker import 命令重新创建一个容器。但请注意，这并不会导出容器的配置（如环境变量、卷挂载等），因此新创建的容器可能需要额外的配置才能与原始容器具有相同的行为。
如果需要迁移容器及其配置，通常更好的方法是使用 docker save 命令来保存容器及其镜像为一个 tar 文件，然后在目标系统上使用 docker load 命令来加载它。这样可以确保所有的依赖项和配置都被正确地迁移。

### 2.9 docker port

Docker port命令用于列出指定的容器的端口映射，或者查找将PRIVATE_PORT NAT到面向公众的端口。简单来说，Docker port命令可以帮助我们查看容器内部的端口是如何映射到宿主机的端口的，从而使得我们可以从外部访问容器中运行的应用程序。

```bash
docker port [OPTIONS] CONTAINER [PRIVATE_PORT[/PROTO]]
```

## 3. 容器 rootfs 命令

### 3.1 docker commit

用于根据Docker容器的改变创建一个新的Docker镜像。

```bash
docker commit [OPTIONS] CONTAINER [REPOSITORY[:TAG]]
```

**OPTIONS**

    -a, --author：指定新镜像的作者。
    -m, --message：设置提交的描述信息。
    -p, --pause：在提交时暂停容器的运行状态。
    --change：应用Dockerfile指令（如CMD、EXPOSE、ENV等）来创建新镜像。
    --squash：压缩提交的镜像层，以减小镜像大小。

### 3.2 docker cp

用于在Docker容器和主机之间复制文件或目录。

```bash
docker cp <source> <container>:<destination>
docker cp <container>:<source> <destination>
```
其中

- <source> 表示源文件或目录的路径，这可以是主机上的文件或目录。
- <container> 是目标容器的名称或ID。
- <destination> 表示目标路径，这可以是容器内的路径或主机上的路径。

docker cp命令假定容器路径相对于容器的根（/）目录，而主机路径则是相对于执行docker cp命令的当前目录。

### 3.3 docker diff

```bash
docker diff [OPTIONS] CONTAINER
```

当运行docker diff命令时，它会列出容器内的文件状态变化，这些变化包括三种状态：

- A：表示文件被添加（Add）。
- D：表示文件被删除（Delete）。
- C：表示文件内容被更改（Change）。

## 4. 镜像仓库

### 4.1 docker login\logout

```bash
docker login [OPTIONS] [SERVER]
docker logout [OPTIONS] [SERVER]
```
**OPTIONS**

    -u :登陆的用户名
    -p :登陆的密码

### 4.2 docker pull

用于从 Docker 镜像仓库下载（拉取）镜像。当需要在本地运行一个容器，但是本地并没有相应的镜像时，可以使用 docker pull 命令来获取这个镜像。

```bash
docker pull [OPTIONS] NAME[:TAG|@DIGEST]
```
- NAME：指定要拉取的镜像的名称。
- TAG：指定要拉取的镜像的标签，它通常用来标识镜像的版本。如果不指定标签，默认会拉取 latest 标签的镜像。
- DIGEST：镜像的摘要，通常用于确保拉取的是特定版本的镜像。

在执行 docker pull 命令时，Docker 客户端会首先检查本地是否有该镜像，如果没有，它会从配置的镜像仓库（默认为 Docker Hub）下载镜像到本地。如果镜像的某个层已经存在于本地，Docker 会利用缓存机制来避免重复下载相同的层，从而提高拉取镜像的效率。

### 4.3 docker push

用于将本地的 Docker 镜像推送到 Docker 镜像仓库中。这通常用于团队协作场景，确保所有成员都在使用相同的基础镜像。

```bash
docker push [OPTIONS] NAME[:TAG]
```
- NAME：指定要推送的镜像的名称。
- TAG：指定要推送的镜像的标签。如果不指定标签，则使用默认的 latest 标签。

**OPTIONS** 

    --all-tags 或 -a：推送具有给定仓库名的所有标记的本地镜像。
    --disable-content-trust：跳过镜像签名。

### 4.4 docker search

用于在 Docker Hub 或其他配置的镜像仓库中搜索镜像。通过搜索，可以查找公开的镜像，了解它们的描述、标签、星标数量以及官方状态等信息，从而找到适合的镜像。

```bash
docker search [OPTIONS] TERM
```
TERM：搜索的关键字或短语。

**OPTIONS**

    --automated 或 -a：只显示自动构建的镜像。
    --no-trunc：显示完整的镜像描述，而不是截断后的结果。
    --stars 或 -s：只显示具有至少指定星标数量的镜像。
    --official：只显示官方镜像。

docker search 默认在 Docker Hub 上进行搜索。如果配置了其他的镜像仓库，可以通过修改 Docker 的配置来使用不同的仓库进行搜索。

## 5. 本地镜像管理

### 5.1 docker images

用于列出主机上保存的所有 Docker 镜像。这个命令可以帮助查看本地有哪些镜像，以及每个镜像的详细信息，比如镜像的仓库名、标签、镜像 ID、创建时间以及所占用的空间大小

```bash
docker images [OPTIONS] [REPOSITORY[:TAG]]
```

**OPTIONS**

    -a 或 --all：显示所有镜像（包括中间层镜像）。默认情况下，docker images 只显示顶层镜像。
    --digests：显示镜像的摘要信息。
    -q 或 --quiet：只显示镜像 ID。
    --no-trunc：显示完整的镜像信息，不截断输出。

### 5.2 docker rmi

用于删除一个或多个 Docker 镜像。当不再需要某个镜像或者想要释放存储空间时，可以使用这个命令来删除它。

```bash
docker rmi [OPTIONS] IMAGE [IMAGE...]
```

**OPTIONS**

    -f 或 --force：强制删除一个或多个镜像，即使它们正在被容器使用。

删除镜像后，相关的容器和数据卷并不会被自动删除。如果需要删除容器或数据卷，需要使用 docker rm 命令删除容器，以及 docker volume rm 命令删除数据卷（如果它们不再需要的话）。

### 5.3 docker tag

用于给 Docker 镜像打标签（tag），或者将已有的标签更改为新的标签。标签有助于区分和管理不同版本的镜像。

```bash
docker tag SOURCE_IMAGE[:TAG] TARGET_IMAGE[:TAG]
```
SOURCE_IMAGE[:TAG]：源镜像及其标签。如果不指定标签，默认为 latest。
TARGET_IMAGE[:TAG]：目标镜像及其新标签。


### 5.4 docker build

用于根据 Dockerfile 创建 Docker 镜像。Dockerfile 是一个文本文件，其中包含了一系列命令和配置信息，用于定义如何构建 Docker 镜像

```bash
docker build [OPTIONS] PATH | URL | -
```

- PATH：指定包含 Dockerfile 的目录路径。Docker 会在该目录下查找 Dockerfile，并根据其中的指令构建镜像。
- URL：Git 仓库的 URL，Docker 会从该仓库克隆代码，并在克隆后的目录下查找 Dockerfile 进行构建。
- -：从标准输入（stdin）读取 Dockerfile 的内容，这通常与管道命令结合使用。

**OPTIONS** 

    --tag, -t：为构建的镜像指定标签（name:tag）。
    --file, -f：指定 Dockerfile 的路径，默认为 PATH 下的 Dockerfile。
    --build-arg：设置构建时的变量。
    --no-cache：构建时不使用缓存。

Docker 会按照 Dockerfile 中的指令顺序执行，包括设置基础镜像、安装软件包、复制文件等。构建完成后，会生成一个新的 Docker 镜像，可以使用 docker images 命令查看。
Dockerfile 的编写需要遵循一定的语法和规则，确保每个指令的正确性和有效性。此外，构建镜像可能需要一些时间，具体取决于 Dockerfile 中的指令和构建环境的性能。

### 5.5 docker history

用于查看 Docker 镜像的构建历史。它显示了构建镜像时执行的所有命令及其结果，帮助用户了解镜像的构建过程和每一层的变化。

```bash
docker history [OPTIONS] IMAGE
```
其中 IMAGE 是要查看历史的镜像名称或 ID。

**OPTIONS**

    --format：使用 Go 模板自定义输出格式。
    --human 或 -H：以人类可读的格式打印镜像大小和日期（默认为 true）。
    --no-trunc：显示完整的提交记录，包括命令的完整 ID。
    -q 或 --quiet：仅显示命令的 ID，不显示其他详细信息。

执行 docker history 命令后，会输出多列信息，每列代表镜像的一个层。通常包含以下列信息：

- ID：层的唯一标识符。
- CREATED：层的创建时间。
- SIZE：该层在镜像中的大小。
- COMMAND：在构建过程中执行的命令。


### 5.6 docker save

用于将 Docker 镜像保存到一个压缩的 tar 归档文件中。这对于备份、迁移或在没有直接访问 Docker 注册表的情况下共享镜像非常有用。

```bash
docker save [OPTIONS] IMAGE [IMAGE...]
```

- OPTIONS 是可选参数。
- IMAGE 是要保存的 Docker 镜像的名称或 ID。可以指定一个或多个镜像。

### 5.7 docker load

用于加载一个 tar 归档文件中的 Docker 镜像。这个命令在需要将一个或多个镜像从一个环境迁移到另一个环境时特别有用，尤其是当直接访问 Docker 注册表不方便或者不可行的时候。

```bash
docker load [OPTIONS]   
```

在 OPTIONS 中，可以使用 -i 或 --input 来指定包含 Docker 镜像的 tar 文件的路径。如果不指定 -i 选项，docker load 命令将从标准输入（stdin）读取数据。
加载的镜像将保存在 Docker 的本地镜像库中，与通过 docker pull 从注册表拉取的镜像一样，可以用于创建和运行容器。

### 5.8 docker import

用于将本地文件系统上的一个文件或一个URL导入为一个本地镜像。具体来说，它可以导入tar归档文件或URL指向的Docker镜像打包文件或打包了文件系统的tar文件。

```bash
docker import [OPTIONS] file|URL|- [REPOSITORY[:TAG]]
```
- file 表示要导入的文件或目录的路径。
- URL 表示要导入的文件或目录的URL。
- \- 表示从标准输入中读取tar归档文件。
- REPOSITORY 表示新镜像的仓库名称。
- TAG 表示新镜像的标签。

docker import命令提供了一种灵活的方式来创建本地Docker镜像，无论是从本地文件系统还是远程URL。

## 6. docker info/version
显示 Docker 系统和版本信息


# docker 部署常用中间件

**登录阿里云镜像仓库**
```bash
docker login --username=aliyun2399219595 registry.cn-shanghai.aliyuncs.com
```

## 1. docker部署 mysql

1. docker pull mysql
2. docker run -d --name mysql -v /docker/mysql/data:/var/lib/mysql -v /docker/mysql/log:/var/log  -v /docker/mysql/conf:/etc/mysql -p 13306:3306 -e MYSQL_ROOT_PASSWORD=123 mysql

mysql 配置文件路径：
```bash
/etc/my.cnf
/etc/conf.d/*
```

mysql 数据文件路径
```bash
/var/lib/mysql/
```

mysql log文件路径
```bash
/var/log
```

## 2. docker部署 redis

1. docker pull redis

    第一步：创建存储目录，日志目录，配置文件目录

    命令：mkdir -p /home/redis/{conf,data,log} 
    

    第二步：创建日志文件

    命令：touch /home/redis/log/redis.log


    第三步：将redis.conf文件上传到/home/redis/redis.conf目录

    因为docker部署的redis并不自带redis.conf文件，需要自己下载

    先切换目录，命令：cd /home/redis/redis.conf

    在线下载命令：wget http://download.redis.io/redis-stable/redis.conf

    服务器无网可在有网的电脑上浏览器输入以下地址下载，然后再上传服务器
    http://download.redis.io/redis-stable/redis.conf


    第四步：修改redis.conf配置文件以下内容
```bash
    # 关闭保护模式
    protected-mode no
    
    # 关闭后台运行(避坑提示，因为docker运行就已经有后台守护，改为yes会启动不了redis)
    daemonize no
    
    # 设置日志文件路径（避坑提示，此路径为容器内的路径，切勿当成宿主路径）
    logfile "/etc/redis.log"
    
    # 设置主服务器密码(为以后redis集群做基础，无集群需求可以不添加)
    masterauth 123456
    
    # 设置redis密码(如果以后有哨兵集群需求，主与从的redis密码必须一致)
    requirepass 123456
```
    第四步：赋予配置文件和日志文件权限

    命令：chmod 777 /home/redis/conf/redis.conf /home/redis/log/redis.log

运行时命令
```bash
docker run -d --name redis -v /docker/redis/data:/data -v /docker/redis/conf/redis.conf:/etc/redis/redis.conf -v /docker/redis/log/redis.log:/etc/redis.log -p 16379:6379 redis redis-server /etc/redis/redis.conf


docker run --name=redis --hostname=4c1ad6457a83 --mac-address=02:42:ac:11:00:02 --env=PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin --env=GOSU_VERSION=1.17 --env=REDIS_VERSION=7.2.5 --env=REDIS_DOWNLOAD_URL=http://download.redis.io/releases/redis-7.2.5.tar.gz --env=REDIS_DOWNLOAD_SHA=5981179706f8391f03be91d951acafaeda91af7fac56beffb2701963103e423d --volume=E:\docker\redis\conf\redis.conf:/etc/redis/redis.conf --volume=E:\docker\redis\data:/data --volume=E:\docker\redis\data\redis.log:/etc/redis.log --volume=/data --workdir=/data -p 6379:6379 --restart=no --runtime=runc -d redis:latest
```
redis-cli 登录命令
```bash
redis-cli -h 127.0.0.1 -p 6379 -a 123
```
注意事项：远程连接直接注释掉 bind，,如果没有设置密码，需要将 protect-mode no

## 3. docker部署 nacos

```bash
    ## 创建conf和logs文件夹
    mkdir -p /dockerImageFile/nacos/conf
    mkdir -p /dockerImageFile/nacos/logs
    ## 复制conf和logs文件夹
    docker cp nacostest:/home/nacos/logs/ /dockerImageFile/nacos/
    docker cp nacostest:/home/nacos/conf/ /dockerImageFile/nacos/

```

docker run 命令
```bash
docker run -d --name nacos -v /docker/nacos/data:/home/nacos/data -v /docker/nacos/logs:/home/nacos/logs -v /docker/nacos/conf:/home/nacos/conf/ -p 18848:8848  --env PREFER_HOST_MODE=hostname --env MODE=standalone --env NACOS_AUTH_ENABLE=true --privileged=true  nacos/nacos-server
```

application.properties 文件

```yaml
# spring
server.servlet.contextPath=${SERVER_SERVLET_CONTEXTPATH:/nacos}
server.contextPath=/nacos
server.port=${NACOS_APPLICATION_PORT:8848}
server.tomcat.accesslog.max-days=30
server.tomcat.accesslog.pattern=%h %l %u %t "%r" %s %b %D %{User-Agent}i %{Request-Source}i
server.tomcat.accesslog.enabled=${TOMCAT_ACCESSLOG_ENABLED:false}
server.error.include-message=ALWAYS
# default current work dir
server.tomcat.basedir=file:.
#*************** Config Module Related Configurations ***************#
### Deprecated configuration property, it is recommended to use `spring.sql.init.platform` replaced.
#spring.datasource.platform=${SPRING_DATASOURCE_PLATFORM:}
spring.sql.init.platform=${SPRING_DATASOURCE_PLATFORM:}
nacos.cmdb.dumpTaskInterval=3600
nacos.cmdb.eventTaskInterval=10
nacos.cmdb.labelTaskInterval=300
nacos.cmdb.loadDataAtStart=false
db.num=1
db.url.0=jdbc:mysql://192.168.10.129:13306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
db.user=root
db.password=123

## DB connection pool settings
db.pool.config.connectionTimeout=${DB_POOL_CONNECTION_TIMEOUT:30000}
db.pool.config.validationTimeout=10000
db.pool.config.maximumPoolSize=20
db.pool.config.minimumIdle=2
### The auth system to use, currently only 'nacos' and 'ldap' is supported:
nacos.core.auth.system.type=${NACOS_AUTH_SYSTEM_TYPE:nacos}
### worked when nacos.core.auth.system.type=nacos
### The token expiration in seconds:
nacos.core.auth.plugin.nacos.token.expire.seconds=${NACOS_AUTH_TOKEN_EXPIRE_SECONDS:18000}
### The default token:
nacos.core.auth.plugin.nacos.token.secret.key=${NACOS_AUTH_TOKEN:}
### Turn on/off caching of auth information. By turning on this switch, the update of auth information would have a 15 seconds delay.
nacos.core.auth.caching.enabled=${NACOS_AUTH_CACHE_ENABLE:false}
nacos.core.auth.enable.userAgentAuthWhite=${NACOS_AUTH_USER_AGENT_AUTH_WHITE_ENABLE:false}
nacos.core.auth.server.identity.key=${NACOS_AUTH_IDENTITY_KEY:}
nacos.core.auth.server.identity.value=${NACOS_AUTH_IDENTITY_VALUE:}
nacos.core.auth.system.type=nacos
nacos.core.auth.enabled=true
## spring security config
### turn off security
nacos.security.ignore.urls=${NACOS_SECURITY_IGNORE_URLS:/,/error,/**/*.css,/**/*.js,/**/*.html,/**/*.map,/**/*.svg,/**/*.png,/**/*.ico,/console-fe/public/**,/v1/auth/**,/v1/console/health/**,/actuator/**,/v1/console/server/**}
# metrics for elastic search
management.metrics.export.elastic.enabled=false
management.metrics.export.influx.enabled=false
nacos.naming.distro.taskDispatchThreadCount=10
nacos.naming.distro.taskDispatchPeriod=200
nacos.naming.distro.batchSyncKeyCount=1000
nacos.naming.distro.initDataRatio=0.9
nacos.naming.distro.syncRetryDelay=5000
nacos.naming.data.warmup=true
nacos.console.ui.enabled=true
nacos.core.param.check.enabled=true
```

执行 /home/nacos/conf 文件下的 sql 文件进行初始化


## 4. docker 部署 rabbitmq

1. 拉取镜像
```bash
docker pull rabbitmq
```

2. 启动容器

```bash
docker run -d --name rabbitmq rabbitmq
```

3. 复制目录

```bash
docker cp rabbitmq:/var/lib/rabbitmq /docker/rabbitmq/data
docker cp rabbitmq:/etc/rabbitmq/ /docker/rabbitmq/conf
docker cp rabbitmq:/var/log/rabbitmq /docker/rabbitmq/log
```

4. 删除容器
```bash
docker stop rabbitmq
docker rm rabbitmq
```

5. 运行rabbitmq 并挂载目录和暴露端口
```bash
docker run -d -p 5672:5672 -p 15672:15672 
-e RABBITMQ_DEFAULT_USER=admin 
-e RABBITMQ_DEFAULT_PASS=admin 
-v /docker/rabbitmq/data:/var/lib/rabbitmq 
-v /docker/rabbitmq/conf/:/etc/rabbitmq/ 
-v /docker/rabbitmq/log/:/var/log/rabbitmq/log 
--name rabbitmq rabbitmq
```

6. rabbitmq 开启管理面板插件
```bash
rabbitmq-plugins enable rabbitmq_management
```
7. channel Stats in management UI are disabled on this node 解决方案

```bash
# cat /etc/rabbitmq/conf.d/management_agent.disable_metrics_collector.conf 
management_agent.disable_metrics_collector = true
```

## 5. docker 部署 ES


1. 拉取镜像
```bash
docker pull elasticsearch
```

2. 启动容器

```bash
docker run -d --name es elasticsearch
```

3. 复制目录

```bash
docker cp es:/usr/share/elasticsearch /docker
```

4. 删除容器
```bash
docker stop es
docker rm es
```

5. es 并挂载目录和暴露端口

```bash
docker run -d \
--name=elastic-search \
-e ES_JAVA_OPTS="-Xms512m -Xmx512m" \
-e "discovery.type=single-node"  \
-p 9200:9200 \
-p 9300:9300 \
-v /docker/elasticsearch/data:/usr/share/elasticsearch/data \
-v /docker/elasticsearch/plugins:/usr/share/elasticsearch/plugins \
-v /docker/elasticsearch/config:/usr/share/elasticsearch/config \
-v /docker/elasticsearch/logs:/usr/share/elasticsearch/logs \
elasticsearch

```
