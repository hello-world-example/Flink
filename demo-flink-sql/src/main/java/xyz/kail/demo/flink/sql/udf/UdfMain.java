package xyz.kail.demo.flink.sql.udf;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.BatchTableEnvironment;
import xyz.kail.demo.flink.tools.SimpleLog;
import xyz.kail.demo.flink.tools.udf.SuffixScalarFunction;

import static org.apache.flink.table.api.Expressions.$;

public class UdfMain {

    static {
        SimpleLog.init();
    }

    public static void main(String[] args) {
        ExecutionEnvironment bEnv = ExecutionEnvironment.getExecutionEnvironment();

        BatchTableEnvironment btEnv = BatchTableEnvironment.create(bEnv);

        btEnv.createTemporarySystemFunction("Suffix", new SuffixScalarFunction());

        DataSet<Integer> dataSet = bEnv.fromElements(1, 2, 3);
        Table table = btEnv.fromDataSet(dataSet, $("num"));

        btEnv.executeSql("select num from " + table).print();

        btEnv.executeSql("select Suffix( CAST(num AS VARCHAR) ) from " + table).print();

        btEnv.executeSql("select Suffix( CAST(num AS VARCHAR), '-hello' ) from " + table).print();


    }

}
