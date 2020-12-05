package xyz.kail.demo.flink.concept.source;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

public class DataStreamSourceMain {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.getExecutionEnvironment();

//        DataStream<String> dataStream = sEnv.socketTextStream("", 123);

        String csvPath = DataSetSourceMain.class.getResource("/demo.csv").getPath();
        System.out.println(csvPath);
        sEnv.readTextFile("file://" + csvPath)
                .addSink(new SinkFunction<String>() {
                    @Override
                    public void invoke(String value, Context context) throws Exception {
                        System.out.println(value);
                    }
                });

        sEnv.execute();

    }

}
