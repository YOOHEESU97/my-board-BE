package my_board.board.service;

import lombok.RequiredArgsConstructor;
import my_board.board.dto.PostDto;
import my_board.board.dto.PostUpdateDto;
import my_board.board.entity.Post;
import my_board.board.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void createPost(PostDto dto) {
        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .build();

        postRepository.save(post);
    }

    public void update(Long id, PostUpdateDto dto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        // 저장 생략해도 JPA 영속성 컨텍스트가 자동 반영함
    }
}

