package my_board.board.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 게시글(Post) 엔티티
 * 사용자가 작성한 게시글 정보를 저장하는 테이블과 매핑
 * 
 * @Entity: JPA 엔티티로 지정
 * @Getter/@Setter: Lombok을 통한 getter/setter 자동 생성
 * @NoArgsConstructor: 기본 생성자 자동 생성 (JPA 요구사항)
 * @AllArgsConstructor: 모든 필드를 포함하는 생성자 자동 생성
 * @Builder: 빌더 패턴 지원 (객체 생성 편의성)
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
     * User 테이블과 연관관계를 맺지 않고 단순 문자열로 저장
     * (성능 최적화 및 단순화를 위한 설계)
     */
    private String email;

    /**
     * 작성자 닉네임
     * 게시글 조회 시 JOIN 없이 바로 표시하기 위해 비정규화
     */
    private String nickname;

    /**
     * 게시글 작성 시간
     * LocalDateTime 타입으로 저장 (년-월-일 시:분:초)
     */
    private LocalDateTime createAt;

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