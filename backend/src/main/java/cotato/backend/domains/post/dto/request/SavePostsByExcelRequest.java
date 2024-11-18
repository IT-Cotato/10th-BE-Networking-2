package cotato.backend.domains.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SavePostsByExcelRequest {

	@NotBlank(message = "엑셀 파일 경로가 입력되지 않았습니다.")
	private String path;
}
