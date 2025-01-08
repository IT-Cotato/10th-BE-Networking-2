package cotato.backend.domains.post.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import cotato.backend.domains.post.entity.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostListDTO {

	// 게시글 미리보기 리스트
	private List<PostDTO> postDTOList;

	// 전체 페이지 수
	private int totalPage;

	// 현재 페이지
	private int currentPage;

	// Map<String, String> -> PostDTO로 변환
	public static PostListDTO toPostListDTO(final Page<Post> postPage) {
		return PostListDTO.builder()
			.postDTOList(postPage.getContent().stream()
				.map(PostDTO::toPostDTO)
				.collect(Collectors.toList()))
			.totalPage(postPage.getTotalPages())
			.currentPage(postPage.getNumber())
			.build();
	}
}
