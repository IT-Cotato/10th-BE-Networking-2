package cotato.backend.domains.post.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class PostException extends RuntimeException {

	private final HttpStatus httpStatus;
	private final String code;

	public PostException(
		final HttpStatus httpStatus,
		final String message,
		final String code
	) {
		super(message);
		this.httpStatus = httpStatus;
		this.code = code;
	}

	public static PostException from(final PostErrorCode errorCode) {
		return new PostException(errorCode.getHttpStatus(), errorCode.getMessage(), errorCode.getCode());
	}
}
