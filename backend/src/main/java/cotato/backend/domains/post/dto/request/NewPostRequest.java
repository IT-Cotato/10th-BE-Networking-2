package cotato.backend.domains.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(title = "단일 게시글 생성 DTO")
public record NewPostRequest(

	@Schema(description = "게시글 제목")
	@NotBlank(message = "게시글 제목을 입력해 주세요")
	String title,

	@Schema(description = "게시글 내용")
	@Size(max = 100, message = "게시글 내용은 100자를 넘을 수 없습니다.")
	@NotBlank(message = "게시글 내용을 입력해 주세요")
	String content,

	@Schema(description = "글쓴이")
	@NotBlank(message = "글쓴이를 입력해 주세요")
	String name
) {
}