package xyz.kail.demo.techshare.hello;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import xyz.kail.demo.techshare.hello.vo.CountDataVO;

import java.util.Objects;

/**
 * 来初步理解窗口
 */
public class HelloCountMain {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.createLocalEnvironment(1);

        // nc -l 19999
        DataStreamSource<String> source = sEnv.socketTextStream("127.0.0.1", 19999);

        source.map(CountDataVO::parse)
                .returns(new TypeHint<CountDataVO>() {})
                .filter((FilterFunction<CountDataVO>) Objects::nonNull)
                .keyBy((KeySelector<CountDataVO, String>) value -> value.getWord())
                // count 滚动窗口
//                .countWindow(5)
//                .countWindowAll(5)
                // count 滑动窗口（每遇到两条相同的 Key，计算最近5条数据的和）
                .countWindow(5, 2)
                // count 滑动窗口（针对所有 Key 进行滑动）
//                .countWindowAll(5, 2)
                // 触发器
//                .trigger(new NumTrigger(8L))
                .reduce(new ReduceFunction<CountDataVO>() {
                    @Override
                    public CountDataVO reduce(CountDataVO value1, CountDataVO value2) throws Exception {
                        return new CountDataVO(value1.getWord(), value1.getSum() + value1.getWeight() + value2.getWeight());
                    }
                })

                .print()
        ;

        // 执行
        sEnv.execute("Word Count");

    }

}
