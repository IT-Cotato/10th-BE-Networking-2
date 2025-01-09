package cotato.backend.domains.post.service;

import cotato.backend.domains.repository.PostRepository;
import cotato.backend.domains.post.entity.Post;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional
public class AsyncService {
    private final PostRepository postRepository;

    @Async
    public void increaseViews(Post post) {
        postRepository.increaseView(post.getId());
    }
}
