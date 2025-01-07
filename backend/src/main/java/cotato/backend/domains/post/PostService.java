package cotato.backend.domains.post;

import static cotato.backend.common.exception.ErrorCode.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
@Transactional
public class PostService {


	private final PostRepository postRepository;
	private final BatchRepository batchRepository;

	// 로컬 파일 경로로부터 엑셀 파일을 읽어 Post 엔터티로 변환하고 저장
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
			batchRepository.insert(posts);

		} catch (Exception e) {
			log.error("Failed to save estates by excel", e);
			throw ApiException.from(INTERNAL_SERVER_ERROR);
		}
	}
	@Transactional(readOnly = true)
	public void savePostBySingle(PostDTO postDTO) {
		Post post = new Post(postDTO.getTitle(), postDTO.getContent(), postDTO.getName());
		postRepository.save(post);
	}

	public Post readPostBySingle(Long id) {
		Post post = postRepository.findByIdForUpdate(id).orElseThrow(() -> ApiException.of(HttpStatus.NOT_FOUND, "해당 ID의 게시글을 핮을 수 없습니다", ""));
		post.update(1);

		return post;
	}
	@Transactional(readOnly = true)
	public Map<String, Object> getPosts(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Post> postPage =  postRepository.findAllByOrderByViewsDesc(pageable);

		List<Map<String, Object>> filteredPosts = postPage.getContent().stream().map(post -> {
			Map<String, Object> postMap = new HashMap<>();
			postMap.put("id", post.getId());
			postMap.put("title", post.getTitle());
			postMap.put("name", post.getName());
			return postMap;
		}).collect(Collectors.toList());

		Map<String, Object> response = new HashMap<>();
		response.put("posts", filteredPosts); // 게시글 리스트
		response.put("currentPage", postPage.getNumber()); // 현재 페이지
		response.put("totalPages", postPage.getTotalPages());

		return response;
	}

	public void deletePostBySingle(Long id) {
		Post post = postRepository.findById(id)
			.orElseThrow(() -> ApiException.of(HttpStatus.NOT_FOUND,"해당 ID의 게시글을 찾을 수 없습니다", ""));
		postRepository.delete(post);

	}

}
