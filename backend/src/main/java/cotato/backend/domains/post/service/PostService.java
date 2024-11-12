package cotato.backend.domains.post.service;

import static cotato.backend.common.exception.ErrorCode.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cotato.backend.common.excel.ExcelUtils;
import cotato.backend.common.exception.ApiException;
import cotato.backend.domains.post.dto.response.FindPostByIdResponse;
import cotato.backend.domains.post.dto.response.FindPostsByPopularResponse;
import cotato.backend.domains.post.entity.Post;
import cotato.backend.domains.post.dto.request.SavePostRequest;
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

	private static final int MAX_RETRIES = 3;

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
	public FindPostByIdResponse findPostById(Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> ApiException.from(POST_NOT_FOUND));

		// 조회수 증가
		safeIncreaseViews(post);

		return FindPostByIdResponse.from(post);
	}

	private void safeIncreaseViews(Post post) {
		int attempt = 0;
		boolean success = false;

		while (attempt < MAX_RETRIES && !success) {
			try {
				attempt++;

				// 조회수 증가
				post.increaseViews();

				postRepository.save(post);
				success = true; // 저장 성공 시 반복문 종료
			} catch (ObjectOptimisticLockingFailureException e) {
				if (attempt >= MAX_RETRIES) {
					throw ApiException.from(INTERNAL_SERVER_ERROR);
				}
				// 로그 출력
				System.out.println("Optimistic lock exception on attempt " + attempt + ". Retrying...");
			}
		}
	}

	// 글 삭제
	@Transactional
	public void deletePostById(Long postId) {
		postRepository.deleteById(postId);
	}

	// 글목록 인기순 조회
	public Page<FindPostsByPopularResponse> findPostsByPopular(int page, int size) {
		PageRequest pageRequest = PageRequest.of(page, size);

		Page<Post> posts = postRepository.findAllByOrderByViewsDesc(pageRequest);

		return posts.map(FindPostsByPopularResponse::from);
	}
}
