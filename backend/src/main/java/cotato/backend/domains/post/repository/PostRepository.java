package cotato.backend.domains.post.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cotato.backend.domains.post.entity.Post;
import jakarta.persistence.LockModeType;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostJdbcRepository {

	@Query("SELECT p FROM Post p ORDER BY p.views DESC")
	Page<Post> findAllByOrderByViewsDesc(Pageable pageable);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM Post p WHERE p.id = :id")
	Optional<Post> findByIdForUpdate(long id);

	@Lock(LockModeType.OPTIMISTIC)
	@Query("SELECT p FROM Post p WHERE p.id = :id")
	Optional<Post> findByIdWithOptimisticLock(long id);
}
