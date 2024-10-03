package matal.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {
    private final String message;
    
    private final String accessToken;
    
    public LoginResponse(String message, String accessToken) {
        this.message = message;
        this.accessToken = accessToken;
    }
}
