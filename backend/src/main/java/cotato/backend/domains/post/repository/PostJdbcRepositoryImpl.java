package cotato.backend.domains.post.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cotato.backend.domains.post.entity.Post;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostJdbcRepositoryImpl implements PostJdbcRepository {

	private final JdbcTemplate jdbcTemplate;

	private static final int BATCH_SIZE = 1000;

	// 게시글 목록 저장, BULK INSERT 방식으로 처리
	@Transactional
	public void saveAllPostsByJdbcTemplate(List<Post> postList) {
		String sql = "INSERT INTO post (title, content, name, views) VALUES (?, ?, ?, ?)";
		jdbcTemplate.batchUpdate(sql, postList, BATCH_SIZE, (ps, post) -> {
			ps.setString(1, post.getTitle());
			ps.setString(2, post.getContent());
			ps.setString(3, post.getName());
			ps.setLong(4, post.getViews());
		});
	}
}
