package cotato.backend.domains.post.repository;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cotato.backend.domains.post.entity.Post;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostJDBCRepository {

	private final JdbcTemplate jdbcTemplate;

	@Transactional
	public void saveAll(List<Post> posts) {
		String sql = "INSERT INTO post (title, content, name, views) " +
			"VALUES (?, ?, ?, ?)";

		jdbcTemplate.batchUpdate(sql,
			posts,
			posts.size(),
			(PreparedStatement ps, Post post) -> {
				ps.setString(1, post.getTitle());
				ps.setString(2, post.getContent());
				ps.setString(3, post.getName());
				ps.setLong(4, post.getViews());
			});
	}
}
