package cotato.backend.domains.post.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import cotato.backend.domains.post.Post;
import lombok.Data;

@Data
public class FindPostListResponse {
	private int currentPage;
	private int totalPage;
	List<FindPostResponse> findPostResponses;

	public static FindPostListResponse createdFrom(Page<Post> posts) {
		FindPostListResponse findPostListResponse = new FindPostListResponse();
		findPostListResponse.currentPage = posts.getNumber();
		findPostListResponse.totalPage = posts.getTotalPages();
		findPostListResponse.findPostResponses = posts.get()
			.map(FindPostResponse::createdFrom)
			.collect(Collectors.toList());
		return findPostListResponse;
	}
}
