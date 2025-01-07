package cotato.backend.common.exception;

import static cotato.backend.common.exception.ErrorCode.*;

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
	public ResponseEntity<String> handleApiException(ApiException e) {
		log.warn("handleApiException", e);

		return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> MethodArgumentNotValidException(MethodArgumentNotValidException ex){
		ApiException api = ApiException.from(NOT_VALID_ERROR);
		return ResponseEntity.status(api.getHttpStatus()).body(api.getMessage());
	}


	private ResponseEntity<Object> makeErrorResponseEntity(HttpStatus httpStatus, String message, String code) {
		return ResponseEntity
			.status(httpStatus)
			.body(ErrorResponse.of(httpStatus, message, code));
	}
}
