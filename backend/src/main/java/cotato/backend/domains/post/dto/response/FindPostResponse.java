package cotato.backend.domains.post.dto.response;

import cotato.backend.domains.post.Post;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindPostResponse {

	private String title;
	private String content;
	private String name;
	private int views;

	public FindPostResponse(String title, String content, String name, int views) {
		this.title = title;
		this.content = content;
		this.name = name;
		this.views = views;
	}

	public static FindPostResponse createdFrom(Post post) {
		return new FindPostResponse(post.getTitle(), post.getContent(), post.getName(), post.getViews());
	}

}