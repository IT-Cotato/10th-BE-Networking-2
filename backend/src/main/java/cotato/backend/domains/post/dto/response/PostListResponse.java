package cotato.backend.domains.post.dto.response;

import cotato.backend.domains.post.dto.PostListDTO;
import java.util.List;

public record PostListResponse(
        int currentPage,
        int totalPage,
        List<PostResponse> postResponses
) {
    public static PostListResponse toPostListResponse(final PostListDTO postListDTO) {
        List<PostResponse> postResponses = postListDTO.postDTOs().stream()
                .map(PostResponse::toPostResponse)
                .toList();

        return new PostListResponse(
                postListDTO.currentPage(),
                postListDTO.totalPage(),
                postResponses
        );
    }
}
