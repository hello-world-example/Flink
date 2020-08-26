# Standalone 集群方式



## 环境准备

- 关闭防火墙
- 配置 ntp
- JDK 1.8.x+
  - 配置 `JAVA_HOME` 或 
  - 在 `conf/flink-conf.yaml` 配置 `env.java.home` 指定 JDK 安装路径
- 创建 `flink` 用户
- ssh 免密登陆
  - 如果非默认端口，通过 在 `conf/flink-conf.yaml` 配置 `env.ssh.opts: -p 2208` 指定 ssh 端口
- 相同的目录结构（便于内置脚本会自动启动集群）
  - `/opt/websuite/flink-1.11.1`





## 集群节点

/etc/hosts

```bash
172.16.2.220    s220.flink.learn.wx     # JobManager-Master
172.16.2.221    s221.flink.learn.wx     # TaskManager
172.16.2.222    s222.flink.learn.wx     # TaskManager
```



## 创建 flink 用户

``` bash
# 切换到 root 账户
$ sudo su - 

# 创建用户
$ useradd flink
# 设置密码 flink
$ passwd flink

# 尝试登录（需密码）
$ ssh -p 2208 flink@s221.flink.learn.wx
```



## ssh 免密配置

> - 【注意】以下操作全在 `s220.flink.learn.wx` 执行
> - 只配置了 `s220` 可以免密登陆 `s221` 和 `s222`，反向并不能免密

```bash
# 切换 flink 用户
$ su - flink

# 生成 公钥和私钥
$ ssh-keygen -t rsa -P "" -f ~/.ssh/id_rsa -C "s220.flink.learn.wx"

$ ll .ssh/
id_rsa       # 私钥
id_rsa.pub   # 公钥

# 当前机器 到 当前机器的免密登陆
$ cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
$ chmod g-w ~/.ssh && chmod 600 ~/.ssh/authorized_keys
$ ssh -p2208 flink@127.0.0.1


# Copy 公钥到 s221.flink.learn.wx， 并设置权限，前两步需要密码
$ ssh -p2208 flink@s221.flink.learn.wx "mkdir -p .ssh && cat>>.ssh/authorized_keys" <.ssh/id_rsa.pub
$ ssh -p2208 flink@s221.flink.learn.wx "chmod g-w .ssh && chmod 600 .ssh/authorized_keys"
# 测试免密是否成功（记得 exist 退出）
$ ssh -p 2208 flink@s221.flink.learn.wx


# Copy 公钥到 s222.flink.learn.wx， 并设置权限，前两步需要密码
$ ssh -p2208 flink@s222.flink.learn.wx "mkdir -p .ssh && cat>>.ssh/authorized_keys" <.ssh/id_rsa.pub
$ ssh -p2208 flink@s222.flink.learn.wx "chmod g-w .ssh && chmod 600 .ssh/authorized_keys"
# 测试免密是否成功（记得 exist 退出）
$ ssh -p 2208 flink@s222.flink.learn.wx
```

> - 如果免密配置失败，查看 `/var/log/secure` 日志文件中的提示信息



## flink 非 HA 配置

> 【注意】以下操作全在 `s220.flink.learn.wx` 执行

```bash
$ /opt/websuite

$ tar zxvf flink-1.11.1-bin-scala_2.12.tgz
$ cd /opt/websuite/flink-1.11.1

# 修改 flink 配置
$ vim conf/flink-conf.yaml 
### 修改
> jobmanager.rpc.address: s220.flink.learn.wx
> taskmanager.numberOfTaskSlots: 4
### 新增 ssh 端口配置
> env.ssh.opts: -p 2208

# 非 HA 模式下无需配置
# $ vim conf/masters
# > s220.flink.learn.wx:8081

# TaskManager 机器列表
$ vim conf/workers 
> s221.flink.learn.wx
> s222.flink.learn.wx
```

## 分发程序

> 【注意】以下操作全在 `s220.flink.learn.wx` 执行

```bash
# Copy 到 s221.flink.learn.wx
$ scp -r -P2208 /opt/websuite/flink-1.11.1 flink@s221.flink.learn.wx:/opt/websuite/
# Copy 到 s222.flink.learn.wx
$ scp -r -P2208 /opt/websuite/flink-1.11.1 flink@s222.flink.learn.wx:/opt/websuite/

# 后续如有文件变更，可单独拷贝
$ scp -r -P2208               /opt/websuite/flink-1.11.1/conf/flink-conf.yaml \
    flink@s222.flink.learn.wx:/opt/websuite/flink-1.11.1/conf/flink-conf.yaml
```

## 启动集群

> 【注意】以下操作全在 `s220.flink.learn.wx` 执行

``` bash
$ cd /opt/websuite/flink-1.11.1

# 启动集群
$ ./bin/start-cluster.sh 
Starting cluster.
Starting standalonesession daemon on host s220.dev.learn.ttp.wx.
Starting taskexecutor daemon on host s221.dev.learn.ttp.wx.
Starting taskexecutor daemon on host s222.dev.learn.ttp.wx.

# 关闭集群
$ ./bin/stop-cluster.sh 
No taskexecutor daemon (pid: 21865) is running anymore on s221.dev.learn.ttp.wx.
No taskexecutor daemon (pid: 25712) is running anymore on s222.dev.learn.ttp.wx.
Stopping standalonesession daemon (pid: 7160) on host s220.dev.learn.ttp.wx.
```



## FAQ

### Could not get JVM parameters and dynamic configurations properly.

检查 `$JAVA_HOME`  对应的 JDK 版本是否是 `1.8.x+` ， `echo $JAVA_HOME` 查看环境变量



## Read More

- [Standalone Cluster](https://ci.apache.org/projects/flink/flink-docs-release-1.11/ops/deployment/cluster_setup.html)