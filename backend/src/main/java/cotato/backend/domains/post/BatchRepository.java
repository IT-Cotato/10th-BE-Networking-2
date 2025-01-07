package cotato.backend.domains.post;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BatchRepository {
	private final JdbcTemplate jdbcTemplate;

	public void insert(List<Post> postList) {

		String itemSql = "INSERT INTO post (title, content, name) VALUES (?, ?, ?)";

		jdbcTemplate.batchUpdate(itemSql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {

				Post post = postList.get(i);
				ps.setString(1, post.getTitle());
				ps.setString(2, post.getContent());
				ps.setString(3, post.getName());
			}

			@Override
			public int getBatchSize() {
				return postList.size();
			}
		});
	}
}
