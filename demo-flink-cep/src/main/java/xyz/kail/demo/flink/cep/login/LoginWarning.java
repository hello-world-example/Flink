package xyz.kail.demo.flink.cep.login;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LoginWarning {

    private String userId;

    private String type;

    private String ip;

    public LoginWarning(String userId, String type, String ip) {
        this.userId = userId;
        this.type = type;
        this.ip = ip;
    }
}