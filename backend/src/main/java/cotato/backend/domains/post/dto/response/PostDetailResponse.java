package cotato.backend.domains.post.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostDetailResponse {
	String title;
	String content;
	String name;
	Integer views;
}
