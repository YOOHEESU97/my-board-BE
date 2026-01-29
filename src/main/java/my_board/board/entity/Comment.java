package my_board.board.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 댓글(Comment) 엔티티
 * 게시글에 달린 댓글 및 대댓글(답글) 정보를 저장하는 테이블과 매핑
 * 
 * @Entity: JPA 엔티티로 지정
 * @Table: 테이블명을 "comments"로 명시적 지정
 * @Getter/@Setter: Lombok을 통한 getter/setter 자동 생성
 * @NoArgsConstructor(access = AccessLevel.PROTECTED): 
 *   protected 기본 생성자 생성 (JPA 요구사항 충족 + 외부 직접 생성 방지)
 * @AllArgsConstructor: 모든 필드를 포함하는 생성자 자동 생성
 * @Builder: 빌더 패턴 지원 (객체 생성 편의성)
 */
@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment {
    /**
     * 댓글 고유 식별자 (Primary Key)
     * 데이터베이스에서 자동 증가(AUTO_INCREMENT)로 생성
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 댓글이 속한 게시글 (Many-to-One 관계)
     * - LAZY 로딩: 댓글 조회 시 게시글은 필요할 때만 로딩
     * - nullable = false: 댓글은 반드시 게시글에 속해야 함
     * 
     * 외래키: post_id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    /**
     * 댓글 작성자 (Many-to-One 관계)
     * - LAZY 로딩: 댓글 조회 시 사용자는 필요할 때만 로딩
     * - nullable = false: 댓글은 반드시 작성자가 있어야 함
     * 
     * 외래키: user_id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 부모 댓글 (대댓글인 경우, Self-Referencing Many-to-One)
     * - LAZY 로딩: 필요할 때만 부모 댓글 로딩
     * - nullable = true: 최상위 댓글인 경우 부모가 없음
     * 
     * 외래키: parent_id
     * null이면 최상위 댓글, 값이 있으면 대댓글(답글)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    /**
     * 댓글 내용
     * 최대 500자로 제한
     */
    @Column(nullable = false, length = 500)
    private String content;

    /**
     * 댓글 작성 시간
     * LocalDateTime 타입으로 저장 (년-월-일 시:분:초)
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * 댓글 삭제 여부
     * true면 삭제된 댓글, false면 정상 댓글
     * 기본값: false
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean deleted = false;

    /**
     * 댓글 생성을 위한 정적 팩토리 메서드
     * 생성자 대신 의미있는 메서드명으로 객체 생성
     * 
     * @param post    댓글이 속한 게시글
     * @param user    댓글 작성자
     * @param parent  부모 댓글 (대댓글인 경우, 없으면 null)
     * @param content 댓글 내용
     * @return 생성된 Comment 객체
     */
    public static Comment create(Post post, User user, Comment parent, String content) {
        return Comment.builder()
                .post(post)
                .user(user)
                .parent(parent)
                .content(content)
                .createdAt(LocalDateTime.now())
                .deleted(false)
                .build();
    }

    /**
     * 댓글을 삭제 처리 (Soft Delete)
     * 실제로 DB에서 삭제하지 않고, 내용을 "삭제 처리 된 댓글입니다."로 변경하고 deleted 플래그를 true로 설정
     */
    public void markAsDeleted() {
        this.content = "삭제 처리 된 댓글입니다.";
        this.deleted = true;
    }
}
