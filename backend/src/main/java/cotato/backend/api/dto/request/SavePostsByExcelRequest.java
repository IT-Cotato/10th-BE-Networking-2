package cotato.backend.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record SavePostsByExcelRequest(
        @Schema(description = "엑셀 파일 경로")
        @NotNull(message = "파일 경로를 입력해 주세요")
        String path
) {
}
