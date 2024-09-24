package matal.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseCode {

    /*
    * store
     */
    NOT_FOUND_STORE_ID(HttpStatus.NOT_FOUND, "요청한 ID의 가게 정보가 존재하지 않습니다.");


    private HttpStatus status;
    private String message;

    ResponseCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
