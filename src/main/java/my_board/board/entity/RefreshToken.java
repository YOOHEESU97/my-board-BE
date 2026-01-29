package my_board.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

/**
 * Refresh Token 엔티티
 * JWT Refresh Token을 데이터베이스에 저장하여 관리
 * Access Token 재발급 시 검증에 사용
 * 
 * @Entity: JPA 엔티티로 지정
 * @Getter: Lombok을 통한 getter 자동 생성
 * @NoArgsConstructor(access = AccessLevel.PROTECTED): 
 *   protected 기본 생성자 생성 (JPA 요구사항 충족 + 외부 직접 생성 방지)
 * @AllArgsConstructor: 모든 필드를 포함하는 생성자 자동 생성
 * @Builder: 빌더 패턴 지원 (객체 생성 편의성)
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RefreshToken {

    /**
     * 사용자 이메일 (Primary Key)
     * 사용자당 하나의 Refresh Token만 유지하도록 이메일을 PK로 사용
     * 새 토큰 발급 시 기존 토큰을 덮어씀
     */
    @Id
    private String email;

    /**
     * Refresh Token 문자열
     * JWT 형식으로 저장되며, 유효 기간은 토큰 내부에 포함
     */
    @Column(nullable = false)
    private String token;
}
