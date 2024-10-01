package matal.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseCode {

    /*
    * store
     */
    STORE_BAD_REQUEST(HttpStatus.BAD_REQUEST, "요청 파라미터 정보가 올바르지 않습니다."),
    STORE_NOT_FOUND_ID(HttpStatus.NOT_FOUND, "요청한 ID의 가게 정보가 존재하지 않습니다."),
    STORE_PAGE_INVALID(HttpStatus.BAD_REQUEST, "page는 음수가 될 수 없습니다.");


    private HttpStatus status;
    private String message;

    ResponseCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
