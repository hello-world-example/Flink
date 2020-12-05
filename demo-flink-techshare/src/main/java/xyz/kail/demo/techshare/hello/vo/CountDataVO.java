package xyz.kail.demo.techshare.hello.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
1,w1,2
2,w2,1
3,w3,3

4,w2,2
5,w1,2

6,w2,5
7,w2,2
8,w2,3
9,w2,4
*/
@Getter
@Setter
@NoArgsConstructor
public class CountDataVO {

    private String num;

    private String word;

    private Integer weight = 0;

    private Long sum = 0L;

    public CountDataVO(String num, String word, Integer count) {
        this.num = num;
        this.word = word;
        this.weight = count;
    }

    public CountDataVO(String word, Long sum) {
        this.word = word;
        this.sum = sum;
    }

    public static CountDataVO parse(String value) {
        String[] lines = value.split(",");
        if (lines.length < 2) {
            return null;
        }
        return new CountDataVO(lines[0], lines[1], Integer.valueOf(lines[2]));
    }

    @Override
    public String toString() {
        return "DataVO{" +
                "eventTime='" + num + '\'' +
                ", word='" + word + '\'' +
                ", weight=" + weight +
                ", sum=" + sum +
                '}';
    }
}