//package xyz.kail.demo.flink.table;
//
//import org.apache.flink.api.common.typeinfo.Types;
//import org.apache.flink.api.java.DataSet;
//import org.apache.flink.api.java.ExecutionEnvironment;
//import org.apache.flink.api.java.operators.DataSource;
//import org.apache.flink.table.api.Table;
//import org.apache.flink.table.api.bridge.java.BatchTableEnvironment;
//import org.apache.flink.table.descriptors.FileSystem;
//import org.apache.flink.table.descriptors.OldCsv;
//import org.apache.flink.table.descriptors.Schema;
//import org.apache.flink.table.expressions.Expression;
//import org.apache.flink.types.Row;
//import xyz.kail.demo.flink.tools.SimpleLog;
//
//import static org.apache.flink.table.api.Expressions.$;
//
//public class JavaBatchWordCount {
//
//    static {
//        SimpleLog.init();
//    }
//
//    public static void main(String[] args) throws Exception {
//        // 拿到执行环境
//        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
//        // 拿到 Table 环境
//        BatchTableEnvironment tEnv = BatchTableEnvironment.create(env);
//
//        String path = JavaBatchWordCount.class.getClassLoader().getResource("words.txt").getPath();
//
//        DataSet<String> dataSet = env.readTextFile(path);
//
//        Table table = tEnv.fromDataSet(dataSet);
//        table.addColumns(
//                $("a").plus(1).as("a1"),
//        )
//        // 数据类型
//                .withFormat(new OldCsv().field("word", Types.STRING).lineDelimiter("\n"))
//                // 字段格式
//                .withSchema(new Schema().field("word", Types.STRING))
//
//
//        tEnv.connect(new FileSystem().path(path))
//
//                // 注册为 TableSource
//                .registerTableSource("fileSource"); // line:20
//
//        // 扫描 Table
//        Table result = tEnv.from("fileSource")
//                // 按照字段 word 分组
//                .groupBy("word")
//                // 查询
//                .select("word, count(1) as count");
//
//        // 按照 DataSet 的方式输出
//        tEnv.toDataSet(result, Row.class).print();
//    }
//
//}
