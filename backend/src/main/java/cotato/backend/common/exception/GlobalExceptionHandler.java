package cotato.backend.common.exception;

import cotato.backend.domains.post.exception.PostException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
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

	@ExceptionHandler(PostException.class)
	public ResponseEntity<Object> handlePostException(PostException e) {
		log.warn("handlePostException", e);

		return makeErrorResponseEntity(e.getHttpStatus(), e.getMessage(), e.getCode());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException e) {
		Map<String, String> errors = new HashMap<>();
		e.getBindingResult().getAllErrors()
				.forEach(error -> errors.put(((FieldError) error).getField(), error.getDefaultMessage()));

		return makeValidationErrorResponseEntity(errors);
	}

	private ResponseEntity<Object> makeErrorResponseEntity(HttpStatus httpStatus, String message, String code) {
		return ResponseEntity
				.status(httpStatus)
				.body(ErrorResponse.of(httpStatus, message, code));
	}

	private ResponseEntity<Object> makeValidationErrorResponseEntity(Map<String, String> reasons) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.of(ErrorCode.INVALID_PARAMETER, reasons));
	}
}
