package cotato.backend.domains.post;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

public interface PostRepository extends JpaRepository<Post, Long> {

	List<Post> findAllByOrderByViewsDesc(Pageable pageable);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM Post p WHERE p.id = :id")
	Optional<Post> findPostByIdWithPessimisticLock(@Param("id") Long id);
}