package cotato.backend.domains.post.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name= "post", indexes = {@Index(name = "idx_likes", columnList = "likes DESC")})
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "post_title", nullable = false)
    private String title;

    @Column(name = "post_content", nullable = false)
    private String content;

    @Column(name = "post_writer", nullable = false)
    private String name;

    @Column(name = "view", nullable = false)
    private Long views = 0L;

    @Column(name = "likes", nullable = false)
    private Long likes = 0L;

    public Post(String title, String content, String name) {
        this.title = title;
        this.content = content;
        this.name = name;
    }
}
