package xyz.kail.demo.flink.tools;

import org.slf4j.impl.SimpleLogger;

public class SimpleLog {

    static {
        init();
    }

    public static void init() {
        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");
        System.setProperty(SimpleLogger.LOG_FILE_KEY, "System.out");
    }
}
