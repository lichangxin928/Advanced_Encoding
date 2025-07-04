给Docker中的container挂载新的文件目录，可以通过以下几种方式实现：

### 1. 使用`-v`参数在容器启动时挂载目录

在启动容器时，可以通过`-v`参数将宿主机上的目录挂载到容器中。例如，要将宿主机上的`/home/user/new_data`目录挂载到容器中的`/new_data`目录，可以使用以下命令：

```bash
docker run -v /home/user/new_data:/new_data image_name
```

- `/home/user/new_data`：宿主机上的目录路径。
- `/new_data`：容器中的目录路径。
- `image_name`：要启动的镜像名称。

### 2. 使用数据卷（Volume）挂载

Docker数据卷是一种可以在多个容器之间共享的特殊目录。可以通过以下步骤创建和使用数据卷：

#### 2.1 创建数据卷

```bash
docker volume create my_new_volume
```

#### 2.2 挂载数据卷到容器

在启动容器时，可以使用`--mount`或`-v`参数将数据卷挂载到容器中。例如：

```bash
docker run --mount source=my_new_volume,target=/new_data image_name
# 或者
docker run -v my_new_volume:/new_data image_name
```

- `my_new_volume`：创建的数据卷名称。
- `/new_data`：容器中的目录路径。
- `image_name`：要启动的镜像名称。

### 3. 修改已运行容器的配置（不推荐）

对于已经运行的容器，通常不推荐直接修改其配置文件来添加挂载目录，因为这可能导致容器状态不稳定或数据丢失。但如果确实需要这样做，可以：

#### 3.1 找到容器配置文件的位置

Docker容器的配置文件通常存储在`/var/lib/docker/containers/`目录下，每个容器都有一个对应的文件夹。

#### 3.2 修改配置文件

在容器的配置文件中，可以添加新的挂载点配置。但请注意，直接修改配置文件可能导致Docker守护进程重启后配置失效。

### 4. 使用`docker cp`命令复制文件到容器中

如果不希望通过挂载的方式将文件目录添加到容器中，也可以使用`docker cp`命令将文件从宿主机复制到容器中。例如：

```bash
docker cp /home/user/new_data/file.txt container_name:/new_data/
```

- `/home/user/new_data/file.txt`：宿主机上的文件路径。
- `container_name`：容器的名称或ID。
- `/new_data/`：容器中的目标目录路径。

### 总结

推荐在容器启动时通过`-v`参数或数据卷来挂载新的文件目录。这样可以确保容器与宿主机之间的数据同步和共享，同时保持容器的可移植性和隔离性。如果需要将文件复制到容器中，可以使用`docker cp`命令。