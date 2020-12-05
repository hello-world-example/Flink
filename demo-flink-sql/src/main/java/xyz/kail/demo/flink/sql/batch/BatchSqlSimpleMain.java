package xyz.kail.demo.flink.sql.batch;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.StatementSet;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.BatchTableEnvironment;
import xyz.kail.demo.flink.tools.SimpleLog;
import xyz.kail.demo.flink.tools.udf.json.JsonToMapTableFunc;
import xyz.kail.demo.flink.tools.udf.json.ToJsonStringScalarFunc;

import static org.apache.flink.table.api.Expressions.$;

public class BatchSqlSimpleMain {

    static {
        SimpleLog.init();
    }

    public static void main(String[] args) {
        ExecutionEnvironment bEnv = ExecutionEnvironment.getExecutionEnvironment();

        EnvironmentSettings environmentSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inBatchMode().build();

        BatchTableEnvironment btEnv = BatchTableEnvironment.create(bEnv);

        DataSource<Integer> dataSource = bEnv.fromElements(1, 2, 3);

        /*
         * 表
         */

        Table table = btEnv.fromDataSet(dataSource, $("col1"));

        //
        btEnv.executeSql("SELECT sum(col1) as sumCol FROM " + table).print();

        //
        btEnv.executeSql("SELECT sum(col1) as sumCol FROM " + table).collect()
                .forEachRemaining(row -> System.out.println(row.toString()));

        // LIMIT 必须带有 ORDER BY
        btEnv.executeSql("SELECT * FROM " + table + " ORDER BY col1 LIMIT 1").print();
        btEnv.executeSql("SELECT * FROM " + table + " ORDER BY col1 DESC LIMIT 1").print();

        /*
         * 视图
         */
        btEnv.createTemporaryView("v_temp", dataSource, $("col1"));

        btEnv.executeSql("SELECT sum(col1) + 100 as sum_col FROM v_temp").print();

        TableEnvironment tEnv = TableEnvironment.create(environmentSettings);
        tEnv.createTemporaryFunction("JsonToMap", JsonToMapTableFunc.class);
        tEnv.createTemporaryFunction("ToJsonString", ToJsonStringScalarFunc.class);

        btEnv.executeSql("SELECT ToJsonString(JsonToMap('{asd:123}'))").print();



    }

}
