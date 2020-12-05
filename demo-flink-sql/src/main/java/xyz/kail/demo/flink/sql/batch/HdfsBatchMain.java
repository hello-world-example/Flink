package xyz.kail.demo.flink.sql.batch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.functions.GroupReduceFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.util.Collector;
import xyz.kail.demo.flink.tools.SimpleLog;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class HdfsBatchMain {

    static {
        SimpleLog.init();
    }

    public static void main(String[] args) throws Exception {
        ExecutionEnvironment bEnv = ExecutionEnvironment.getExecutionEnvironment();

        String hdfsPath = "hdfs://s01.hadoop.ttp.wx:8020/hdfs/apm/develop/2020-05-12-122526116";
        DataSet<String> dataSet = bEnv.readTextFile(hdfsPath);

        dataSet.map(new MapFunction<String, Tuple1<String>>() {
            @Override
            public Tuple1<String> map(String value) throws Exception {
                JSONObject jsonObject = JSON.parseObject(value);
                return new Tuple1<>(jsonObject.getString("appid"));
            }
        })
                .groupBy(0)
                .reduceGroup(new GroupReduceFunction<Tuple1<String>, Map<String, Long>>() {
                    @Override
                    public void reduce(Iterable<Tuple1<String>> values, Collector<Map<String, Long>> out) throws Exception {
                        Map<String, Long> map = new HashMap<>();

                        Iterator<Tuple1<String>> iterator = values.iterator();
                        for (; iterator.hasNext(); ) {
                            Tuple1<String> appId = iterator.next();
                            map.computeIfPresent(appId.f0, new BiFunction<String, Long, Long>() {
                                @Override
                                public Long apply(String s, Long aLong) {
                                    return aLong + 1;
                                }
                            });
                            map.computeIfAbsent(appId.f0, new Function<String, Long>() {
                                @Override
                                public Long apply(String s) {
                                    return 1L;
                                }
                            });
                        }

                        out.collect(map);
                    }
                })
                .print();

    }

}
