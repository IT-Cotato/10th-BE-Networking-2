package cotato.backend.domains.post;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostJdbcRepositoryImpl implements PostJdbcRepository {

	private final JdbcTemplate jdbcTemplate;
	private final int BATCH_SIZE = 1000;

	@Override
	public void saveAll(List<Post> posts) {
		int batchCount = 0;
		List<Post> subItems = new ArrayList<>();
		for (int i = 0; i < posts.size(); i++) {
			subItems.add(posts.get(i));
			if ((i + 1) % BATCH_SIZE == 0) {
				batchCount = batchInsert(batchCount, subItems);
			}
		}
		if (!subItems.isEmpty()) {
			batchCount = batchInsert(batchCount, subItems);
		}
		System.out.println("batchCount: " + batchCount);
	}

	private int batchInsert(int batchCount, List<Post> subPosts) {
		jdbcTemplate.batchUpdate("INSERT INTO POST (`TITLE`, `CONTENT`, `NAME`, `VIEWS`) VALUES (?, ?, ?, ?)",
			new BatchPreparedStatementSetter() {
				@Override
				public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
					ps.setString(1, subPosts.get(i).getName());
					ps.setString(2, subPosts.get(i).getTitle());
					ps.setString(3, subPosts.get(i).getContent());
					ps.setLong(4, subPosts.get(i).getViews());
				}

				@Override
				public int getBatchSize() {
					return subPosts.size();
				}
			});
		subPosts.clear();
		batchCount++;
		return batchCount;
	}
}