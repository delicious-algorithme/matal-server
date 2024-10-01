package matal.global.exception;

public class NotFoundException extends BaseException {

    public NotFoundException(ResponseCode responseCode) {
        super(responseCode);
    }
}
