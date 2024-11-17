package cotato.backend.common.exception;

import java.util.NoSuchElementException;

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

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException e){
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(e.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
	}

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<Object> handleApiException(ApiException e) {
		log.warn("handleApiException", e);

		return makeErrorResponseEntity(e.getHttpStatus(), e.getMessage(), e.getCode());
	}

	private ResponseEntity<Object> makeErrorResponseEntity(HttpStatus httpStatus, String message, String code) {
		return ResponseEntity
			.status(httpStatus)
			.body(ErrorResponse.of(httpStatus, message, code));
	}
}
