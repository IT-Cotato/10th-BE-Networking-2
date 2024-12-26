package cotato.backend.domains.post.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SavePostRequest(
        @NotBlank(message = "제목은 필수 입력 값입니다.")
        String title,
        @NotBlank(message = " 내용은 필수 입력 값입니다.")
        String content,
        @NotBlank(message = "이름은 필수 입력 값입니다.")
        String name
) {
}
