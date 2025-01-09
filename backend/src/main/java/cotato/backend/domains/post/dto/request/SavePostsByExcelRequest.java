package cotato.backend.domains.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SavePostsByExcelRequest {

	@Schema(description = "엑셀 파일 경로")
	@NotNull(message = "파일 경로를 입력해 주세요")
	private String path;
}
