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
 * 
 * @Service: Spring의 서비스 계층 컴포넌트로 등록
 * @RequiredArgsConstructor: final 필드에 대한 생성자 자동 생성 (DI)
 */
@Service
@RequiredArgsConstructor
public class PostService {

    /**
     * 게시글 데이터 접근을 위한 Repository
     */
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
     * 
     * JPA 영속성 컨텍스트의 변경 감지(Dirty Checking) 기능을 활용
     * - @Transactional 내에서 엔티티를 조회하고 필드를 변경하면
     * - 트랜잭션 커밋 시점에 자동으로 UPDATE 쿼리가 실행됨
     * - 명시적인 save() 호출 불필요
     * 
     * @param id  수정할 게시글 ID
     * @param dto 수정할 내용 (제목, 내용)
     * @throws IllegalArgumentException 게시글을 찾을 수 없는 경우
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

