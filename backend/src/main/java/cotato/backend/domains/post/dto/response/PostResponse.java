package cotato.backend.domains.post.dto.response;

import cotato.backend.domains.post.dto.PostDTO;

public record PostResponse(
        long id,
        String title,
        String name
) {
    public static PostResponse toPostResponse(final PostDTO postDTO) {
        return new PostResponse(
                postDTO.id(),
                postDTO.title(),
                postDTO.name()
        );
    }
}
