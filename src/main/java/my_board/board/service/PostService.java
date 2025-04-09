package my_board.board.service;

import lombok.RequiredArgsConstructor;
import my_board.board.dto.PostDto;
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
}

