package cotato.backend.domains.post.service;

import static cotato.backend.common.exception.ErrorCode.*;

import cotato.backend.domains.post.entity.Post;
import cotato.backend.domains.post.repository.ExcelBatchRepository;
import cotato.backend.domains.post.repository.PostRepository;
import cotato.backend.domains.post.dto.PostDTO;
import cotato.backend.domains.post.dto.PostListDTO;
import cotato.backend.domains.post.exception.PostErrorCode;
import cotato.backend.domains.post.exception.PostException;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cotato.backend.common.excel.ExcelUtils;
import cotato.backend.common.exception.ApiException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostService {
	private static final long INCREASE_VIEWS_AMOUNT = 1L;
	private static final String COLUMN_VIEWS = "views";
	private static final int PAGE_SIZE = 10;

	private final PostRepository postRepository;
	private final ExcelBatchRepository excelBatchRepository;

	@Transactional(readOnly = true)
	protected Sort getSort() {
		return Sort.by(
				Order.desc(COLUMN_VIEWS)
		);
	}

	@Transactional(readOnly = true)
	protected Pageable getPageable(final int page) {
		return PageRequest.of(
				page,
				PAGE_SIZE,
				getSort()
		);
	}

	// 로컬 파일 경로로부터 엑셀 파일을 읽어 Post 엔터티로 변환하고 저장
	@Transactional
	public void saveEstatesByExcel(final String filePath) {
		try {
			// 엑셀 파일을 읽어 데이터 프레임 형태로 변환
			List<Post> posts = ExcelUtils.parseExcelFile(filePath).stream()
					.map(PostDTO::toPostDTO)
					.map(Post::toPost)
					.toList();

			excelBatchRepository.saveAllPosts(posts);
		} catch (Exception e) {
			log.error("Failed to save estates by excel", e);
			throw ApiException.from(INTERNAL_SERVER_ERROR);
		}
	}

	// 전달받은 값으로 Post 엔티티로 변환하고 저장
	@Transactional
	public void savePost(final PostDTO postDTO) {
		postRepository.save(Post.toPost(postDTO));
	}

	// 전달받은 id 값으로 Post 엔티티를 조회하고, PostDTO로 변환해 반환
	// 조회 성공 시, views 값을 1 늘려줌
	@Transactional
	public PostDTO findPostById(final long id) {
		Post foundPost = postRepository.findById(id).orElseThrow(
				() -> PostException.from(PostErrorCode.NOT_EXIST)
		);

		foundPost.setViews(foundPost.getViews() + INCREASE_VIEWS_AMOUNT);

		return PostDTO.toPostDTO(foundPost);
	}

	// findPostById 함수의 동시성 문제 해결 버전 - Pessimistic Lock
	@Transactional
	public PostDTO findPostByIdWithPessimisticLock(final long id) {
		Post foundPost = postRepository.findByIdWithPessimisticLock(id).orElseThrow(
				() -> PostException.from(PostErrorCode.NOT_EXIST)
		);

		foundPost.setViews(foundPost.getViews() + INCREASE_VIEWS_AMOUNT);

		return PostDTO.toPostDTO(foundPost);
	}

	// findPostById 함수의 동시성 문제 해결 버전 - Optimistic Lock
	@Transactional
	public PostDTO findPostByIdWithOptimisticLock(final long id) {
		Post foundPost = postRepository.findByIdWithOptimisticLock(id).orElseThrow(
				() -> PostException.from(PostErrorCode.NOT_EXIST)
		);

		foundPost.setViews(foundPost.getViews() + INCREASE_VIEWS_AMOUNT);

		return PostDTO.toPostDTO(foundPost);
	}

	// 전달받은 id 값으로 Post 엔티티를 조회하고, 있다면 삭제
	// 없다면 에러를 반환
	@Transactional
	public void deletePostById(final long id) {
		// 대상 게시글이 없다면 에러 반환
		if (!postRepository.existsById(id)) {
			throw PostException.from(PostErrorCode.NOT_EXIST);
		}

		postRepository.deleteById(id);
	}

	// page 값으로 Pageable 생성하고 views가 높은 순으로 목록 조회
	// 10개의 게시글과 현재 페이, 전체 페이지 수 반환
	@Transactional(readOnly = true)
	public PostListDTO getPosts(final int page) {
		Pageable pageable = getPageable(page);

		return PostListDTO.toPostListDTO(
				postRepository.findAll(pageable)
		);
	}
}
