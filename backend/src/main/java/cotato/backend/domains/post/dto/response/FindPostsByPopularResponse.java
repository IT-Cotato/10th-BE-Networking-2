package cotato.backend.domains.post.dto.response;

import cotato.backend.domains.post.entity.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindPostsByPopularResponse {

	private String postId;
	private String title;
	private String content;
	private String name;
	private Long views;

	public static FindPostsByPopularResponse from(Post post) {
		return new FindPostsByPopularResponse(
			post.getId().toString(),
			post.getTitle(),
			post.getContent(),
			post.getName(),
			post.getViews()
		);
	}
}
