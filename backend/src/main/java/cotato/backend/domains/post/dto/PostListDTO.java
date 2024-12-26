package cotato.backend.domains.post.dto;

import cotato.backend.domains.post.entity.Post;
import java.util.List;
import org.springframework.data.domain.Page;

public record PostListDTO(
        int currentPage,
        int totalPage,
        List<PostDTO> postDTOs
) {
    public static PostListDTO toPostListDTO(final Page<Post> posts) {
        List<PostDTO> postDTOS = posts.get()
                .map(PostDTO::toPostDTO)
                .toList();

        return new PostListDTO(
                posts.getNumber(),
                posts.getTotalPages(),
                postDTOS
        );
    }
}
