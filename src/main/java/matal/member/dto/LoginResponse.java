package matal.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {
    private String message;
    private String accessToken;
    public LoginResponse(String message, String accessToken) {
        this.message = message;
        this.accessToken = accessToken;
    }
}
