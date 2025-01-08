package cotato.backend.domains.post.repository;

import java.util.List;

import cotato.backend.domains.post.entity.Post;

public interface PostJdbcRepository {

	void saveAllPostsByJdbcTemplate(List<Post> postList);

}
