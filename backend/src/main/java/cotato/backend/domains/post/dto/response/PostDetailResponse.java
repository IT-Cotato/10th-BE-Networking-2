package cotato.backend.domains.post.dto.response;

import cotato.backend.domains.post.dto.PostDTO;

public record PostDetailResponse(
        String title,
        String content,
        String name,
        long views
) {
    public static PostDetailResponse toPostDetailResponse(final PostDTO postDTO) {
        return new PostDetailResponse(
                postDTO.title(),
                postDTO.content(),
                postDTO.name(),
                postDTO.views()
        );
    }
}
