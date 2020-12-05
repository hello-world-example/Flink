package xyz.kail.demo.techshare.hello.trigger;

import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.Window;
import xyz.kail.demo.techshare.hello.vo.CountDataVO;

public class NumTrigger extends Trigger<CountDataVO, Window> {

    private Long num;

    public NumTrigger(Long num) {
        this.num = num;
    }

    @Override
    public TriggerResult onElement(CountDataVO data, long timestamp, Window window, TriggerContext ctx) throws Exception {
        if (Long.parseLong(data.getNum()) >= num) {
            return TriggerResult.FIRE;
        }
        return TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onProcessingTime(long time, Window window, TriggerContext ctx) throws Exception {
        return TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onEventTime(long time, Window window, TriggerContext ctx) throws Exception {
        return TriggerResult.CONTINUE;
    }

    @Override
    public void clear(Window window, TriggerContext ctx) throws Exception {

    }

}
