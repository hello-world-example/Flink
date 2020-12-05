package xyz.kail.demo.flink.concept.source;

import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import xyz.kail.demo.flink.tools.SimpleLog;

import java.util.HashMap;
import java.util.Map;

public class DataSetSourceMain {

    static {
        SimpleLog.init();
    }

    public static void main(String[] args) throws Exception {
        ExecutionEnvironment bEnv = ExecutionEnvironment.getExecutionEnvironment();

        /**
         * CSV
         */
        String csvPath = DataSetSourceMain.class.getResource("/demo.csv").getPath();
        bEnv.readCsvFile(csvPath)
                .ignoreFirstLine()
                .types(String.class, Integer.class)
                .map(value -> Tuple1.of(value.f1))
                .returns(new TypeHint<Tuple1<Integer>>() {})
                .sum(0).print();




        /**
         * 生成序列
         */
        DataSet<Tuple1<Long>> dataSet2 = bEnv.generateSequence(1, 100)
                .map(Tuple1::new)
                .returns(new TypeHint<Tuple1<Long>>() {});

        dataSet2.sum(0).print();

    }
}
