package matal.global.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,
        String message,
        String error,
        String path
) {
}
