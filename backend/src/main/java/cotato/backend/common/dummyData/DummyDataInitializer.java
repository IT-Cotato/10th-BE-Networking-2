package cotato.backend.common.dummyData;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import cotato.backend.domains.post.entity.Post;
import cotato.backend.domains.post.repository.PostJDBCRepository;
import cotato.backend.domains.post.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class DummyDataInitializer {

	private final int BATCH_SIZE = 5000;

	@Bean
	CommandLineRunner initData(PostRepository postRepository, PostJDBCRepository postJDBCRepository) {
		// postRepository가 비어있으면 데이터를 초기화한다.
		return args -> {
			if (postRepository.count() == 0) {
				initializeData(postJDBCRepository);
			}
		};
	}

	@Transactional
	public void initializeData(PostJDBCRepository postJDBCRepository) {
		List<Post> posts = new ArrayList<>();
		for (int i = 1; i <= 500000; i++) {
			// Post 엔터티를 생성하고 저장
			Post post = new Post("title" + i, "content" + i, "name" + i);
			posts.add(post);

			// 배치 크기만큼 모이면 저장
			if (i % BATCH_SIZE == 0) {
				postJDBCRepository.saveAll(posts); // JDBC를 통한 저장, 영속성 컨텍스트에 데이터가 쌇이는 것을 방지.
				posts.clear(); // 리스트 비우기, out of memory 방지
			}
		}
		// 남아있는 데이터 저장
		if (!posts.isEmpty()) {
			postJDBCRepository.saveAll(posts);
			posts.clear();
		}
	}
}
