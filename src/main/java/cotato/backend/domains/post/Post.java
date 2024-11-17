package cotato.backend.domains.post;

import cotato.backend.domains.post.dto.request.SavePostRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "제목을 입력해야 합니다.")
	private String title;

	@NotBlank(message = "내용을 입력해야 합니다.")
	private String content;

	@NotBlank(message = "작성자 이름을 입력해야 합니다.")
	private String name;

	private int views;

	public Post(String title, String content, String name) {
		this.title = title;
		this.content = content;
		this.name = name;
		this.views = 0;
	}

	public static Post createdFrom(SavePostRequest request) {
		return new Post(request.getTitle(), request.getContent(), request.getName());
	}

	public void increaseViews() {
		this.views += 1;
	}

}