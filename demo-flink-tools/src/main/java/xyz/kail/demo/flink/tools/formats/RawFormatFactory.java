package xyz.kail.demo.flink.tools.formats;

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.table.factories.Factory;

import java.util.Set;

public class RawFormatFactory implements Factory {

    @Override
    public String factoryIdentifier() {
        return null;
    }

    @Override
    public Set<ConfigOption<?>> requiredOptions() {
        return null;
    }

    @Override
    public Set<ConfigOption<?>> optionalOptions() {
        return null;
    }
}
