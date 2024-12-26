package cotato.backend.domains.post.repository;

import cotato.backend.domains.post.entity.Post;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("FROM Post WHERE id=:postId")
    Optional<Post> findByIdWithPessimisticLock(@Param("postId") final long postId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("FROM Post WHERE id=:postId")
    Optional<Post> findByIdWithOptimisticLock(@Param("postId") final long postId);
}
