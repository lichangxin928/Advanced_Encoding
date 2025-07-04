### 1. 关闭防火墙

```shell
systemctl stop firewalld
systemctl disable firewalld
```

### 2. 安装 SSH

```shell
yum install sshd or yum install openssh-server
service sshd start
```

### 3. chmod

```shell
可选项：
  -c, --changes          like verbose but report only when a change is made (若该档案权限确实已经更改，才显示其更改动作)
  -f, --silent, --quiet  suppress most error messages  （若该档案权限无法被更改也不要显示错误讯息）
  -v, --verbose          output a diagnostic for every file processed（显示权限变更的详细资料）
       --no-preserve-root  do not treat '/' specially (the default)
       --preserve-root    fail to operate recursively on '/'
       --reference=RFILE  use RFILE's mode instead of MODE values
  -R, --recursive        change files and directories recursively （以递归的方式对目前目录下的所有档案与子目录进行相同的权限变更)
       --help		显示此帮助信息
       --version		显示版本信息
mode ：权限设定字串，详细格式如下：
[ugoa...][[+-=][rwxX]...][,...]，其中
[ugoa...]
u 表示该档案的拥有者，g 表示与该档案的拥有者属于同一个群体(group)者，o 表示其他以外的人，a 表示所有（包含上面三者）。
[+-=]
+ 表示增加权限，- 表示取消权限，= 表示唯一设定权限。
[rwxX]
r 表示可读取，w 表示可写入，x 表示可执行，X 表示只有当该档案是个子目录或者该档案已经被设定过为可执行。
 	
file...
文件列表（单个或者多个文件、文件夹）

```

#### 数字权限使用格式

在这种使用方式中，首先我们需要了解数字如何表示权限。 首先，我们规定 数字 4 、2 和 1表示读、写、执行权限（具体原因可见下节权限详解内容），即 r=4，w=2，x=1 。此时其他的权限组合也可以用其他的八进制数字表示出来，如： rwx = 4 + 2 + 1 = 7 rw = 4 + 2 = 6 rx = 4 +1 = 5 即

若要同时设置 rwx (可读写运行） 权限则将该权限位 设置 为 4 + 2 + 1 = 7 若要同时设置 rw- （可读写不可运行）权限则将该权限位 设置 为 4 + 2 = 6 若要同时设置 r-x （可读可运行不可写）权限则将该权限位 设置 为 4 +1 = 5

上面我们提到，每个文件都可以针对三个粒度，设置不同的rwx(读写执行)权限。即我们可以用用三个8进制数字分别表示 拥有者 、群组 、其它组( u、 g 、o)的权限详情，并用chmod直接加三个8进制数字的方式直接改变文件权限。语法格式为 ：