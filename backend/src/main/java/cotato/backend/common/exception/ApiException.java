package cotato.backend.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

	private final HttpStatus httpStatus;
	private final String code;

	private ApiException(
			final HttpStatus httpStatus,
			final String message,
			final String code
	) {
		super(message);
		this.httpStatus = httpStatus;
		this.code = code;
	}

	public static ApiException from(final ErrorCode errorCode) {
		return new ApiException(errorCode.getHttpStatus(), errorCode.getMessage(), errorCode.getCode());
	}
}
