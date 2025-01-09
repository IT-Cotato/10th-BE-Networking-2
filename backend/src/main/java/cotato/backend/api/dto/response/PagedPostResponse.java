package cotato.backend.api.dto.response;

import cotato.backend.domains.post.entity.Post;
import java.util.List;

public record PagedPostResponse(
        List<PostInfoResponse> infos,
        Integer currentPage,
        Integer totalPage
) {
    public static PagedPostResponse of(List<Post> posts, int currentPage, int totalPage) {
        List<PostInfoResponse> postInfos = posts.stream()
                .map(PostInfoResponse::from)
                .toList();
        return new PagedPostResponse(postInfos, currentPage, totalPage);
    }
}
