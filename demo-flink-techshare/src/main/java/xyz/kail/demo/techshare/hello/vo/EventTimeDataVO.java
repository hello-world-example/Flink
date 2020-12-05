package xyz.kail.demo.techshare.hello.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;

/*
2020-12-03 16:00:00,kkk
2020-12-03 16:00:01,bbb
2020-12-03 16:00:05,111
2020-12-03 16:00:05,222
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class EventTimeDataVO {

    private static Logger log = LoggerFactory.getLogger(EventTimeDataVO.class);

    private Long eventTime;

    private String word;

    private Long count = 1L;


    public static EventTimeDataVO parse(String value) {
        String[] lines = value.split(",");
        if (lines.length < 2) {
            return null;
        }
        try {
            return new EventTimeDataVO(DateUtils.parseDate(lines[0], "yyyy-MM-dd HH:mm:ss").getTime(), lines[1]);
        } catch (ParseException e) {
            log.error("", e);
        }
        return null;
    }

    public EventTimeDataVO(Long eventTime, String word) {
        this.eventTime = eventTime;
        this.word = word;
    }

    public EventTimeDataVO(String word, Long count) {
        this.word = word;
        this.count = count;
    }
}