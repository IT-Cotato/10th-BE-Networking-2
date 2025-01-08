package cotato.backend.domains.post.dto.response;

import cotato.backend.domains.post.dto.PostDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 게시글 상세 조회	응답
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetPostResponse {

	// 게시글 제목
	private String title;

	// 게시글 내용
	private String content;

	// 게시글 작성자
	private String name;

	// 조회수
	private Long views;

	// Post -> GetPostResponse로 변환
	public static GetPostResponse toGetPostResponse(final PostDTO postDTO) {
		return GetPostResponse.builder()
			.title(postDTO.getTitle())
			.content(postDTO.getContent())
			.name(postDTO.getName())
			.views(postDTO.getViews())
			.build();
	}
}
