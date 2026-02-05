package my_board.board.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 게시글(Post) 엔티티
 * 사용자가 작성한 게시글 정보를 저장하는 테이블과 매핑
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    /**
     * 게시글 고유 식별자 (Primary Key)
     * 데이터베이스에서 자동 증가(AUTO_INCREMENT)로 생성
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 게시글 제목
     */
    private String title;

    /**
     * 게시글 내용
     * TEXT 타입으로 지정하여 긴 내용도 저장 가능 (최대 65,535자)
     */
    @Column(columnDefinition = "TEXT")
    private String content;

    /**
     * 작성자 이메일
     */
    private String email;

    /**
     * 작성자 닉네임
     */
    private String nickname;

    /**
     * 게시글 작성 시간
     * LocalDateTime 타입으로 저장 (년-월-일 시:분:초)
     */
    private LocalDateTime createAt;

    /**
     * 게시글에 달린 댓글 리스트
     * OneToMany -> post에 여러개 댓글(comment) 달림
     * cascade -> post 지우면 댓글도 지워져야되기때문에 씀
     * orphanRemoval DB에서 삭제됨
     */
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    /**
     * 엔티티가 처음 저장되기 전에 자동으로 호출되는 메서드
     * 작성 시간을 현재 시간으로 자동 설정
     * 
     * @PrePersist: JPA 생명주기 콜백 어노테이션
     */
    @PrePersist
    public void prePersist() {
        this.createAt = LocalDateTime.now();
    }
}