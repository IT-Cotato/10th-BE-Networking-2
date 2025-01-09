package cotato.backend.domains.post.dto.response;

import java.util.List;

import cotato.backend.domains.post.dto.PostConcept;

public record PostListFindResponse(
	List<PostFindResponse> postFindResponseList
) {
	public static PostListFindResponse from(List<PostConcept> posts) {
		return new PostListFindResponse(
			posts.stream()
				.map(PostFindResponse::from)
				.toList());
	}
}