package cotato.backend.domains.post.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode {

	// 게시글 저장 관련 에러
	SAVE_EXCEL_FILE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "엑셀 파일 저장에 실패하였습니다.", "POST-001"),
	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다.", "POST-002"),

	;

	private final HttpStatus httpStatus;
	private final String message;
	private final String code;
}
