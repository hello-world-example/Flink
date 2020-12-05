# FAQ



## No ExecutorFactory found to execute the application

> Starting from Flink 1.11.0, the `flink-streaming-java` module does not have a dependency on `flink-clients` anymore
>
> —— [Release Notes - Flink 1.11](https://ci.apache.org/projects/flink/flink-docs-master/release-notes/flink-1.11.html#reversed-dependency-from-flink-streaming-java-to-flink-client-flink-15090)

添加依赖

```xml
<dependency>
  <groupId>org.apache.flink</groupId>
  <artifactId>flink-clients_${flink.scala.version}</artifactId>
</dependency>
```

