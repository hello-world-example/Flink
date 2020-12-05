package xyz.kail.demo.flink.tools.udf.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.functions.util.ListCollector;
import org.apache.flink.table.functions.TableFunction;
import org.apache.flink.types.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ]
 */
public abstract class JsonToLineTableFuncAbstract<T> extends TableFunction<T> {

    public void eval(Character json) {
        eval(json.toString());
    }

    /**
     * 参数支持所有类型
     */
    public void eval(String json) {
        // 对象处理
        if (json.startsWith("{")) {
            this.collector(toRow(JSON.parseObject(json)));
            return;
        }

        // 数组处理
        if (json.startsWith("[")) {
            JSONArray jsonArray = JSON.parseArray(json);
            for (Object o : jsonArray) {
                if (o instanceof JSONObject) {
                    // 对象列表
                    this.collector(toRow((JSONObject) o));
                } else {
                    // 基本数据类型类表
                    this.collector(toRow(o));
                }
            }
        }
    }

    protected void collector(T line) {
        if (null == line) {
            return;
        }
        this.collect(line);
    }

    protected abstract T toRow(JSONObject jsonObject);

    protected abstract T toRow(Object data);

    public static void main(String[] args) {
        List<Row> listRow = new ArrayList<>();
        JsonToRowTableFunc jsonToRow = new JsonToRowTableFunc();
        jsonToRow.setCollector(new ListCollector<>(listRow));
        jsonToRow.eval("{'asd':1,'arr':[1,2,3,4]}");
        jsonToRow.eval("[{'asd':2,'json':{'a':1}}]");
        jsonToRow.eval("[{'asd':3},{'asd':4}]");
        jsonToRow.eval("[1,2,3,4]");
        System.out.println(listRow);

        List<Map<String, String>> listMap = new ArrayList<>();
        JsonToMapTableFunc jsonToMap = new JsonToMapTableFunc();
        jsonToMap.setCollector(new ListCollector<>(listMap));
        jsonToMap.eval("{'asd':1,'arr':[1,2,3,4]}");
        jsonToMap.eval("[{'asd':2,'json':{'a':1}}]");
        jsonToMap.eval("[{'asd':3},{'asd':4}]");
        jsonToMap.eval("[1,2,3,4]");
        System.out.println(listMap);
    }

}
