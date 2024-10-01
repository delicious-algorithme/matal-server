package matal.global.exception;

public class BadRequestException extends BaseException {

    public BadRequestException(ResponseCode responseCode) {
        super(responseCode);
    }
}
