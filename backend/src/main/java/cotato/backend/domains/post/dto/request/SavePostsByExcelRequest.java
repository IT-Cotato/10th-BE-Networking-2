package cotato.backend.domains.post.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SavePostsByExcelRequest (
		@NotBlank(message = "경로는 공백이 될 수 없습니다.")
		String path
) {
}
