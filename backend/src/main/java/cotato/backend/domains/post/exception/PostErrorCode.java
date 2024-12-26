package cotato.backend.domains.post.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode {
    NOT_EXIST(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글입니다.", "POST-001");

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;
}
