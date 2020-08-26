# FAQ



### 升级到 1.11.1 本地运行报 "No ExecutorFactory found to execute the application"

需要显示引用 `flink-clients` 

```xml
<dependency>
  <groupId>org.apache.flink</groupId>
  <artifactId>flink-clients_${flink.scala.version}</artifactId>
</dependency>
```

> Starting from Flink 1.11.0, the `flink-streaming-java` module does not have a dependency on `flink-clients` anymore
>
>  —— [Release Notes - Flink 1.11](https://ci.apache.org/projects/flink/flink-docs-master/release-notes/flink-1.11.html#reversed-dependency-from-flink-streaming-java-to-flink-client-flink-15090)



### Read More

- [Apache Flink常见问题汇总【精品问答】](https://developer.aliyun.com/ask/288158)
- [Apache Flink 中文用户邮件列表](http://apache-flink.147419.n8.nabble.com/)

