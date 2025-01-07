package cotato.backend.domains.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostDTO {

	@NotBlank(message = "제목을 적어주세요")
	private String title;

	@NotBlank(message = "내용을 적어주세요")
	private String content;

	@NotBlank(message = "이름을 적어주세요")
	private String name;




}
