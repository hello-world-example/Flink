# TODO



1.11.0

Exception in thread "main" java.lang.IllegalStateException: No ExecutorFactory found to execute the application.
	at org.apache.flink.core.execution.DefaultExecutorServiceLoader.getExecutorFactory(DefaultExecutorServiceLoader.java:84)
	at org.apache.flink.api.java.ExecutionEnvironment.executeAsync(ExecutionEnvironment.java:964)
	at org.apache.flink.api.java.ExecutionEnvironment.execute(ExecutionEnvironment.java:889)
	at org.apache.flink.api.java.ExecutionEnvironment.execute(ExecutionEnvironment.java:873)
	at org.apache.flink.api.java.DataSet.collect(DataSet.java:413)
	at org.apache.flink.api.java.DataSet.print(DataSet.java:1652)
	at xyz.kail.demo.flink.example.DataSetWordCount.main(DataSetWordCount.java:30)