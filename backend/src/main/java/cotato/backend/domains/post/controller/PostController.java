package cotato.backend.domains.post.controller;

import cotato.backend.domains.post.dto.PostDTO;
import cotato.backend.domains.post.dto.PostListDTO;
import cotato.backend.domains.post.dto.request.SavePostRequest;
import cotato.backend.domains.post.dto.response.PostDetailResponse;
import cotato.backend.domains.post.dto.response.PostListResponse;
import cotato.backend.domains.post.facade.PostFacade;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
import cotato.backend.domains.post.dto.request.SavePostsByExcelRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostFacade postFacade;

	@PostMapping("/excel")
	public ResponseEntity<DataResponse<Void>> savePostsByExcel(final @RequestBody @Valid SavePostsByExcelRequest request) {
		postFacade.saveEstatesByExcel(request.path());

		return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created());
	}

	@GetMapping
	public ResponseEntity<DataResponse<PostListResponse>> getPosts(final @RequestParam(required = false, defaultValue = "0") int page) {
		PostListDTO postListDTO = postFacade.getPosts(page);
		PostListResponse postListResponse = PostListResponse.toPostListResponse(postListDTO);

		return ResponseEntity.ok(DataResponse.from(postListResponse));
	}

	@PostMapping
	public ResponseEntity<DataResponse<Void>> savePost(final @RequestBody @Valid SavePostRequest savePostRequest) {
		postFacade.savePost(PostDTO.toPostDTO(savePostRequest));

		return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created());
	}

	@GetMapping("/{id}")
	public ResponseEntity<DataResponse<PostDetailResponse>> getPost(final @PathVariable("id") long id) {
		final PostDTO postDTO = postFacade.findPostByIdWithOptimisticLock(id);
		final PostDetailResponse postDetailResponse = PostDetailResponse.toPostDetailResponse(postDTO);

		return ResponseEntity.ok(DataResponse.from(postDetailResponse));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<DataResponse<Void>> deletePost(final @PathVariable("id") long id) {
		postFacade.deletePostById(id);

		return ResponseEntity.ok(DataResponse.ok());
	}
}
