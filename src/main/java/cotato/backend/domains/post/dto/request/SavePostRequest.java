package cotato.backend.domains.post.dto.request;

import lombok.Data;

@Data
public class SavePostRequest {

	private String title;
	private String content;
	private String name;

}