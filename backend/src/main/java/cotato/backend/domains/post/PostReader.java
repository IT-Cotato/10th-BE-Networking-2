package cotato.backend.domains.post;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cotato.backend.common.exception.ApiException;
import cotato.backend.common.exception.ErrorCode;
import cotato.backend.domains.post.dto.PostConcept;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostReader {

	private final PostRepository postRepository;

	@Transactional
	public PostConcept findSinglePost(Long id) {
		Post post = postRepository.findPostByIdWithPessimisticLock(id)
			.orElseThrow(() -> ApiException.from(ErrorCode.POST_NOT_FOUND));
		post.increaseViews();
		return PostConcept.from(post);
	}
}