package cotato.backend.domains.post.dto.response;

import cotato.backend.domains.post.dto.PostConcept;

public record SinglePostResponse(
	String title,
	String content,
	String author,
	Long views
) {
	public static SinglePostResponse from(PostConcept postConcept) {
		return new SinglePostResponse(
			postConcept.title(),
			postConcept.content(),
			postConcept.author(),
			postConcept.views()
		);
	}
}