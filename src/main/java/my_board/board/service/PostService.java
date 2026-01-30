package my_board.board.service;

import lombok.RequiredArgsConstructor;
import my_board.board.dto.PostDto;
import my_board.board.dto.PostUpdateDto;
import my_board.board.entity.Post;
import my_board.board.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 게시글 관련 비즈니스 로직을 처리하는 서비스
 * - 게시글 작성
 * - 게시글 수정
 */
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    /**
     * 게시글 작성
     * 
     * @param dto 게시글 작성 정보 (제목, 내용, 작성자 이메일, 닉네임)
     */
    public void createPost(PostDto dto) {
        // 게시글 엔티티 생성
        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .build();

        // 데이터베이스에 저장
        postRepository.save(post);
    }

    /**
     * 게시글 수정
     * @param id  수정할 게시글 ID
     * @param dto 수정할 내용 (제목, 내용)
     */
    @Transactional
    public void update(Long id, PostUpdateDto dto) {
        // 게시글 조회 (영속성 컨텍스트에 관리됨)
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 필드 변경 (변경 감지 대상)
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        
        // save() 호출 불필요: JPA 영속성 컨텍스트가 트랜잭션 커밋 시 자동으로 UPDATE 쿼리 실행
    }
}

