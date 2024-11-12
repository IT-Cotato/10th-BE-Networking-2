package cotato.backend.domains.post.repository;

import static jakarta.persistence.LockModeType.*;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import cotato.backend.domains.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

	Page<Post> findAllByOrderByViewsDesc(Pageable pageable);

	@Lock(PESSIMISTIC_WRITE)
	Optional<Post> findById(Long id);
}
