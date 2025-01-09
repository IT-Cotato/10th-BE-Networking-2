package cotato.backend.domains.post;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cotato.backend.domains.post.dto.PostConcept;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostService {

	private final PostAppender postAppender;
	private final PostReader postReader;
	private final PostProcessor postProcessor;
	private final PostBatchProcessor postBatchProcessor;

	public void saveEstatesByExcel(String filePath) {
		postBatchProcessor.savePostsByExcelWithBatch(filePath);
	}

	public void saveEstatesByExcelWithVirtualThread(String filePath) {
		postBatchProcessor.savePostsByExcelWithVirtualThread(filePath);
	}

	public void createSinglePost(String content, String title, String name) {
		postAppender.append(content, title, name);
	}

	public PostConcept findSinglePost(Long postId) {
		return postReader.findSinglePost(postId);
	}

	public List<PostConcept> findPostListSortByViews(Pageable pageable) {
		return postProcessor.findPostListSortByViews(pageable);
	}

	public List<PostConcept> findHotPostListRedis(Pageable pageable) {
		return postProcessor.findHotPostsRedisCache(pageable);
	}

	public CompletableFuture<List<PostConcept>> findHotPostListVirtualThread(Pageable pageable) {
		return CompletableFuture.supplyAsync(() -> postProcessor.findHotPostsVirtualThread(pageable));
	}

	public void deletePostById(Long postId) {
		postProcessor.removePost(postId);
	}
}
