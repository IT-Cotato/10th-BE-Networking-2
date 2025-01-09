package cotato.backend.common.exception;

import static cotato.backend.common.exception.ErrorCode.INVALID_INPUT;

import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cotato.backend.common.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleApiException(ApiException e) {
        log.warn("handleApiException", e);

        return makeErrorResponseEntity(e.getHttpStatus(), e.getMessage(), e.getCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return makeErrorResponseEntity(INVALID_INPUT.getHttpStatus(), errorMessage, INVALID_INPUT.getCode());
    }

    private ResponseEntity<Object> makeErrorResponseEntity(HttpStatus httpStatus, String message, String code) {
        return ResponseEntity
                .status(httpStatus)
                .body(ErrorResponse.of(httpStatus, message, code));
    }
}
