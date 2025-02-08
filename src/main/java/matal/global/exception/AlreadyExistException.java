package matal.global.exception;

public class AlreadyExistException extends BaseException {

    public AlreadyExistException(ResponseCode responseCode) {
        super(responseCode);
    }
}
