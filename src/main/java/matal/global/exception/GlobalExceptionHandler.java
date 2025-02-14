package matal.global.exception;

import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleStoreNotFoundException(NotFoundException exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                exception.getResponseCode().getMessage(),
                exception.getMessage(),
                request.getDescription(false)
        );
        return ResponseEntity.status(exception.getResponseCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                exception.getResponseCode().getMessage(),
                exception.getMessage(),
                request.getDescription(false)
        );
        return ResponseEntity.status(exception.getResponseCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                exception.getResponseCode().getMessage(),
                exception.getMessage(),
                request.getDescription(false)
        );
        return ResponseEntity.status(exception.getResponseCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExistException(AlreadyExistException exception,
                                                                     WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                exception.getResponseCode().getMessage(),
                exception.getMessage(),
                request.getDescription(false)
        );
        return ResponseEntity.status(exception.getResponseCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                exception.getStatusCode().toString(),
                exception.getMessage(),
                request.getDescription(false)
        );
        return ResponseEntity.status(exception.getStatusCode()).body(errorResponse);
    }
}
