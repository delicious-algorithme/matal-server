package matal.global.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final ResponseCode responseCode;

    public BaseException(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }
}
