package cotato.backend.domains.post;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cotato.backend.common.dto.DataResponse;
import cotato.backend.domains.post.dto.PostConcept;
import cotato.backend.domains.post.dto.request.NewPostRequest;
import cotato.backend.domains.post.dto.request.SavePostsByExcelRequest;
import cotato.backend.domains.post.dto.response.PostListFindResponse;
import cotato.backend.domains.post.dto.response.SinglePostResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "게시글 API", description = "게시글 관련 API")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	// 게시글 단일 조회 기능
	@GetMapping("/{postId}")
	@Operation(summary = "게시글 단건 조회", description = "postId를 바탕으로 게시글 하나에 대한 정보를 조회합니다")
	public ResponseEntity<DataResponse<SinglePostResponse>> findSinglePostById(@PathVariable Long postId) {
		PostConcept singlePost = postService.findSinglePost(postId);
		return ResponseEntity.ok(DataResponse.from(SinglePostResponse.from(singlePost)));
	}

	// 게시글 단일 생성 기능
	@PostMapping("/single")
	@Operation(summary = "게시글 단건 생성", description = "SinglePostCreateRequest를 통해 게시글을 하나 생성합니다.")
	public ResponseEntity<DataResponse<?>> createSinglePost(@RequestBody @Valid NewPostRequest request) {
		postService.createSinglePost(request.content(), request.title(), request.name());
		return ResponseEntity.ok(DataResponse.ok());
	}

	@GetMapping()
	@Operation(summary = "게시글 리스트 조회(페이징, 조회수 순 정렬)", description = "전체 게시글을 조회수 순으로 정렬한 후 페이징하여 조회합니다.(기본 페이지: 0, 페이지 별 기본 게시글 수: 10)")
	public ResponseEntity<DataResponse<PostListFindResponse>> findPostListSortByViews(
		@PageableDefault(page = 0, size = 10) Pageable pageable) {
		List<PostConcept> posts = postService.findPostListSortByViews(pageable);
		return ResponseEntity.ok(DataResponse.from(PostListFindResponse.from(posts)));
	}

	@GetMapping("/redis")
	@Operation(summary = "게시글 리스트 조회(페이징, 조회수 순 정렬), redis cache 적용", description = "전체 게시글을 조회수 순으로 정렬한 후 페이징하여 조회합니다.(기본 페이지: 0, 페이지 별 기본 게시글 수: 10)")
	public ResponseEntity<DataResponse<PostListFindResponse>> findHotPostsRedis(
		@PageableDefault(page = 0, size = 10) Pageable pageable) {
		List<PostConcept> posts = postService.findHotPostListRedis(pageable);
		return ResponseEntity.ok(DataResponse.from(PostListFindResponse.from(posts)));
	}

	@GetMapping("/virtual-thread")
	@Operation(summary = "게시글 리스트 조회(페이징, 조회수 순 정렬), virtual thread 적용", description = "전체 게시글을 조회수 순으로 정렬한 후 페이징하여 조회합니다.(기본 페이지: 0, 페이지 별 기본 게시글 수: 10)")
	public CompletableFuture<ResponseEntity<DataResponse<PostListFindResponse>>> getHotPosts(
		@PageableDefault(page = 0, size = 10) Pageable pageable) {
		return postService.findHotPostListVirtualThread(pageable)
			.thenApply(posts -> ResponseEntity.ok(DataResponse.from(PostListFindResponse.from(posts))));
	}

	@PostMapping("/excel")
	@Operation(summary = "게시글 다중 생성(엑셀)", description = "SavePostsByExcelRequest(엑셀 파일의 경로)를 통해 다중 게시글을 한 번에 생성합니다.")
	public ResponseEntity<DataResponse<Void>> savePostsByExcel(@RequestBody @Valid SavePostsByExcelRequest request) {
		postService.saveEstatesByExcel(request.getPath());
		return ResponseEntity.ok(DataResponse.ok());
	}

	@PostMapping("/excel/virtual-thread")
	@Operation(summary = "게시글 다중 생성(엑셀), virtual thread 사용", description = "SavePostsByExcelRequest(엑셀 파일의 경로)를 통해 다중 게시글을 한 번에 생성합니다.")
	public ResponseEntity<DataResponse<Void>> savePostsByExcelWithVirtualThread(
		@RequestBody @Valid SavePostsByExcelRequest request) {
		postService.saveEstatesByExcelWithVirtualThread(request.getPath());
		return ResponseEntity.ok(DataResponse.ok());
	}

	@DeleteMapping("/{postId}")
	@Operation(summary = "게시글 단건 삭제", description = "postId를 통해 게시글을 하나 삭제합니다.")
	public ResponseEntity<DataResponse<Void>> deletePostById(@PathVariable Long postId) {
		postService.deletePostById(postId);
		return ResponseEntity.ok(DataResponse.ok());
	}
}
