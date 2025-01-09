package cotato.backend.domains.post;

import java.util.List;

public interface PostJdbcRepository {

	void saveAll(List<Post> posts);
}