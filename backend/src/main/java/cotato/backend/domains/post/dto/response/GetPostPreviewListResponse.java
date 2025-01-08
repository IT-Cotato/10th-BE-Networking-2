package cotato.backend.domains.post.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import cotato.backend.domains.post.dto.PostListDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 게시글 미리보기 리스트 조회 응답
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetPostPreviewListResponse {

	// 게시글 미리보기 리스트
	private List<GetPostPreviewResponse> postList;

	// 전체 페이지 수
	private int totalPage;

	// 현재 페이지
	private int currentPage;

	// List<GetPostPreviewResponse> -> GetPostPreviewListResponse로 변환
	public static GetPostPreviewListResponse toGetPostPreviewListResponse(final PostListDTO postListDTO) {
		return GetPostPreviewListResponse.builder()
			.postList(postListDTO.getPostDTOList().stream()
				.map(GetPostPreviewResponse::toGetPostPreviewResponse)
				.collect(Collectors.toList()))
			.totalPage(postListDTO.getTotalPage())
			.currentPage(postListDTO.getCurrentPage())
			.build();
	}
}
