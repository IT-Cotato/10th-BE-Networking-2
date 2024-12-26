package cotato.backend.domains.post.dto;

import cotato.backend.domains.post.entity.Post;
import cotato.backend.domains.post.dto.request.SavePostRequest;
import java.util.Map;

public record PostDTO (
        Long id,
        String title,
        String content,
        String name,
        Long views
) {
    private static final String TITLE = "title";
    private static final String CONTENT = "content";
    private static final String NAME = "name";
    private static final long DEFAULT_VIEWS = 0L;

    public static PostDTO toPostDTO(final Map<String, String> parsedData) {
        return new PostDTO(
                null,
                parsedData.get(TITLE),
                parsedData.get(CONTENT),
                parsedData.get(NAME),
                DEFAULT_VIEWS
        );
    }

    public static PostDTO toPostDTO(final SavePostRequest savePostRequest) {
        return new PostDTO(
                null,
                savePostRequest.title(),
                savePostRequest.content(),
                savePostRequest.name(),
                DEFAULT_VIEWS
        );
    }

    public static PostDTO toPostDTO(final Post post) {
        return new PostDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getName(),
                post.getViews()
        );
    }
}
