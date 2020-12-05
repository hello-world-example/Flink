package xyz.kail.demo.techshare.hello;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.runtime.operators.util.AssignerWithPeriodicWatermarksAdapter;
import xyz.kail.demo.techshare.hello.vo.EventTimeDataVO;

import java.util.Objects;

/**
 * 来理解事件窗口
 */
public class HelloEventTimeMain {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.createLocalEnvironment(1);
        sEnv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        // nc -l 19999
        DataStreamSource<String> source = sEnv.socketTextStream("127.0.0.1", 19999);

        source.map(EventTimeDataVO::parse)
                .returns(new TypeHint<EventTimeDataVO>() {})
                //.assignTimestampsAndWatermarks()
                .filter(Objects::nonNull)
                .keyBy(EventTimeDataVO::getWord)
                .timeWindowAll(Time.seconds(5L))
                .reduce(new ReduceFunction<EventTimeDataVO>() {
                    @Override
                    public EventTimeDataVO reduce(EventTimeDataVO value1, EventTimeDataVO value2) throws Exception {
                        return new EventTimeDataVO(value1.getWord(), value1.getCount() + value2.getCount());
                    }
                })

                // 结果输出到控制台
                .print();

        // 执行
        sEnv.execute("Word Count Time Window");

    }

}
