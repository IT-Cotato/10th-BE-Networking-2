package cotato.backend.domains.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import cotato.backend.domains.post.dto.request.SavePostRequest;
import cotato.backend.domains.post.dto.request.SavePostsByExcelRequest;
import cotato.backend.domains.post.dto.response.FindPostResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@PostMapping
	public ResponseEntity<DataResponse<Void>> savePost(@RequestBody @Valid SavePostRequest request){
		postService.savePost(request);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@PostMapping("/excel")
	public ResponseEntity<DataResponse<Void>> savePostsByExcel(@RequestBody SavePostsByExcelRequest request) {
		postService.saveEstatesByExcel(request.getPath());

		return ResponseEntity.ok(DataResponse.ok());
	}

	@GetMapping("/{id}")
	public ResponseEntity<FindPostResponse> findPost(@PathVariable Long id) {
		return ResponseEntity.ok(postService.findPostById(id));
	}

	@GetMapping("/list")
	public ResponseEntity<?> findPosts(@PageableDefault(size = 10, sort = "views", direction = Sort.Direction.DESC)
	Pageable pageable) {
		return ResponseEntity.ok(DataResponse.from(postService.findPostsWithPaging(pageable)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<DataResponse<String>> deletePost(@PathVariable Long id){
		postService.deletePostById(id);

		return ResponseEntity.ok(DataResponse.from("게시글이 성공적으로 삭제되었습니다."));
	}
}
