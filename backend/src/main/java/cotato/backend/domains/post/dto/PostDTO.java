package cotato.backend.domains.post.dto;

import java.util.Map;

import cotato.backend.domains.post.dto.request.SavePostRequest;
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
public class PostDTO {

	private Long id;

	private String title;

	private String content;

	private String name;

	private long views;

	private static final String EXCEL_TITLE = "title";
	private static final String EXCEL_CONTENT = "content";
	private static final String EXCEL_NAME = "name";

	// SavePostRequest -> PostDTO로 변환
	public static PostDTO toPostDTO(final SavePostRequest request) {
		return PostDTO.builder()
			.title(request.getTitle())
			.content(request.getContent())
			.name(request.getName())
			.build();
	}

	// Post -> PostDTO로 변환
	public static PostDTO toPostDTO(final Post post) {
		return PostDTO.builder()
			.id(post.getId())
			.title(post.getTitle())
			.content(post.getContent())
			.name(post.getName())
			.views(post.getViews())
			.build();
	}

	// Map<String, String> -> PostDTO로 변환
	public static PostDTO toPostDTO(final Map<String, String> parsedData) {
		return PostDTO.builder()
			.title(parsedData.get(EXCEL_TITLE))
			.content(parsedData.get(EXCEL_CONTENT))
			.name(parsedData.get(EXCEL_NAME))
			.build();
	}
}


