package cotato.backend.domains.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SavePostRequest {

	@NotBlank(message = "제목을 입력해주세요.")
	private String title;

	@NotBlank(message = "내용을 입력해주세요.")
	private String content;

	@NotBlank(message = "작성자를 입력해주세요.")
	private String name;
}
