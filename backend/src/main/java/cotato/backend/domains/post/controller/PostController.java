package cotato.backend.domains.post.controller;

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
import cotato.backend.common.dto.PageResponse;
import cotato.backend.domains.post.dto.request.SavePostRequest;
import cotato.backend.domains.post.dto.request.SavePostsByExcelRequest;
import cotato.backend.domains.post.dto.response.FindPostByIdResponse;
import cotato.backend.domains.post.dto.response.FindPostsByPopularResponse;
import cotato.backend.domains.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@PostMapping("/excel")
	public ResponseEntity<DataResponse<Void>> savePostsByExcel(@RequestBody @Valid SavePostsByExcelRequest request) {
		postService.saveEstatesByExcel(request.getPath());

		return ResponseEntity.ok(DataResponse.ok());
	}

	@PostMapping
	public ResponseEntity<DataResponse<Void>> savePost(@RequestBody @Valid SavePostRequest request) {
		postService.save(request);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@GetMapping("/{postId}")
	public ResponseEntity<DataResponse<FindPostByIdResponse>> findPostById(@PathVariable Long postId) {
		FindPostByIdResponse response = postService.findPostDtoByIdAndIncreaseView(postId);

		return ResponseEntity.ok(DataResponse.from(response));
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<DataResponse<Void>> deletePostById(@PathVariable Long postId) {
		postService.deletePostById(postId);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@GetMapping
	public ResponseEntity<DataResponse<PageResponse<FindPostsByPopularResponse>>> findPostsByPopular(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		PageResponse<FindPostsByPopularResponse> responses = postService.findPostsByPopular(page, size);

		return ResponseEntity.ok(DataResponse.from(responses));
	}
}
