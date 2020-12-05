package xyz.kail.demo.flink.tools.udf.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class JsonToMapTableFunc extends JsonToLineTableFuncAbstract<Map<String, String>> {

    @Override
    public Map<String, String> toRow(JSONObject jsonObject) {
        Map<String, String> map = new LinkedHashMap<>();

        Set<Map.Entry<String, Object>> entries = jsonObject.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            Object value = entry.getValue();
            if (value instanceof JSON) {
                map.put(entry.getKey(), JSON.toJSONString(value));
            } else {
                map.put(entry.getKey(), value.toString());
            }
        }
        return map;
    }

    @Override
    protected Map<String, String> toRow(Object data) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("f0", data.toString());
        return map;
    }
}
