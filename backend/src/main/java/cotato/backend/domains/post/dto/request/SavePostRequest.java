package cotato.backend.domains.post.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SavePostRequest {

	@NotNull
	String title;

	@NotNull
	String content;

	@NotNull
	String name;
}
