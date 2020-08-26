# Local 安装



## 安装

```bash
$ java -version
java version "1.8.0_151"
Java(TM) SE Runtime Environment (build 1.8.0_151-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.151-b12, mixed mode)

# https://flink.apache.org/downloads.html
# 
$ cd /opt/package
$ wget https://downloads.apache.org/flink/flink-1.11.1/flink-1.11.1-bin-scala_2.12.tgz
$ cd /opt/websuite
$ cp /opt/package/flink-1.11.1-bin-scala_2.12.tgz .
$ tar zxvf flink-1.11.1-bin-scala_2.12.tgz
$ rm flink-1.11.1-bin-scala_2.12.tgz

# 启动
$ cd /opt/websuite/flink-1.11.1/
$ ./bin/start-cluster.sh

# 启动的 Java 进程
$ jps
34240 TaskManagerRunner
33989 StandaloneSessionClusterEntrypoint

# 访问
http://localhost:8081

# 启动日志
$ tail -n 400 log/flink-*-standalonesession-*log*
- --------------------------------------------------------------------------------
...
- Trying to start actor system at localhost:6123
...
- Actor system started at akka.tcp://flink@localhost:6123
...
- Actor system started at akka.tcp://flink-metrics@localhost:50929
...
- Rest endpoint listening at localhost:8081
...
- Web frontend listening at http://localhost:8081.
...
- Association with remote system [akka.tcp://flink@192.168.1.4:50931] has failed, address is now gated for [50] ms. Reason: [Disassociated] 
...
- Stopped BLOB server at 0.0.0.0:50928


# 关闭
# $ ./bin/stop-cluster.sh

```



## Read More

- [Local Setup Tutorial](https://ci.apache.org/projects/flink/flink-docs-stable/ops/deployment/local.html)
- [《Flink官方文档》Quick Start](http://ifeve.com/flink-quick-start/)

