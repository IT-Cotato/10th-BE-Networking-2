package cotato.backend.domains.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SavePostRequest {

	@NotBlank(message = "제목이 입력되지 않았습니다.")
	private String title;

	@NotBlank(message = "내용이 입력되지 않았습니다.")
	private String content;

	@NotBlank(message = "작성자 이름이 입력되지 않았습니다.")
	private String name;

}