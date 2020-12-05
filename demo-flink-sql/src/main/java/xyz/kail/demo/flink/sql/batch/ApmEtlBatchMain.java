package xyz.kail.demo.flink.sql.batch;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import xyz.kail.demo.flink.tools.SimpleLog;
import xyz.kail.demo.flink.tools.udf.json.JsonToMapTableFunc;
import xyz.kail.demo.flink.tools.udf.json.JsonToRowTableFunc;

import java.util.concurrent.TimeUnit;

import static org.apache.flink.table.api.Expressions.row;

public class ApmEtlBatchMain {

    static {
        SimpleLog.init();
        ExecutionEnvironment.setDefaultLocalParallelism(1);
    }

    public static void main(String[] args) throws Exception {

        EnvironmentSettings environmentSettings = EnvironmentSettings.newInstance()
                .inBatchMode()
                .useBlinkPlanner().build();

        TableEnvironment btEnv = TableEnvironment.create(environmentSettings);

//        btEnv.executeSql("" +
//                "CREATE TABLE apm (\n" +
//                "  line STRING\n" +
//                ") WITH (" +
//                "  'connector' = 'filesystem', " +
//                "   'path' = 'hdfs://s01.hadoop.ttp.wx:8020/hdfs/apm/develop/2020-05-12-122526116'," +
//                "   'format' = 'csv'," +
//                "   'csv.field-delimiter' = '~' " +
//                ")");

        Table apm = btEnv.fromValues(
                row("{'appid':1, 'content':{'a':1}}"),
                row("[{'appid':1, 'content':{'a':2}},{'appid':3, 'content':{'a':3}}]")
        ).as("line");


        // 注册函数
        btEnv.createTemporaryFunction("jsonToMap", new JsonToMapTableFunc());

        //
        Table table = btEnv.sqlQuery("" +
                " SELECT " +
                "    data['appid'] AS appid, " +
                "    data['content'] AS content " +
                " FROM " + apm + " LEFT JOIN LATERAL TABLE(jsonToMap(line)) AS t(data) ON TRUE");


        table = btEnv.sqlQuery("" +
                " SELECT " +
                "    appid, " +
                "    COUNT(*) AS cnt" +
                " FROM " + table + " LEFT JOIN LATERAL TABLE(jsonToMap(content)) AS t(c) ON TRUE" +
                " GROUP BY appid" +
                " ORDER BY appid");

        table.execute().print();

    }

}
