package xyz.kail.demo.flink.cep.login;

import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LoginCepMain {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<LoginEvent> dataStream = sEnv.fromCollection(Arrays.asList(
                new LoginEvent("小明", "192.168.0.1", "fail"),
                new LoginEvent("小明", "192.168.0.2", "fail"),
                new LoginEvent("小王", "192.168.10,11", "fail"),
                new LoginEvent("小王", "192.168.10,12", "fail"),
                new LoginEvent("小明", "192.168.0.3", "fail"),
                new LoginEvent("小明", "192.168.0.4", "fail"),
                new LoginEvent("小王", "192.168.10,10", "success")
        ));


        Pattern<LoginEvent, LoginEvent> loginFailPattern = Pattern
                .<LoginEvent>begin("begin").where(new SimpleCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent event) throws Exception {
                        return event.getType().equals("fail");
                    }
                })
                .next("next").where(new SimpleCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent loginEvent) throws Exception {
                        return loginEvent.getType().equals("fail");
                    }
                })
                .within(Time.seconds(1));


        PatternStream<LoginEvent> patternStream = CEP.pattern(
                // input
                dataStream.keyBy(LoginEvent::getUserId),
                // pattern
                loginFailPattern
                //
        );


        final DataStream<LoginWarning> warningDataStream = patternStream.select(new PatternSelectFunction<LoginEvent, LoginWarning>() {
            @Override
            public LoginWarning select(Map<String, List<LoginEvent>> pattern) throws Exception {
                List<LoginEvent> first = pattern.get("begin");
                List<LoginEvent> second = pattern.get("next");
                return new LoginWarning(second.get(0).getUserId(), second.get(0).getType(), second.get(0).getIp());
            }
        });

        warningDataStream.print();

        sEnv.execute();

    }

}
