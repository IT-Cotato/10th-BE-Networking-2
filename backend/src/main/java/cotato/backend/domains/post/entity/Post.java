package cotato.backend.domains.post.entity;

import static jakarta.persistence.GenerationType.*;

import cotato.backend.domains.post.dto.request.SavePostRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

	@Id
	@Column(name = "post_id")
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Long views;

	public Post(
		String title,
		String content,
		String name
	) {
		this.title = title;
		this.content = content;
		this.name = name;
		this.views = 0L;
	}

	public static Post from(SavePostRequest savePostRequest) {
		return new Post(
			savePostRequest.getTitle(),
			savePostRequest.getContent(),
			savePostRequest.getName()
		);
	}

	public void increaseViews() {
		this.views++;
	}
}
