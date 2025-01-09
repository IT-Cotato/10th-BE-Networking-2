package cotato.backend.domains.post.dto.response;

import cotato.backend.domains.post.dto.PostConcept;

public record PostFindResponse(
	Long postId,
	String title,
	String author
) {
	public static PostFindResponse from(PostConcept post) {
		return new PostFindResponse(
			post.id(),
			post.title(),
			post.author()
		);
	}
}