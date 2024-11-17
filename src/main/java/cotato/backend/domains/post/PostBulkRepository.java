package cotato.backend.domains.post;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class PostBulkRepository {

	private static final int BATCH_SIZE = 500;

	private final JdbcTemplate jdbcTemplate;

	public void saveAllByExcel(List<Post> posts) {
		jdbcTemplate.batchUpdate(
			"INSERT INTO post (title, content, name, views) VALUES (?, ?, ?, ?)",
			new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setString(1, posts.get(i).getTitle());
					ps.setString(2, posts.get(i).getContent());
					ps.setString(3, posts.get(i).getName());
					ps.setLong(4, posts.get(i).getViews());
				}

				@Override
				public int getBatchSize() {
					return BATCH_SIZE;
				}
			}
		);
	}
}