# Flink Source





## ExecutionEnvironment

| 内置 Source                                              | 简介                                     |      |
| -------------------------------------------------------- | ---------------------------------------- | ---- |
| fromElements(T …)                                        | 从给定的对象序列中创建数据流             |      |
| fromCollection(Collection)                               | 从 java.util.Collection 创建数据流       |      |
| fromCollection(Iterator, Class)                          | 通过 java.util.Iterator 创建数据流       |      |
| fromParallelCollection(SplittableIterator, Class)        | 创建并行数据流                           |      |
| generateSequence(from, to)                               | 生成指定区间范围内的数字序列的并行数据流 |      |
|                                                          |                                          |      |
| readTextFile(path)                                       | 读取文件                                 |      |
| readCsvFile(csvPath) .types(String.class, Integer.class) | 读取 CSV 文件，指定类型                  |      |





## StreamExecutionEnvironment

| 内置 Source        | 简介                   |      |
| ------------------ | ---------------------- | ---- |
| readTextFile(path) | 读取文件，检测文件变化 |      |
|                    |                        |      |
|                    |                        |      |







## 自定义 Source

















## Read More

- [Flink基础（十）：Flink常用的Source和Sink](https://blog.csdn.net/weixin_42155491/article/details/105280877)



Source

- env.readTextFile
- env.socketTextStream
- Connector