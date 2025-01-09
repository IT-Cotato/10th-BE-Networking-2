package cotato.backend.domains.post.service;

import static cotato.backend.common.exception.ErrorCode.*;

import cotato.backend.api.dto.response.PagedPostResponse;
import cotato.backend.api.dto.response.PostInfoResponse;
import cotato.backend.domains.repository.PostJdbcRepository;
import cotato.backend.domains.repository.PostRepository;
import cotato.backend.domains.post.entity.Post;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cotato.backend.common.excel.ExcelUtils;
import cotato.backend.common.exception.ApiException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class PostService {
    private static final int PAGE_SIZE = 10;
    public static final int PAGE_INDEX_OFFSET = 1;

    private final PostRepository postRepository;
    private final PostJdbcRepository postJdbcRepository;
    private final AsyncService asyncService;

    // 로컬 파일 경로로부터 엑셀 파일을 읽어 Post 엔터티로 변환하고 저장
    @Transactional
    public void saveEstatesByExcel(String filePath) {
        try {
            // 엑셀 파일을 읽어 데이터 프레임 형태로 변환
            List<Post> posts = ExcelUtils.parseExcelFile(filePath).stream()
                    .map(row -> {
                        String title = row.get("title");
                        String content = row.get("content");
                        String name = row.get("name");

                        return new Post(title, content, name);
                    })
                    .toList();

            postJdbcRepository.saveAllWithBatch(posts);
        } catch (Exception e) {
            log.error("Failed to save estates by excel", e);
            throw ApiException.from(INTERNAL_SERVER_ERROR);
        }
    }

    public PostInfoResponse findPostById(long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> ApiException.from(POST_NOT_FOUND));
        asyncService.increaseViews(post);
        return PostInfoResponse.from(post);
    }

    @Transactional
    public void savePost(String title, String content, String name) {
        postRepository.save(new Post(title, content, name));
    }

    public PagedPostResponse findPostsByLikes(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Post> postPage = postRepository.findAllByOrderByLikesDesc(pageable);

        return PagedPostResponse.of(postPage.getContent(),
                postPage.getNumber() + PAGE_INDEX_OFFSET,
                postPage.getTotalPages()
        );
    }

    @Transactional
    public void deletePostById(long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> ApiException.from(POST_NOT_FOUND));
        postRepository.delete(post);
    }
}
