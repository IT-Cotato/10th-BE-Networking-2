package cotato.backend.domains.post.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SavePostsByExcelRequest {

	private String path;
}
