package xyz.kail.demo.flink.cep.login;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LoginEvent {

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 登录IP
     */
    private String ip;
    /**
     * 登录类型
     */
    private String type;

    public LoginEvent(String userId, String ip, String type) {
        this.userId = userId;
        this.ip = ip;
        this.type = type;
    }
}