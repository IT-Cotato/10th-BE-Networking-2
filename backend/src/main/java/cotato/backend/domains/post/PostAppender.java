package cotato.backend.domains.post;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PostAppender {

	private final PostRepository postRepository;

	@Transactional
	public void append(String content, String title, String name) {
		Post post = Post.builder()
			.title(title)
			.content(content)
			.name(name)
			.views(0L)
			.build();

		postRepository.save(post);
	}

	@Transactional
	public void appendList(List<Post> posts) {
		postRepository.saveAll(posts);
	}
}