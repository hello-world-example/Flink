package xyz.kail.demo.flink.concept.environment;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.BatchTableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class EnvMain {

    public static void main(String[] args) {
        // DataSet
        ExecutionEnvironment bEnv = ExecutionEnvironment.getExecutionEnvironment();
        // DataSet Table
        BatchTableEnvironment btEnv = BatchTableEnvironment.create(bEnv);

        // DataStream
        StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        // DataStream Table
        StreamTableEnvironment stEnv = StreamTableEnvironment.create(sEnv);
    }

}
