package xyz.kail.demo.flink.example;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.apache.flink.streaming.connectors.elasticsearch5.ElasticsearchSink;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema;
import org.apache.flink.util.Collector;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.*;

public class KafkaSinkElasticMain {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();

        /*
         * Kafka 配置
         */
        String topic = "topic-clickdata-event-batch-test";
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "s01.pub-kafka.ttp.wx:9092,s02.pub-kafka.ttp.wx:9092,s03.pub-kafka.ttp.wx:9092");
        properties.setProperty("group.id", "group-flink-topic-clickdata-event-batch-by-kail");
        KafkaDeserializationSchema<ObjectNode> deserializationSchema = new JSONKeyValueDeserializationSchema(true);
        FlinkKafkaConsumer<ObjectNode> kafkaConsumer = new FlinkKafkaConsumer<>(topic, deserializationSchema, properties);
        // 从头开始消费（每次都从头开始）
        // kafkaConsumer.setStartFromEarliest();
        // 消费者组当前的位置开始
        kafkaConsumer.setStartFromGroupOffsets();

        /*
         * DataStream
         */
        DataStreamSource<ObjectNode> dataStream = environment.addSource(kafkaConsumer);

        /*
         * Batch > Single
         */
        SingleOutputStreamOperator<JsonNode> operator = dataStream.flatMap(new FlatMapFunction<ObjectNode, JsonNode>() {
            @Override
            public void flatMap(ObjectNode kafkaData, Collector<JsonNode> out) throws Exception {
                JsonNode batchList = kafkaData.findValue("value");
                for (JsonNode jsonNode : batchList) {
                    out.collect(jsonNode);
                }
            }
        });

        /*
         * 输出 控制台
         */
        operator.print().setParallelism(1);

        /*
         * 输出 ElasticSearch
         */
        Map<String, String> userConfig = new HashMap<>();
        userConfig.put("cluster.name", "elasticsearch-offline");
        userConfig.put("bulk.flush.max.actions", "1");

        List<InetSocketAddress> addresses = new ArrayList<>();
        addresses.add(new InetSocketAddress(InetAddress.getByName("172.16.2.182"), 9300));

        ElasticsearchSinkFunction<JsonNode> sinkFunction = new ElasticsearchSinkFunction<JsonNode>() {
            public IndexRequest createIndexRequest(JsonNode element) {
                return Requests.indexRequest()
                        .index("kail-test-fink-sink")
                        .source(element.toString());
            }

            @Override
            public void process(JsonNode element, RuntimeContext ctx, RequestIndexer indexer) {
                indexer.add(createIndexRequest(element));
            }
        };

        ElasticsearchSink<JsonNode> elasticsearchSink = new ElasticsearchSink<>(userConfig, addresses, sinkFunction);
        operator.addSink(elasticsearchSink);

        /*
         * 执行
         */
        environment.execute("Kafka to print");

    }

}
