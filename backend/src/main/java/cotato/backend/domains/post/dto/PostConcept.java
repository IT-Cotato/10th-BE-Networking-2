package cotato.backend.domains.post.dto;

import cotato.backend.domains.post.Post;

public record PostConcept(
	Long id,
	String title,
	String content,
	String author,
	Long views
) {
	public static PostConcept from(Post post) {
		return new PostConcept(
			post.getId(),
			post.getTitle(),
			post.getContent(),
			post.getName(),
			post.getViews()
		);
	}

}
