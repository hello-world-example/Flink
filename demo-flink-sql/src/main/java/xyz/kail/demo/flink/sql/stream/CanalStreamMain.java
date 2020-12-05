package xyz.kail.demo.flink.sql.stream;

import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import xyz.kail.demo.flink.tools.udf.json.ToJsonStringScalarFunc;
import xyz.kail.demo.flink.tools.SimpleLog;

public class CanalStreamMain {

    static {
        SimpleLog.init();
    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        sEnv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        StreamTableEnvironment tEnv = StreamTableEnvironment.create(sEnv);


//        ExecutionEnvironment bEnv = ExecutionEnvironment.getExecutionEnvironment();
//        BatchTableEnvironment btEnv = BatchTableEnvironment.create(bEnv);


        tEnv.executeSql("CREATE TABLE canal_all_tables (\n" +
                "    `id`          BIGINT, \n" +
                "    `es`          BIGINT, \n" +
                "    `ts`          BIGINT, \n" +
                "    `table`       STRING, \n" +
                "    `database`    STRING, \n" +
                "    `type`        STRING, \n" +
                "    `isDdl`       BOOLEAN, \n" +
                "    `sql`         STRING, \n" +
                "    `data`        ARRAY<MAP<STRING,STRING>> \n" +
                ") WITH (\n" +
                "  'connector.type' = 'kafka',       \n" +
                "  'connector.version' = 'universal',\n" +
                "  'connector.properties.bootstrap.servers' = '172.16.2.163:9092',\n" +
                "\n" +
                "  'connector.topic' = 'topic-cancal-kail-219',\n" +
                "  'connector.properties.group.id' = 'topic-cancal-kail-219-group-local',\n" +
//                "  'connector.startup-mode' = 'earliest-offset',\n" +
                "  'connector.startup-mode' = 'group-offsets',\n" +
                "  \n" +
                "  'format.type'='json',\n" +
                "  'update-mode' = 'append'\n" +
                ")");


        tEnv.executeSql("create table sink_print ( \n" +
                        "    `canal_id`    BIGINT, \n" +
                        "    `es`          BIGINT, \n" +
                        "    `ts`          BIGINT, \n" +
                        "    `database_name`    STRING, \n" +
                        "    `table_name`       STRING, \n" +
                        "    `sql_type`        STRING, \n" +
                        "    `is_ddl`       BOOLEAN, \n" +
                        "    `sql`       STRING, \n" +
                        "    `data`        STRING \n" +
//                        " ,PRIMARY KEY (canal_id,es,database_name,table_name) NOT ENFORCED " +
                        ") with (" +
                        //
//                        " 'connector' = 'print' " +
                        //
//                " 'connector' = 'filesystem', " +
//                " 'path' = 'file:///Users/kail/IdeaProjects/github/hello-world-example/Flink/demo-flink-sql/target/', " +
//                " 'format' = 'json'" +
                        //
//                        " 'connector' = 'jdbc',\n" +
//                        " 'url' = 'jdbc:mysql://172.16.2.160:3306/flink_test',\n" +
//                        " 'table-name' = 'canal_data', \n" +
//                        " 'username' = 'admin', \n" +
//                        " 'password' = 'admin@123123' \n" +
                        //
//                        " 'connector' = 'jdbc',\n" +
//                        " 'url' = 'jdbc:postgresql://172.16.2.148:5432/kailbin',\n" +
//                        " 'table-name' = 'canal.canal_data', \n" +
//                        " 'username' = 'postgres', \n" +
//                        " 'password' = 'abc123' \n" +
                        //
                        " 'connector' = 'jdbc',\n" +
                        " 'url' = 'jdbc:postgresql://10.10.4.81:5432/ttpai_group_architecture',\n" +
                        " 'table-name' = 'clickdata.canal_data', \n" +
                        " 'username' = 'gpadmin', \n" +
                        " 'password' = 'gpadmin' \n" +
                        ")"
        );


//        Table table = tEnv.sqlQuery("select \n" +
//                        "    `table`, \n" +
//                        "    `database`, \n" +
//                        "    `es`, \n" +
//                        "    `ts`, \n" +
//                        "    `type`, \n" +
//                        "    `isDdl`, \n" +
////                "    p_data['id'] as id,  \n" +
//                        "    p_data \n" +
//                        "from canal_all_tables \n" +
//                        "cross join unnest(data) AS t (p_data) \n" +
//                        "where \n" +
//                        "    canal_all_tables.`database` = 'test' \n" +
//                        "AND canal_all_tables.`table` = 't_ttp_user' "
//        );


//        tEnv.toRetractStream(table, Row.class).print("#####");

        tEnv.createTemporarySystemFunction("toJsonString", new ToJsonStringScalarFunc());

        tEnv.executeSql("" +
                " insert into sink_print " +
                " SELECT " +
                "    `id`, \n" +
                "    `es`, \n" +
                "    `ts`, \n" +
                "    `table`, \n" +
                "    `database`, \n" +
                "    `type`, \n" +
                "    `isDdl`, \n" +
                "    `sql`, \n" +
                "    toJsonString(p_data) \n" +
                " from canal_all_tables " +
                " cross join unnest(canal_all_tables.data) AS t1 (p_data)" +
                " WHERE canal_all_tables.data is not null" +
                "");

//        sEnv.execute();

    }

}
