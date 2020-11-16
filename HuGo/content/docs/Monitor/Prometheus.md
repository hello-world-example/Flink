# Flink Prometheus



- Flink 基于 Prometheus 的数据传递方式有两种
  - Flink 暴露数据接口，Prometheus 定时调用数据接口进行收集（PrometheusReporter）
    - 优点： 监控策略变更的时候，无需修改 Flink，调整 Prometheus 即可
    - 缺点：Flink 扩容的时候，需要修改 Prometheus 配置
  - Flink 主动推送给 Prometheus [pushgateway](https://github.com/prometheus/pushgateway)
    - 优缺点与 PrometheusReporter 方式相反
- 以下使用 PrometheusReporter 的方式



## 开启 PrometheusReporter

```bash
$ vim conf/flink-conf.yaml 
metrics.reporters: prom
metrics.reporter.prom.class: org.apache.flink.metrics.prometheus.PrometheusReporter
metrics.reporter.prom.port: 9999
 

# 删除日志
$ rm -rf log/*
# 分发配置
$ scp -r -P2208 /opt/websuite/flink-1.11.1 flink@s221.flink.learn.wx:/opt/websuite/
$ scp -r -P2208 /opt/websuite/flink-1.11.1 flink@s222.flink.learn.wx:/opt/websuite/
$ scp -r -P2208 /opt/websuite/flink-1.11.1 flink@s223.flink.learn.wx:/opt/websuite/

# 启动集群
$ ./bin/start-cluster.sh

# 查看 暴露的 Prometheus 指标
$ curl http://s220.flink.learn.wx:9999
$ curl http://s221.flink.learn.wx:9999
```



## 配置 Prometheus

> 安装 Prometheus ：略

```yaml
# 增加以下配置并重启
$ vim prometheus.yml 

scrape_configs:
  - job_name: 'flink_learn_cluster'
    static_configs:
    - targets:
      - s220.flink.learn.wx:9999
      - s221.flink.learn.wx:9999
      - s222.flink.learn.wx:9999
      - s223.flink.learn.wx:9999
```



## 配置 Grafana 报表

> - 安装 Grafana ：略
> - 导入 Dashboard https://grafana.com/grafana/dashboards/11049
> - @see [grafana-flink-databoard.json](/Flink/docs/Monitor/grafana-flink-databoard.json)



## Read More

- [Flink 和 Prometheus：流式应用程序的云原生监控](https://ververica.cn/developers/flink-and-prometheus/)

- [Flink and Prometheus: Cloud-native monitoring of streaming applications](https://flink.apache.org/features/2019/03/11/prometheus-monitoring.html)

- [Prometheus (org.apache.flink.metrics.prometheus.PrometheusReporter)](https://ci.apache.org/projects/flink/flink-docs-stable/monitoring/metrics.html#prometheus-orgapacheflinkmetricsprometheusprometheusreporter)

  