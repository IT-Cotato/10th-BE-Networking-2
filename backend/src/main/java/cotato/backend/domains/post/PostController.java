package cotato.backend.domains.post;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import cotato.backend.domains.post.dto.request.SavePostsByExcelRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@PostMapping("/excel")
	public ResponseEntity<DataResponse<Long>> savePostsByExcel(@RequestBody SavePostsByExcelRequest request) {
		Long startTime = System.currentTimeMillis();
		postService.saveEstatesByExcel(request.getPath());
		Long endTime = System.currentTimeMillis();
		Long elapsedTime = endTime - startTime;
		return ResponseEntity.ok(DataResponse.from(elapsedTime));
	}

	@PostMapping("/single")
	public ResponseEntity<DataResponse<Void>> savePostBySingle(@Valid@RequestBody PostDTO postDTO){
		postService.savePostBySingle(postDTO);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Post> readPostBySingle(@PathVariable Long id)  {
		Post post = postService.readPostBySingle(id);

		return ResponseEntity.ok(post);
	}
	@GetMapping("/views")
	public ResponseEntity<Map<String, Object>> readByView(@RequestParam(defaultValue = "0") int page){
		int size = 10;
		Map<String, Object> response;
		response = postService.getPosts(page, size);

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<DataResponse<String>> deletePostBySingle(@PathVariable Long id) {
		postService.deletePostBySingle(id);

		return ResponseEntity.ok(DataResponse.success("게시글이 성공적으로 삭제 되었습니다."));
	}


}
