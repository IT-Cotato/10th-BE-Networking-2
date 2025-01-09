package cotato.backend.domains.repository;

import cotato.backend.domains.post.entity.Post;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostJdbcRepository {

    private final int BATCH_SIZE = 1000;

    private final JdbcTemplate jdbcTemplate;

    public void saveAllWithBatch(List<Post> posts) {
        String POST_SQL = "INSERT INTO post (post_title, post_content, post_writer, view, likes) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(POST_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, posts.get(i).getTitle());
                ps.setString(2, posts.get(i).getContent());
                ps.setString(3, posts.get(i).getName());
                ps.setLong(4, posts.get(i).getViews());
                ps.setLong(5, posts.get(i).getLikes());
            }

            @Override
            public int getBatchSize() {
                return BATCH_SIZE;
            }
        });
    }
}
