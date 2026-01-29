package my_board.board.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 사용자(User) 엔티티
 * 시스템에 가입한 사용자 정보를 저장하는 테이블과 매핑
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
public class User {

    /**
     * 사용자 고유 식별자 (Primary Key)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 사용자 이메일 (로그인 ID)
     * - 필수 입력 (nullable = false)
     * - 중복 불가 (unique = true)
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * 사용자 비밀번호 (암호화되어 저장)
     * BCrypt로 해시화되어 저장됨
     */
    @Column(nullable = false)
    private String password;

    /**
     * 사용자 닉네임 (화면 표시용)
     * - 필수 입력 (nullable = false)
     * - 중복 불가 (unique = true)
     */
    @Column(nullable = false, unique = true)
    private String nickname;

    /**
     * 사용자 권한 (예: ROLE_USER, ROLE_ADMIN)
     * Spring Security에서 권한 체크에 사용
     */
    @Column(nullable = false)
    private String role;
}