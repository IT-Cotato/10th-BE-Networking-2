package cotato.backend.common.exception;

import static cotato.backend.common.exception.ErrorCode.*;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import cotato.backend.common.dto.ErrorResponse;
import cotato.backend.domains.post.exception.PostException;
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
	public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.warn("handleMethodArgumentNotValidException", e);

		Map<String, String> reasons = e.getBindingResult().getFieldErrors()
			.stream()
			.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

		return makeErrorResponseEntity(reasons);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
		log.warn("handleMethodArgumentTypeMismatchException", e);

		return makeMethodArgumentTypeMismatchErrorResponseEntity();
	}

	private ResponseEntity<Object> makeErrorResponseEntity(HttpStatus httpStatus, String message, String code) {
		return ResponseEntity
			.status(httpStatus)
			.body(ErrorResponse.of(httpStatus, message, code));
	}

	private ResponseEntity<Object> makeErrorResponseEntity(Map<String, String> reasons) {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.of(INVALID_PARAMETER, reasons));
	}

	private ResponseEntity<Object> makeMethodArgumentTypeMismatchErrorResponseEntity() {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(
				ErrorResponse.of(HttpStatus.BAD_REQUEST, INVALID_PARAMETER.getMessage(), INVALID_PARAMETER.getCode()));
	}

}
