package xyz.kail.demo.flink.tools.udf;

import org.apache.flink.table.functions.ScalarFunction;

/**
 * 为数据增加后缀
 * <p>
 * https://ci.apache.org/projects/flink/flink-docs-release-1.11/dev/table/functions/udfs.html#evaluation-methods
 */
public class SuffixScalarFunction extends ScalarFunction {

    public String eval(String value) {
        return eval(value, "-kail");
    }

    public String eval(String value, String suffix) {
        return value + suffix;
    }

}
