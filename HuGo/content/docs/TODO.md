# TODO

- 内置 Zookeeper ？？
- SQL API ？？
- 自定义监控指标



- Flink SQL： https://ci.apache.org/projects/flink/flink-docs-release-1.11/dev/table/sql/queries.html#operations
- Flink CEP： https://ci.apache.org/projects/flink/flink-docs-release-1.11/dev/libs/cep.html
- 



## 核心概念

- Window 窗口的类型
  - 滚动时间窗口
  - 滑动时间窗口
  - 数据大小窗口
  - ..
- Time 时间
  - 事件时间
  - 摄入时间
  - 处理时间
  - ..
- 并行度（不能大于 Slot 个数）
  - 算子级别
  - 运行环境级别
  - 客户端级别
  - 系统级别