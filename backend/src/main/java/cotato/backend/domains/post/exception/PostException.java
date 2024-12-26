package cotato.backend.domains.post.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PostException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String code;

    private PostException(
            final HttpStatus httpStatus,
            final String message,
            final String code
    ) {
        super(message);
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public static PostException from(final PostErrorCode postErrorCode) {
        return new PostException(
                postErrorCode.getHttpStatus(),
                postErrorCode.getMessage(),
                postErrorCode.getCode()
        );
    }
}
