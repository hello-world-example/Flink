package xyz.kail.demo.flink.tools.udf.json;

import com.alibaba.fastjson.JSON;
import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;

/**
 * 为 Json String 类型
 */
public class ToJsonStringScalarFunc extends ScalarFunction {

    /**
     * 参数支持所有类型
     */
    public String eval(@DataTypeHint(value = "RAW") Object value) {
        return "json:" + JSON.toJSONString(value);
    }

}
