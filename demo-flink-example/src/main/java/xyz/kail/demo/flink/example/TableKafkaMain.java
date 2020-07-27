package xyz.kail.demo.flink.example;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;

public class TableKafkaMain {

    public static void main(String[] args) {

        EnvironmentSettings environmentSettings = EnvironmentSettings.newInstance()
                .build();

        TableEnvironment tableEnvironment = TableEnvironment.create(environmentSettings);
    }

}
