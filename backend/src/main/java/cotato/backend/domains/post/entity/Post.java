package cotato.backend.domains.post.entity;

import cotato.backend.domains.post.dto.PostDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(indexes = {@Index(name = "idx_views", columnList = "views DESC")})
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;

	// 제목
	@Column(name = "title", nullable = false)
	private String title;

	// 내용
	@Column(name = "content", nullable = false)
	private String content;

	// 작성자
	@Column(name = "name", nullable = false)
	private String name;

	// 조회수, 기본값 0
	@Column(name = "views")
	private long views = 0L;

	// 조회수 증가
	public void increaseViews() {
		this.views++;
	}

	// PostDTO -> Post 엔터티로 변환
	public static Post toPostEntity(final PostDTO postDTO) {
		return Post.builder()
			.title(postDTO.getTitle())
			.content(postDTO.getContent())
			.name(postDTO.getName())
			.build();
	}
}
