# Monitoring REST API



- 在 `flink-conf.yaml` 通过 `rest.port` 配置 API 端口
- REST API 和 Web UI 公用一个端口
- 如果无需二次开发，大部分功能可通过 Web UI 查看



## Cluster

| Method | URI                            | Desc     |
| -----: | ------------------------------ | -------- |
|    GET | /config                        | 查看配置 |
|    GET | /overview                      | 集群概况 |
|   POST | /savepoint-disposal            |          |
|    GET | /savepoint-disposal/:triggerid |          |
| DELETE | /cluster                       | 关闭集群 |



## JobManager

| Method | URI                                                          | Desc                                    |
| -----: | ------------------------------------------------------------ | --------------------------------------- |
|    GET | **/jobmanager/config**                                       | JobManager 配置                         |
|    GET | /jobmanager/logs                                             | JobManager 日志文件                     |
|    GET | **/jobmanager/metrics[?get=,,]**                             | JobManager 指标，get 参数选择指定的指标 |



## TaskManager


| Method | URI                                                          | Desc                                    |
| -----: | ------------------------------------------------------------ | --------------------------------------- |
|    GET | /taskmanagers                                                |                                         |
|    GET | /taskmanagers/metrics                                        |                                         |
|    GET | /taskmanagers/:taskmanagerid                                 |                                         |
|    GET | /taskmanagers/:taskmanagerid/logs                            |                                         |
|    GET | /taskmanagers/:taskmanagerid/metrics                         |                                         |
|    GET | /taskmanagers/:taskmanagerid/thread-dump                     |                                         |



## Jobs

| Method | URI                                                          | Desc                                    |
| -----: | ------------------------------------------------------------ | --------------------------------------- |
|    GET | /jobs                                                        | 查看任务和当前状态                      |
|   POST | /jobs                                                        | 提交作业请求                            |
|    GET | **/jobs/metrics[?get=&agg=&jobs=]**                          | 作业 指标                               |
|    GET | **/jobs/overview**                                           | 作业概况                                |
|        |                                                              |                                         |
|    GET | **/jobs/:jobid**                                             | 作业详情                                |
|  PATCH | /jobs/:jobid                                                 | 中断 job                                |
|    GET | **/jobs/:jobid/plan**                                        |                                         |
|    GET | **/jobs/:jobid/config**                                      | Job 配置                                |
|    GET | /jobs/:jobid/exceptions                                      | Job 异常                                |
|    GET | /jobs/:jobid/execution-result                                | Job 执行结果                            |
|    GET | **/jobs/:jobid/metrics[?get=]**                              | Job 指标                                |
|  PATCH | /jobs/:jobid/rescaling                                       |                                         |
|    GET | /jobs/:jobid/rescaling/:triggerid                            |                                         |
|    GET | /jobs/:jobid/accumulators                                    |                                         |
|        |                                                              |                                         |
|    GET | /jobs/:jobid/checkpoints                                     | checkpoints 统计                        |
|   POST | /jobs/:jobid/savepoints                                      | 触发 savepoints                         |
|    GET | /jobs/:jobid/savepoints/:triggerid                           | 获取 savepoints 状态                    |
|   POST | /jobs/:jobid/stop                                            |                                         |
|    GET | /jobs/:jobid/checkpoints/config                              | checkpoints 配置                        |
|    GET | /jobs/:jobid/checkpoints/details/:checkpointid               | checkpoints 详情                        |
|    GET | /jobs/:jobid/checkpoints/details/:checkpointid/subtasks/:vertexid | 任务和子任务的检查点信息                |



## Jobs Vertices

| Method | URI                                                          | Desc                                    |
| -----: | ------------------------------------------------------------ | --------------------------------------- |
|    GET | /jobs/:jobid/vertices/:vertexid                              |                                         |
|    GET | /jobs/:jobid/vertices/:vertexid/accumulators                 |                                         |
|    GET | /jobs/:jobid/vertices/:vertexid/backpressure                 |                                         |
|    GET | /jobs/:jobid/vertices/:vertexid/metrics                      |                                         |
|    GET | /jobs/:jobid/vertices/:vertexid/subtasks/accumulators        |                                         |
|    GET | /jobs/:jobid/vertices/:vertexid/subtasks/metrics             |                                         |
|    GET | /jobs/:jobid/vertices/:vertexid/subtasks/:subtaskindex       |                                         |
|    GET | /jobs/:jobid/vertices/:vertexid/subtasks/:subtaskindex/attempts/:attempt |                                         |
|    GET | /jobs/:jobid/vertices/:vertexid/subtasks/:subtaskindex/attempts/:attempt/accumulators |                                         |
|    GET | /jobs/:jobid/vertices/:vertexid/subtasks/:subtaskindex/metrics |                                         |
|    GET | /jobs/:jobid/vertices/:vertexid/subtasktimes                 |                                         |
|    GET | /jobs/:jobid/vertices/:vertexid/taskmanagers                 |                                         |
|    GET | /jobs/:jobid/vertices/:vertexid/watermarks                   |                                         |



## DataSets

| Method | URI                                                          | Desc                                    |
| -----: | ------------------------------------------------------------ | --------------------------------------- |
|    GET | /datasets                                                    |                                         |
|    GET | /datasets/delete/:triggerid                                  |                                         |
| DELETE | /datasets/:datasetid                                         |                                         |



## Jars

| Method | URI               | Desc                         |
| -----: | ----------------- | ---------------------------- |
|    GET | /jars             | 查看上传的 jar 包            |
|   POST | /jars/upload      | 上传 jar 包                  |
| DELETE | /jars/:jarid      | 删除 jar 包                  |
|    GET | /jars/:jarid/plan | 查看 jar 包 的 dataflow 计划 |
|   POST | /jars/:jarid/run  | 运行 jar 包                  |






## Read More

- [Monitoring REST API](https://ci.apache.org/projects/flink/flink-docs-stable/monitoring/rest_api.html)