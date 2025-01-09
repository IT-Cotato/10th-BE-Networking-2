package cotato.backend.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record SavePostRequest(
        @Schema(description = "제목")
        @NotNull(message = "제목을 입력해 주세요")
        String title,

        @Schema(description = "내용")
        @NotNull(message = "내용을 입력해 주세요")
        String content,

        @Schema(description = "작성자")
        @NotNull(message = "작성자를 입력해 주세요")
        String name
) {
}
