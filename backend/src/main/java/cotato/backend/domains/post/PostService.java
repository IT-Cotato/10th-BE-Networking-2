package cotato.backend.domains.post;

import static cotato.backend.common.exception.ErrorCode.*;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cotato.backend.common.excel.ExcelUtils;
import cotato.backend.common.exception.ApiException;
import cotato.backend.domains.post.dto.request.SavePostRequest;
import cotato.backend.domains.post.dto.response.FindPostListResponse;
import cotato.backend.domains.post.dto.response.FindPostResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostService {

	private final PostRepository postRepository;
	private final PostBulkRepository postBulkRepository;

	@Transactional
	public void savePost(SavePostRequest request){
		Post post = Post.createdFrom(request);
		postRepository.save(post);
	}

	// 로컬 파일 경로로부터 엑셀 파일을 읽어 Post 엔터티로 변환하고 저장
	@Transactional
	public void saveEstatesByExcel(String filePath) {
		try {
			// 엑셀 파일을 읽어 데이터 프레임 형태로 변환
			List<Post> posts = ExcelUtils.parseExcelFile(filePath).stream()
				.map(Post::createdFrom)
				.toList();

			postBulkRepository.saveAllByExcel(posts);

		} catch (Exception e) {
			log.error("Failed to save estates by excel", e);
			throw ApiException.from(INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional(readOnly = true)
	public FindPostResponse findPostById(Long id) {
		Post post = postRepository.findById(id)
			.orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다."));

		postRepository.increaseViews(id);
		return FindPostResponse.createdFrom(post);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "Posts", key = "#pageable.pageNumber", cacheManager = "redisCacheManager")
	public FindPostListResponse findPostsWithPaging(Pageable pageable) {
		Page<Post> posts = postRepository.findAll(pageable);
		return FindPostListResponse.createdFrom(posts);
	}

	@Transactional
	@CacheEvict(value = "Posts", allEntries = true, cacheManager = "redisCacheManager")
	public void deletePostById(Long id){
		Post post = postRepository.findById(id)
			.orElseThrow(() -> { return new NoSuchElementException("게시글이 존재하지 않습니다."); });

		postRepository.delete(post);
	}
}
