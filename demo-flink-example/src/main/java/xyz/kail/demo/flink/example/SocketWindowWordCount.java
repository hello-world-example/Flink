package xyz.kail.demo.flink.example;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * Implements a streaming windowed version of the "WordCount" program.
 *
 * <p>This program connects to a server socket and reads strings from the socket.
 * The easiest way to try this out is to open a text server (at port 12345)
 * using the <i>netcat</i> tool via
 * <pre>
 * nc -l 12345
 * </pre>
 * and run this example with the hostname and the port as arguments.
 *
 * @author Flink
 */
public class SocketWindowWordCount {

    /**
     * nc -l 12345
     */
    public static void main(String[] args) throws Exception {
        if (args.length <= 0) {
            args = new String[]{"--hostname", "10.10.16.160"};
        }

        // 要连接的主机和端口
        final String hostname;
        final int port;

        try {
            final ParameterTool params = ParameterTool.fromArgs(args);
            hostname = params.get("hostname", "localhost");
            port = params.getInt("port", 12345);
        } catch (Exception e) {
            System.err.println("No port specified. Please run 'SocketWindowWordCount " +
                    "--hostname <hostname> --port <port>', where hostname (localhost by default) " +
                    "and port is the address of the text server");

            System.err.println("To start a simple text server, run 'netcat -l <port>' and " +
                    "type the input text into the command line");
            return;
        }

        // 第一步是创建一个 StreamExecutionEnvironment。这是一个入口类，可以用来设置参数和创建数据源以及提交任务
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 创建一个从本地端口号 socket 中读取数据的数据源
        // DataStream 是 Flink 中做流处理的核心 API，上面定义了非常多常见的操作（如，过滤、转换、聚合、窗口、关联等）
        DataStream<String> text = env.socketTextStream(hostname, port, "\n");

        // 定义所有的算子操作
        DataStream<WordWithCount> windowCounts = text
                // 实现了一个 flatmap 来做解析的工作，因为一行数据中可能有多个单词
                .flatMap(new FlatMapFunction<String, WordWithCount>() {
                    @Override
                    public void flatMap(String value, Collector<WordWithCount> out) {
                        for (String word : value.split("\\s")) {
                            out.collect(new WordWithCount(word, 1L));
                        }
                    }
                })
                // group by 切出来的单词，key 是 WordWithCount 类的字段名
                .keyBy((KeySelector<WordWithCount, String>) value -> value.word)
                // 时间窗口是 5s，一次处理 5s 内的数据
                .timeWindow(Time.seconds(5))
                // 相同的 key 出现多次，如何处理
                .reduce(new ReduceFunction<WordWithCount>() {
                    @Override
                    public WordWithCount reduce(WordWithCount a, WordWithCount b) {
                        return new WordWithCount(a.word, a.count + b.count);
                    }
                });

        // 打印结果到控制台，单线程处理，避免打印乱序
        // windowCounts.print().setParallelism(1);
        windowCounts.printToErr().setParallelism(1).name("xxxxx");

        // 真正开始执行
        env.execute("Socket Window WordCount");
    }

    // ------------------------------------------------------------------------

    /**
     * Data type for words with count.
     */
    public static class WordWithCount {

        public String word;
        public long count;

        public WordWithCount() {
        }

        public WordWithCount(String word, long count) {
            this.word = word;
            this.count = count;
        }

        @Override
        public String toString() {
            return word + " : " + count;
        }
    }
}