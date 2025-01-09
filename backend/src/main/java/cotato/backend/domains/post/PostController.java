package cotato.backend.domains.post;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cotato.backend.common.dto.DataResponse;
import cotato.backend.domains.post.dto.request.SavePostRequest;
import cotato.backend.domains.post.dto.request.SavePostsByExcelRequest;
import cotato.backend.domains.post.dto.response.PostDetailResponse;
import cotato.backend.domains.post.dto.response.PostListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@PostMapping("/excel")
	@Operation(summary = "게시글 다중 생성 API")
	public ResponseEntity<DataResponse<Void>> savePostsByExcel(@RequestBody SavePostsByExcelRequest request) {
		postService.saveEstatesByExcel(request.getPath());

		return ResponseEntity.ok(DataResponse.ok());
	}

	@PostMapping("")
	@Operation(summary = "게시글 단일 생성 API")
	public ResponseEntity<DataResponse<Void>> savePost(@RequestBody SavePostRequest request){
		postService.savePost(request);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@GetMapping("/{postId}")
	@Operation(summary = "게시글 조회 API")
	public ResponseEntity<DataResponse<PostDetailResponse>> getPost(@PathVariable Long postId){
		return ResponseEntity.ok(DataResponse.from(postService.getPost(postId)));
	}

	@GetMapping("")
	@Operation(summary = "게시글 목록 인기순으로 조회 API")
	@Parameter(name = "page", description = "페이지 번호를 입력해주세요.")
	public ResponseEntity<DataResponse<PostListResponse.PostPreviewList>> getPostList(@RequestParam(name = "page") Integer page){

		return ResponseEntity.ok(DataResponse.from(postService.getPostList(page)));
	}

	@DeleteMapping("/{postId}")
	@Operation(summary = "게시글 삭제 API")
	public ResponseEntity<DataResponse<String>> deletePost(@PathVariable Long postId){
		postService.deletePost(postId);
		return ResponseEntity.ok(DataResponse.from("게시글이 성공적으로 삭제되었습니다."));
	}
}
