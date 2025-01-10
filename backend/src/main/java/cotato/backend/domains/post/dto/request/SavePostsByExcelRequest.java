package cotato.backend.domains.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SavePostsByExcelRequest {

	@NotBlank(message = "파일 경로를 입력해주세요.")
	private String path;
}
