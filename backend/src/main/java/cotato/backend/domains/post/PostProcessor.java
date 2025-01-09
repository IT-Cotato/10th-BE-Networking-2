package cotato.backend.domains.post;

import static cotato.backend.common.exception.ErrorCode.*;

import java.util.List;
import java.util.concurrent.ExecutorService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cotato.backend.common.exception.ApiException;
import cotato.backend.domains.post.dto.PostConcept;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostProcessor {

	private final PostRepository postRepository;
	private final ExecutorService virtualThreadExecutor;

	@Transactional
	@CacheEvict(value = "postList", allEntries = true)
	public void removePost(Long postId) {
		postRepository.findById(postId).ifPresentOrElse(
			postRepository::delete,
			() -> {
				throw ApiException.from(POST_NOT_FOUND);
			}
		);
	}

	public List<PostConcept> findPostListSortByViews(Pageable pageable) {
		return postRepository.findAllByOrderByViewsDesc(pageable)
			.stream()
			.map(PostConcept::from)
			.toList();
	}

	@Cacheable(value = "postList", key = "#pageable.pageNumber")
	public List<PostConcept> findHotPostsRedisCache(Pageable pageable) {
		return postRepository.findAllByOrderByViewsDesc(pageable)
			.stream()
			.map(PostConcept::from)
			.toList();
	}

	@Cacheable(value = "postList", key = "#pageable.pageNumber")
	public List<PostConcept> findHotPostsVirtualThread(Pageable pageable) {
		return postRepository.findAllByOrderByViewsDesc(pageable)
			.stream()
			.map(PostConcept::from)
			.toList();
	}
}