package cotato.backend.domains.post.facade;

import cotato.backend.domains.post.dto.PostDTO;
import cotato.backend.domains.post.dto.PostListDTO;
import cotato.backend.domains.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostFacade {
    private final PostService postService;

    public void saveEstatesByExcel(final String filePath) {
        postService.saveEstatesByExcel(filePath);
    }

    public void savePost(final PostDTO postDTO) {
        postService.savePost(postDTO);
    }

    public synchronized PostDTO findPostById(final long id) {
        return postService.findPostById(id);
    }

    public PostDTO findPostByIdWithOptimisticLock(final long id) {
        try {
            return postService.findPostByIdWithOptimisticLock(id);
        } catch (final OptimisticLockingFailureException e) {
            return findPostByIdWithOptimisticLock(id);
        }
    }

    public void deletePostById(final long id) {
        postService.deletePostById(id);
    }

    public PostListDTO getPosts(final int page) {
        return postService.getPosts(page);
    }
}
