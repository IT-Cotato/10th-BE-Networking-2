package cotato.backend.api;

import cotato.backend.api.dto.request.SavePostRequest;
import cotato.backend.api.dto.response.PagedPostResponse;
import cotato.backend.api.dto.response.PostInfoResponse;
import cotato.backend.domains.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import cotato.backend.api.dto.request.SavePostsByExcelRequest;
import lombok.RequiredArgsConstructor;

@Tag(name = "게시물 관련 API", description = "게시물 관련 API 모음")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private static final String DEFAULT_PAGE_INDEX = "0";

    private final PostService postService;

    @Operation(summary = "엑셀 파일을 이용한 다중 게시물 생성 API")
    @PostMapping("/excel")
    public ResponseEntity<DataResponse<Void>> savePostsByExcel(@Valid @RequestBody SavePostsByExcelRequest request) {
        postService.saveEstatesByExcel(request.path());

        return ResponseEntity.ok(DataResponse.ok());
    }

    @Operation(summary = "좋아요 순으로 페이징 게시 정보 반환 API")
    @GetMapping
    public ResponseEntity<DataResponse<PagedPostResponse>> getPosts(
            @RequestParam(defaultValue = DEFAULT_PAGE_INDEX) int page) {
        PagedPostResponse response = postService.findPostsByLikes(page);

        return ResponseEntity.ok(DataResponse.from(response));
    }

    @Operation(summary = "단건 게시물 생성 API")
    @PostMapping
    public ResponseEntity<DataResponse<Void>> savePost(@Valid @RequestBody SavePostRequest request) {
        postService.savePost(request.title(), request.content(), request.name());

        return ResponseEntity.ok(DataResponse.ok());
    }

    @Operation(summary = "단건 게시물 정보 반환 API")
    @GetMapping("/{post_id}")
    public ResponseEntity<DataResponse<PostInfoResponse>> findPostById(@PathVariable(name = "post_id") long postId) {
        PostInfoResponse postInfo = postService.findPostById(postId);

        return ResponseEntity.ok(DataResponse.from(postInfo));
    }

    @Operation(summary = "단건 게시물 삭제 API")
    @DeleteMapping("/{post_id}")
    public ResponseEntity<DataResponse<Void>> deletePost(@PathVariable(name = "post_id") long postId) {
        postService.deletePostById(postId);

        return ResponseEntity.ok(DataResponse.ok());
    }
}
