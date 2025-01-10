package cotato.backend.domains.post.service;

import static cotato.backend.common.exception.ErrorCode.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cotato.backend.common.dto.PageResponse;
import cotato.backend.common.excel.ExcelUtils;
import cotato.backend.common.exception.ApiException;
import cotato.backend.domains.post.dto.request.SavePostRequest;
import cotato.backend.domains.post.dto.response.FindPostByIdResponse;
import cotato.backend.domains.post.dto.response.FindPostsByPopularResponse;
import cotato.backend.domains.post.entity.Post;
import cotato.backend.domains.post.repository.PostJDBCRepository;
import cotato.backend.domains.post.repository.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class PostService {

	private final PostJDBCRepository postJDBCRepository;
	private final PostRepository postRepository;
	private final Map<Long, AtomicInteger> viewCounts;

	// 로컬 파일 경로로부터 엑셀 파일을 읽어 Post 엔터티로 변환하고 저장
	@Transactional
	public void saveEstatesByExcel(String filePath) {
		try {
			// 엑셀 파일을 읽어 데이터 프레임 형태로 변환
			List<Post> posts = ExcelUtils.parseExcelFile(filePath).stream()
				.map(row -> {
					String title = row.get("title");
					String content = row.get("content");
					String name = row.get("name");

					return new Post(title, content, name);
				})
				.collect(Collectors.toList());

			// Post 엔터티를 저장
			postJDBCRepository.saveAll(posts);
		} catch (Exception e) {
			log.error("Failed to save estates by excel", e);
			throw ApiException.from(INTERNAL_SERVER_ERROR);
		}
	}

	// 글 저장
	@Transactional
	public void save(SavePostRequest request) {
		Post post = Post.from(request);

		postRepository.save(post);
	}

	// 글 조회
	@Transactional
	public FindPostByIdResponse findPostDtoByIdAndIncreaseView(Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> ApiException.from(POST_NOT_FOUND));

		// 조회수 증가
		incrementViewCountInCache(postId);

		return FindPostByIdResponse.from(post);
	}

	// 조회수 증가
	private void incrementViewCountInCache(Long postId) {
		viewCounts.computeIfAbsent(postId, id -> new AtomicInteger(0))
			.incrementAndGet();
	}

	// 글 삭제
	@Transactional
	public void deletePostById(Long postId) {
		postRepository.deleteById(postId);
	}

	// 글목록 인기순 조회
	@Cacheable(cacheNames = "shortTermCache", key = "'posts:page:' + #page + ':size:' + #size")
	public PageResponse<FindPostsByPopularResponse> findPostsByPopular(int page, int size) {
		PageRequest pageRequest = PageRequest.of(page, size);

		Page<Post> posts = postRepository.findAllByOrderByViewsDesc(pageRequest);

		return PageResponse.from(posts.map(FindPostsByPopularResponse::from));
	}

	// 글 조회
	@Cacheable(cacheNames = "shortTermCache", key = "'posts:postId:' + #postId")
	public Post findById(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> ApiException.from(POST_NOT_FOUND));
	}
}
