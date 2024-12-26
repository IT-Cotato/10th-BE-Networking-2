package cotato.backend.domains.post.entity;

import cotato.backend.domains.post.dto.PostDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String name;

    @Column
    private long views;

    @Version
    private Long version;

    private Post(
            final String title,
            final String content,
            final String name,
            final long views
    ) {
        this.title = title;
        this.content = content;
        this.name = name;
        this.views = views;
    }

    public static Post toPost(final PostDTO postDTO) {
        return new Post(
                postDTO.title(),
                postDTO.content(),
                postDTO.name(),
                postDTO.views()
        );
    }
}
