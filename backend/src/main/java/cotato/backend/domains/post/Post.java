package cotato.backend.domains.post;

import java.util.Map;

import cotato.backend.domains.post.dto.request.SavePostRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
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

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
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

	public static Post createdFrom(Map<String, String> row) {
		return new Post(row.get("title"), row.get("content"), row.get("name"));
	}

	public void increaseViews() {
		this.views += 1;
	}

}