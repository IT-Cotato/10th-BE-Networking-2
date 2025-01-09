package cotato.backend.api.dto.response;

import cotato.backend.domains.post.entity.Post;

public record PostInfoResponse(
        String title,
        String content,
        String writer,
        Long views,
        Long likes
) {
    public static PostInfoResponse from(Post post) {
        return new PostInfoResponse(
                post.getTitle(),
                post.getContent(),
                post.getName(),
                post.getViews(),
                post.getLikes()
        );
    }
}
