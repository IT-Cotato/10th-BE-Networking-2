package cotato.backend.domains.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 단일 게시글 저장 요청
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SavePostRequest {

	// 게시글 제목
	@NotBlank(message = "제목을 입력해주세요.")
	private String title;

	// 게시글 내용
	@NotBlank(message = "내용을 입력해주세요.")
	private String content;

	// 게시글 작성자
	@NotBlank(message = "작성자를 입력해주세요.")
	private String name;

}
