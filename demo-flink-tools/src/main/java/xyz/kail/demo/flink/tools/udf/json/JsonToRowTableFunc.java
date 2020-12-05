package xyz.kail.demo.flink.tools.udf.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.flink.types.Row;

public class JsonToRowTableFunc extends JsonToLineTableFuncAbstract<Row> {

    @Override
    public Row toRow(JSONObject jsonObject) {
        Row row = new Row(jsonObject.size());

        int i = 0;
        for (Object value : jsonObject.values()) {
            row.setField(i++, value);
        }
        return row;
    }

    @Override
    protected Row toRow(Object data) {
        return Row.of(JSON.toJSONString(data));
    }
}
