package matal.global.exception;

public class AuthException extends BaseException{

    public AuthException(ResponseCode responseCode) {
        super(responseCode);
    }
}
