package matal.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseCode {

    /*
    * bookmark
     */
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "북마크 정보를 찾을 수 없습니다."),
    BOOKMARK_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 북마크 입니다."),

    /*
    * member & session
     */
    SESSION_AUTH_EXCEPTION(HttpStatus.UNAUTHORIZED, "사용자 세션이 만료되었거나 유효하지 않습니다. 다시 로그인해 주세요."),
    SESSION_VALUE_EXCEPTION(HttpStatus.UNAUTHORIZED, "세션이 존재하지 않습니다."),

    MEMBER_NOT_FOUND_ID(HttpStatus.NOT_FOUND, "회원 정보가 존재하지 않습니다."),
    MEMBER_NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND, "회원 정보가 존재하지 않습니다."),
    MEMBER_BAD_REQUEST(HttpStatus.BAD_REQUEST, "회원가입 정보를 올바르게 입력해주세요."),
    MEMBER_AUTH_EXCEPTION(HttpStatus.UNAUTHORIZED, "회원정보가 일치하지 않습니다."),
    MEMBER_ALREADY_EXIST_EXCEPTION(HttpStatus.CONFLICT, "이미 존재하는 이메일 정보 입니다."),

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
