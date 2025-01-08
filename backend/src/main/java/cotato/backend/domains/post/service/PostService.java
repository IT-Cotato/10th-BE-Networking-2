package cotato.backend.domains.post.service;

import static cotato.backend.domains.post.exception.PostErrorCode.*;
import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cotato.backend.common.excel.ExcelUtils;
import cotato.backend.domains.post.dto.PostDTO;
import cotato.backend.domains.post.dto.PostListDTO;
import cotato.backend.domains.post.entity.Post;
import cotato.backend.domains.post.exception.PostException;
import cotato.backend.domains.post.repository.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostService {

	private final PostRepository postRepository;

	private static final int PAGE_SIZE = 10;

	// 로컬 파일 경로로부터 엑셀 파일을 읽어 Post 엔터티로 변환하고 저장
	@CacheEvict(value = "postList", allEntries = true, cacheManager = "cacheManager")
	@Transactional
	public void saveEstatesByExcel(final String filePath) {

		try {
			// 엑셀 파일을 읽어 데이터 프레임 형태로 변환
			List<Post> postList = ExcelUtils.parseExcelFile(filePath).stream()
				.map(PostDTO::toPostDTO)
				.map(Post::toPostEntity)
				.collect(toList());

			// 게시글 목록 저장
			postRepository.saveAllPostsByJdbcTemplate(postList);

		} catch (Exception e) {
			log.error("Failed to save estates by excel", e);
			throw PostException.from(SAVE_EXCEL_FILE_FAILED);
		}
	}

	// 단일 게시글 저장
	@CacheEvict(value = "postList", allEntries = true, cacheManager = "cacheManager")
	@Transactional
	public void savePost(final PostDTO postDTO) {
		postRepository.save(Post.toPostEntity(postDTO));
	}

	// 단일 게시글 조회
	@Transactional
	public PostDTO getPostById(final long id) {

		// 게시글이 존재하지 않을 경우 예외 처리
		Post post = postRepository.findByIdForUpdate(id).orElseThrow(() -> PostException.from(POST_NOT_FOUND));

		// 조회수 증가
		post.increaseViews();

		// DTO로 변환하여 반환
		return PostDTO.toPostDTO(post);
	}

	// 게시글 목록 조회, 조회수 내림차순 정렬
	@Transactional(readOnly = true)
	public PostListDTO getPostPreviewList(final int page) {

		Pageable pageable = getPageable(page);

		return PostListDTO.toPostListDTO(postRepository.findAllByOrderByViewsDesc(pageable));
	}

	// 게시글 목록 조회, 조회수 내림차순 정렬, Redis 캐시 적용
	@Cacheable(value = "postList", key = "#page", cacheManager = "cacheManager")
	@Transactional(readOnly = true)
	public PostListDTO getPostPreviewListWithCache(final int page) {

		Pageable pageable = getPageable(page);

		return PostListDTO.toPostListDTO(postRepository.findAllByOrderByViewsDesc(pageable));
	}

	// 단일 게시글 삭제
	@CacheEvict(value = "postList", allEntries = true, cacheManager = "cacheManager")
	@Transactional
	public void deletePost(final long id) {
		if (!postRepository.existsById(id)) {
			throw PostException.from(POST_NOT_FOUND);
		}
		postRepository.deleteById(id);
	}

	// 페이징 정보 반환
	protected Pageable getPageable(final int page) {
		return Pageable.ofSize(PAGE_SIZE).withPage(page);
	}
}
