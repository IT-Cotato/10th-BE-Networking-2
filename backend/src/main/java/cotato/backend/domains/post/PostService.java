package cotato.backend.domains.post;

import static cotato.backend.common.exception.ErrorCode.*;

import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cotato.backend.common.excel.ExcelUtils;
import cotato.backend.common.exception.ApiException;
import cotato.backend.common.exception.ErrorCode;
import cotato.backend.domains.post.dto.request.SavePostRequest;
import cotato.backend.domains.post.dto.response.PostDetailResponse;
import cotato.backend.domains.post.dto.response.PostListResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostService {

	private final PostRepository postRepository;

	// 로컬 파일 경로로부터 엑셀 파일을 읽어 Post 엔터티로 변환하고 저장
	public void saveEstatesByExcel(String filePath) {
		try {

			// 엑셀 파일을 읽어 데이터 프레임 형태로 변환
			int batchSize = 5000; // 배치 크기 설정

			List<Post> posts = ExcelUtils.parseExcelFile(filePath).stream()
				.map(row -> {
					String title = row.get("title");
					String content = row.get("content");
					String name = row.get("name");

					return new Post(title, content, name);
				})
				.collect(Collectors.toList());

			for(int i = 0; i < posts.size(); i+= batchSize) {
				List<Post> batch = posts.subList(i, Math.min(posts.size(), i + batchSize));
				postRepository.saveAll(batch);
			}

		} catch (Exception e) {
			log.error("Failed to save estates by excel", e);
			throw ApiException.from(INTERNAL_SERVER_ERROR);
		}
	}

	public void savePost(SavePostRequest request){
		postRepository.save(Post.toPost(request));
	}

	public synchronized PostDetailResponse getPost(Long postId){
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> ApiException.from(ErrorCode.POST_NOT_FOUND));

		post.incrementViews();
		postRepository.save(post);

		return PostDetailResponse.builder()
			.title(post.getTitle())
			.content(post.getContent())
			.name(post.getName())
			.views(post.getViews())
			.build();
	}

	public PostListResponse.PostPreviewList getPostList(Integer page){
		long startTime = System.currentTimeMillis(); // 시작 시간 기록

		PageRequest pageRequest = PageRequest.of(page -1 , 10, Sort.by(Sort.Order.desc("views"), Sort.Order.asc("id")));
		Page<Post> postPage = postRepository.findAll(pageRequest);

		List<PostListResponse.PostPreview> postPreviewList = postPage.stream()
			.map(post -> PostListResponse.PostPreview.builder()
				.id(post.getId())
				.title(post.getTitle())
				.name(post.getName())
				.views(post.getViews())
				.build()
			).collect(Collectors.toList());

		PostListResponse.PostPreviewList response = PostListResponse.PostPreviewList.builder()
			.currentPage(postPage.getNumber() + 1)
			.totalPage(postPage.getTotalPages())
			.postList(postPreviewList)
			.build();

		long endTime = System.currentTimeMillis(); // 끝난 시간 기록

		// 실행 시간 출력
		System.out.println("조회 소요 시간: " + (endTime - startTime) + "ms");

		return response;
	}

	public void deletePost(Long postId){
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> ApiException.from(ErrorCode.POST_NOT_FOUND));
		postRepository.delete(post);
	}

}
