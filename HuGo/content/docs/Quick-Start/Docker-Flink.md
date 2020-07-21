# Docker Flink



## help

```bash
$ docker pull flink:1.11.0

# 查看帮助
$ docker run --rm flink:1.11.0 help  
```



## standalone-job

```bash
$ docker run -d \
  -p 18081:8081 \
  -p 16123:6123 \
  --name flink_standalone \
  flink:1.11.0 standalone-job
```



## Cluster

```bash
$ docker network create flink-network

# Job Manager
$ docker run -d \
  -p 28081:8081 \
  -p 26123:6123 \
  --network flink-network \
  --env FLINK_PROPERTIES="jobmanager.rpc.address: jobmanager" \
  --name jobmanager \
  flink:1.11.0 jobmanager

# Task Manager
$ docker run -d \
  --network flink-network \
  --env FLINK_PROPERTIES="jobmanager.rpc.address: jobmanager" \
  --name taskmanager01 \
  flink:1.11.0 taskmanager

# Task Manager 02
$ docker run -d \
  --network flink-network \
  --env FLINK_PROPERTIES="jobmanager.rpc.address: jobmanager" \
  --name taskmanager02 \
  flink:1.11.0 taskmanager
  
# 设置时区
$ docker exec -it jobmanager ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
$ docker exec -it taskmanager01 ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
$ docker exec -it taskmanager02 ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
```





## Read More

- 部署与运维 > 集群与部署 > [Docker](https://ci.apache.org/projects/flink/flink-docs-stable/zh/ops/deployment/docker.html)
- [apache / flink-docker](https://github.com/apache/flink-docker)
- [Dockerhub Flink](https://hub.docker.com/_/flink)