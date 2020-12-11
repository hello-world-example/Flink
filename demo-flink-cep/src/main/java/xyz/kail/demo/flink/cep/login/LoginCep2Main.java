package xyz.kail.demo.flink.cep.login;

import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LoginCep2Main {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<LoginEvent> dataStream = sEnv.fromCollection(Arrays.asList(
                new LoginEvent("小明", "192.168.0.1", "fail"),
                new LoginEvent("小明", "192.168.0.2", "fail"),

                new LoginEvent("小王", "192.168.10,11", "fail"),
                new LoginEvent("小王", "192.168.10,12", "fail"),

                new LoginEvent("小明", "192.168.0.3", "success"),
                new LoginEvent("小明", "192.168.0.4", "fail"),
                new LoginEvent("小明", "192.168.0.5", "fail"),
                new LoginEvent("小明", "192.168.0.6", "fail"),
                new LoginEvent("小明", "192.168.0.7", "fail"),
                new LoginEvent("小明", "192.168.0.8", "success"),

                new LoginEvent("小王", "192.168.10,13", "success")
        ));


        Pattern<LoginEvent, LoginEvent> loginFailPattern = Pattern
                .<LoginEvent>begin("begin")
                .where(new SimpleCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent value) throws Exception {
                        return value.getType().equals("fail");
                    }
                })
                .timesOrMore(3)
                .until(new SimpleCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent value) throws Exception {
                        return !value.getType().equals("fail");
                    }
                });


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
                List<LoginEvent> begin = pattern.get("begin");
                System.out.println("===============" + begin);

                final LoginEvent last = begin.get(begin.size() - 1);
                return new LoginWarning(last.getUserId(), last.getType(), last.getIp());
            }
        });

        warningDataStream.print();

        sEnv.execute();

    }

}
