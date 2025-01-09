package cotato.backend.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	//Post
	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다.", "POST_001"),

	//400
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", "COMMON-001"),
	INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "요청 파라미터가 잘 못 되었습니다.", "COMMON-002"),
	INVALID_INPUT(HttpStatus.BAD_REQUEST, "값이 없습니다", "COMMON-003"),

	//500
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부에서 에러가 발생하였습니다.", "COMMON-002"),
	;

	private final HttpStatus httpStatus;
	private final String message;
	private final String code;
}
