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
import cotato.backend.domains.post.dto.PostDTO;
import cotato.backend.domains.post.dto.PostListDTO;
import cotato.backend.domains.post.dto.request.SavePostRequest;
import cotato.backend.domains.post.dto.request.SavePostsByExcelRequest;
import cotato.backend.domains.post.dto.response.GetPostPreviewListResponse;
import cotato.backend.domains.post.dto.response.GetPostResponse;
import cotato.backend.domains.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	// 엑셀 파일로부터 게시글 저장 API
	@Operation(summary = "엑셀 파일로부터 게시글 저장", description = "엑셀 파일로부터 게시글을 저장합니다.")
	@PostMapping("/excel")
	public ResponseEntity<DataResponse<Void>> savePostsByExcel(
		final @RequestBody @Valid SavePostsByExcelRequest request) {
		postService.saveEstatesByExcel(request.getPath());

		return ResponseEntity.ok(DataResponse.ok());
	}

	// 단일 게시글 저장 API
	@Operation(summary = "게시글 저장", description = "단일 게시글을 저장합니다.")
	@PostMapping
	public ResponseEntity<DataResponse<Void>> createPost(final @RequestBody @Valid SavePostRequest request) {

		postService.savePost(PostDTO.toPostDTO(request));

		return ResponseEntity.ok(DataResponse.ok());
	}

	// 단일 게시글 조회 API
	@Operation(summary = "게시글 조회", description = "게시글을 조회합니다.")
	@GetMapping("/{id}")
	public ResponseEntity<DataResponse<GetPostResponse>> getPostById(final @PathVariable("id") long id) {

		final PostDTO postDTO = postService.getPostById(id);

		return ResponseEntity.ok(DataResponse.from(GetPostResponse.toGetPostResponse(postDTO)));
	}

	// 게시글 목록 조회 API
	@Operation(summary = "게시글 목록 조회", description = "게시글 목록을 조회합니다.")
	@GetMapping
	public ResponseEntity<DataResponse<GetPostPreviewListResponse>> getPosts(
		final @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int page) {

		PostListDTO postListDTO = postService.getPostPreviewListWithCache(page);

		return ResponseEntity.ok(
			DataResponse.from(GetPostPreviewListResponse.toGetPostPreviewListResponse(postListDTO)));

	}

	// 단일 게시글 삭제 API
	@Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
	@DeleteMapping("/{id}")
	public ResponseEntity<DataResponse<Void>> deletePost(final @PathVariable("id") long id) {
		postService.deletePost(id);
		return ResponseEntity.ok(DataResponse.ok());
	}
}
