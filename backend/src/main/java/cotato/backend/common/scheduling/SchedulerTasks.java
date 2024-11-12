package cotato.backend.common.scheduling;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cotato.backend.domains.post.entity.Post;
import cotato.backend.domains.post.service.PostService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SchedulerTasks {

	private final Map<Long, AtomicInteger> viewCounts;
	private final PostService postService;

	// 조회수 증가를 DB에 반영
	@Scheduled(fixedRate = 10000) // 매 1분마다 캐시를 DB에 반영
	@Transactional
	public void persistViewCounts() {
		viewCounts.forEach((postId, count) -> {
			Post post = postService.findById(postId);

			post.setViews(post.getViews() + count.get());

			count.set(0); // 캐시 초기화
		});
	}
}
