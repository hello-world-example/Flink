# Standalone HA 部署

> 请先参考 [Standalone 集群方式]({{< relref "/docs/Opt/Install-By-Standalone-Cluster.md" >}})， HA 的部署基于此



## 概述

- Standalone 独立集群部署方式，默认只有一个 JobManager
- 如果这个 JobManager 挂掉，则 无法提交程序，程序也会报错
- Flink 的 HA 模式是 保证 JobManager 的高可用，**多个 JobManager 做主备**，一个挂掉之后，备机可以及时顶上
- 主备 JobManager 通过 Zookeeper 实现
- 依赖
  - Hadoop HDFS
  - Zookeeper（可内置）



## HDFS 搭建

> 参考： [Hadoop 快速搭建](/Hadoop/docs/Quick-Start/Install-by-Docker/)



## ??? 启用 内嵌 Zookeeper

> 【注意】以下操作全在 `s220.flink.learn.wx` 执行

```bash
$ vim conf/zoo.cfg
# ZooKeeper quorum peers
server.0=s220.flink.learn.wx:2888:3888
server.1=s221.flink.learn.wx:2888:3888
server.2=s222.flink.learn.wx:2888:3888
# server.2=host:peer-port:leader-port

# 分发 zoo 配置文件
$ scp -r -P2208 /opt/websuite/flink-1.11.1/conf/zoo.cfg flink@s221.flink.learn.wx:/opt/websuite/flink-1.11.1/conf/zoo.cfg
$ scp -r -P2208 /opt/websuite/flink-1.11.1/conf/zoo.cfg flink@s222.flink.learn.wx:/opt/websuite/flink-1.11.1/conf/zoo.cfg


# 启动 Zookeeper 集群（）
$ bin/start-zookeeper-quorum.sh

# 查看日志
$ tail -fn 400 log/flink-flink-zookeeper-*

# 停止 Zookeeper 集群
# $ bin/stop-zookeeper-quorum.sh
```



## 新增 Standby 节点

```bash
$ vim /etc/hosts
# 现有
172.16.2.220    s220.flink.learn.wx     # JobManager-Master
172.16.2.221    s221.flink.learn.wx     # TaskManager
172.16.2.222    s222.flink.learn.wx     # TaskManager
# 新增 
172.16.2.223    s223.flink.learn.wx     # JobManager-Standby


# 配置免密登陆，
```



## 配置 conf/masters

```bash
$ vim conf/masters
s220.flink.learn.wx:8081
s223.flink.learn.wx:8081
```



## 开启 HA

```bash
$ vim conf/flink-conf.yaml

high-availability: zookeeper

# 需要手动添加 Hadoop jar 包依赖，详见 FAQ
high-availability.storageDir: hdfs://172.16.2.108/flink/ha/

# 这里无需配置 chroot: zk:2181/flink
high-availability.zookeeper.quorum: 172.16.2.183:2181

# Flink 的 znode 默认是 /{zookeeper.path.root:flink}/{cluster-id:default}/xxx
# zookeeper.path.root 控制一级目录，默认 flink
high-availability.zookeeper.path.root: /flink
# cluster-id 控制二级目录，默认 default，【同时控制 HDFS 和 Zookeeper Path 】
high-availability.cluster-id: /cluster_learn
```



## 分发与启动

> 【注意】以下操作全在 `s220.flink.learn.wx` 执行

``` bash
# Copy 到 s223.flink.learn.wx
$ scp -r -P2208 /opt/websuite/flink-1.11.1 flink@s223.flink.learn.wx:/opt/websuite/
# Copy 到 s221.flink.learn.wx
$ scp -r -P2208 /opt/websuite/flink-1.11.1 flink@s221.flink.learn.wx:/opt/websuite/
# Copy 到 s222.flink.learn.wx
$ scp -r -P2208 /opt/websuite/flink-1.11.1 flink@s222.flink.learn.wx:/opt/websuite/

# 启动集群
$ ./bin/start-cluster.sh

# 这时访问 http://s223.flink.learn.wx:8081 
# 和      http://s220.flink.learn.wx:8081  都可以访问
```



## HA 失败恢复测试

- 如何确定 Master 实例
  - 打开 http://s220.flink.learn.wx:8081/#/job-manager/config
  - **jobmanager.rpc.address** 对应的地址即 Master 实例

``` bash
# 假如 当前 Master 实例是 s220.flink.learn.wx， 则登陆该机器

# 关闭 master 节点
$ ./bin/jobmanager.sh stop

# 启动节点
$ ./bin/jobmanager.sh start
# 刚开始会报选举的错误，之后会恢复正常
{"errors":["Service temporarily unavailable due to an ongoing leader election. Please refresh."]}
```





## FAQ

### UnsupportedFileSystemSchemeException: Hadoop is not in the classpath/dependencies

- 到 https://repo.maven.apache.org/maven2/org/apache/flink/flink-shaded-hadoop-2-uber/ 找到对应的版本
- 这里选择 `flink-shaded-hadoop-2-uber-2.8.3-10.0.jar`
- 下载后，拷贝到 `./lib/` 目录下，然后重启



### Permission denied: user=flink, access=WRITE

```bash
$ sudo su - hdfs
$ hadoop fs -mkdir /flink   
$ hadoop fs -chown flink:flink /flink
$ hadoop fs -ls / | grep flink
drwxr-xr-x   - flink flink               0 2020-08-26 15:21 /flink
```



### java.io.NotSerializableException: org.apache.flink.runtime.rest.messages.ResourceProfileInfo

- 1.11.1 版本的 Bug
- @see https://github.com/apache/flink/pull/12991
- @see http://apache-flink-user-mailing-list-archive.2336050.n4.nabble.com/NotSerializableException-org-apache-flink-runtime-rest-messages-ResourceProfileInfo-td36860.html



## Read More

- [JobManager High Availability (HA)](https://ci.apache.org/projects/flink/flink-docs-release-1.11/ops/jobmanager_high_availability.html)
- [Flink JobManager HA模式部署（基于Standalone）](https://www.cnblogs.com/liugh/p/7482571.html)