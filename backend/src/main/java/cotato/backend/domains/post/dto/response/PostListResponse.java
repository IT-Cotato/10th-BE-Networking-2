package cotato.backend.domains.post.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

public class PostListResponse {

	@Builder
	@Getter
	public static class PostPreviewList {
		List<PostPreview> postList;
		Integer totalPage;
		Integer currentPage;
	}

	@Builder
	@Getter
	public static class PostPreview{
		Long id;
		String title;
		String name;
		Integer views;
	}
}
