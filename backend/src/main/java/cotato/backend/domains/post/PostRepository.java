package cotato.backend.domains.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	@Modifying
	@Query("UPDATE Post p SET p.views = p.views + 1 WHERE p.id = :id")
	void increaseViews(Long id);

	@Override
	Page<Post> findAll(Pageable pageable);

}
