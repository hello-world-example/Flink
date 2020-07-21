# flink-conf.yaml

- 不作任何修改的情况下，默认是单节点
- 如果自定义 Java 版本可通过 `env.java.home` / `JAVA_HOME` 指定



|      | Key                                                          | Def Value | Desc                        |
| :--: | ------------------------------------------------------------ | --------- | --------------------------- |
|      | `jobmanager.rpc.address`                                     | localhost | TaskManager 链接 JobManager |
|      | `jobmanager.rpc.port`                                        | 6123      | TaskManager 链接 JobManager |
|      | `rest.address`                                               | 0.0.0.0   | Client REST API             |
|      | `rest.port`                                                  | 8081      | Client REST API             |
|      |                                                              |           |                             |
| Mem  | `jobmanager.memory.process.size` / ~~`jobmanager.heap.size`~~ | 1600m     |                             |
| Mem  | `taskmanager.memory.process.size` / ~~`taskmanager.heap.size`~~ | 1728m     |                             |
|      |                                                              |           |                             |
| CPU  | `taskmanager.numberOfTaskSlots`                              |           |                             |
| CPU  | `parallelism.default`                                        |           |                             |
|      |                                                              |           |                             |
|  IO  | state.backend                                                |           |                             |
|  IO  | state.checkpoints.dir                                        |           |                             |
|  IO  | state.savepoints.dir                                         |           |                             |
|  IO  | io.tmp.dirs                                                  |           |                             |
|      |                                                              |           |                             |
|      | `web.submit.enable`                                          | true      | 是否允许在 UI 上提交作业    |



## Read More

- [Deployment & Operations Configuration](https://ci.apache.org/projects/flink/flink-docs-stable/ops/config.html)
- [Flink 配置文件详解](http://www.54tianzhisheng.cn/2018/10/27/flink-config/)

