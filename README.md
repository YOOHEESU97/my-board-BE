# 게시판 애플리케이션 (Board Application)

Spring Boot 기반의 RESTful API 게시판 애플리케이션입니다. JWT 인증, 게시글 CRUD, 댓글/대댓글, 택배 조회 등의 기능을 제공합니다.

## 📋 목차
- [주요 기능](#주요-기능)
- [기술 스택](#기술-스택)
- [프로젝트 구조](#프로젝트-구조)
- [시작하기](#시작하기)
- [API 엔드포인트](#api-엔드포인트)
- [환경 설정](#환경-설정)

## ✨ 주요 기능

### 1. 사용자 관리
- **회원가입**: 이메일, 비밀번호, 닉네임으로 회원가입
- **로그인**: JWT 기반 인증 (Access Token + Refresh Token)
- **토큰 재발급**: Refresh Token을 통한 Access Token 갱신
- **닉네임 중복 확인**: 실시간 닉네임 중복 체크

### 2. 게시글 관리
- **게시글 작성**: 제목, 내용 작성 (인증 필요)
- **게시글 목록 조회**: 최신순 정렬 (공개)
- **게시글 상세 조회**: 개별 게시글 조회 (공개)
- **게시글 수정**: 제목, 내용 수정 (인증 필요)
- **게시글 삭제**: 게시글 삭제 (인증 필요)

### 3. 댓글 관리
- **댓글 작성**: 일반 댓글 및 대댓글 작성 (인증 필요)
- **댓글 목록 조회**: 게시글별 댓글 목록 (공개)

### 4. 기타 기능
- **택배 배송 조회**: Sweet Tracker API를 통한 실시간 배송 정보 조회
- **축구 경기 정보**: RapidAPI를 통한 프리미어리그 경기 일정 조회 (선택사항)

## 🛠 기술 스택

### Backend
- **Java 17**: 프로그래밍 언어
- **Spring Boot 3.4.4**: 애플리케이션 프레임워크
- **Spring Security**: 인증/인가 처리
- **Spring Data JPA**: 데이터베이스 ORM
- **Hibernate**: JPA 구현체

### Database
- **MySQL**: 관계형 데이터베이스

### Security
- **JWT (JSON Web Token)**: 토큰 기반 인증
  - jjwt 0.12.6 라이브러리 사용
  - Access Token (유효기간: 1시간)
  - Refresh Token (유효기간: 7일)
- **BCrypt**: 비밀번호 암호화

### Build Tool
- **Gradle**: 빌드 및 의존성 관리

### Libraries
- **Lombok**: 코드 간소화 (Getter, Setter, Builder 등)
- **Jackson**: JSON 직렬화/역직렬화
- **RestTemplate**: HTTP 통신 (외부 API 호출)

## 📁 프로젝트 구조

```
src/main/java/my_board/board/
├── config/                     # 설정 클래스
│   ├── SecurityConfig.java         # Spring Security 설정
│   ├── JwtAuthenticationFilter.java # JWT 인증 필터
│   └── RestTemplateConfig.java     # RestTemplate 빈 설정
├── controller/                 # REST API 컨트롤러
│   ├── UserController.java         # 사용자 관련 API
│   ├── PostController.java         # 게시글 관련 API
│   ├── CommentController.java      # 댓글 관련 API
│   ├── DeliveryController.java     # 택배 조회 API
│   └── FootballController.java     # 축구 정보 API
├── service/                    # 비즈니스 로직
│   ├── UserService.java            # 사용자 서비스
│   ├── PostService.java            # 게시글 서비스
│   ├── CommentService.java         # 댓글 서비스
│   └── DeliveryService.java        # 택배 조회 서비스
├── repository/                 # 데이터 접근 계층
│   ├── UserRepository.java         # 사용자 Repository
│   ├── PostRepository.java         # 게시글 Repository
│   ├── CommentRepository.java      # 댓글 Repository
│   └── RefreshTokenRepository.java # Refresh Token Repository
├── entity/                     # JPA 엔티티
│   ├── User.java                   # 사용자 엔티티
│   ├── Post.java                   # 게시글 엔티티
│   ├── Comment.java                # 댓글 엔티티
│   └── RefreshToken.java           # Refresh Token 엔티티
├── dto/                        # 데이터 전송 객체
│   ├── UserRegisterDto.java        # 회원가입 DTO
│   ├── LoginDto.java               # 로그인 DTO
│   ├── TokenRequestDto.java        # 토큰 재발급 DTO
│   ├── PostDto.java                # 게시글 작성 DTO
│   ├── PostUpdateDto.java          # 게시글 수정 DTO
│   ├── CommentRequestDto.java      # 댓글 작성 DTO
│   ├── CommentResponseDto.java     # 댓글 응답 DTO
│   └── DeliveryRequestDto.java     # 택배 조회 DTO
├── jwt/                        # JWT 관련
│   └── JwtTokenProvider.java       # JWT 토큰 생성/검증
└── BoardApplication.java       # 애플리케이션 진입점
```

## 🚀 시작하기

### 사전 요구사항
- **Java 17** 이상
- **MySQL 8.0** 이상
- **Gradle** (Wrapper 포함)

### 1. 데이터베이스 설정

MySQL에 데이터베이스를 생성합니다:

```sql
CREATE DATABASE board_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 환경 설정

`src/main/resources/application.yml` 파일을 수정합니다:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/board_db?serverTimezone=Asia/Seoul
    username: your_mysql_username  # MySQL 사용자명
    password: your_mysql_password  # MySQL 비밀번호
    driver-class-name: com.mysql.cj.jdbc.Driver

# Sweet Tracker API 키 설정 (택배 조회 기능 사용 시)
smarttracker:
  key: "your_api_key_here"
```

### 3. 애플리케이션 실행

```bash
# Gradle Wrapper를 사용하여 실행
./gradlew bootRun

# 또는 JAR 파일 빌드 후 실행
./gradlew build
java -jar build/libs/board-0.0.1-SNAPSHOT.jar
```

애플리케이션이 `http://localhost:8080`에서 실행됩니다.

## 📡 API 엔드포인트

### 사용자 관련 API

| 메서드 | 엔드포인트 | 설명 | 인증 필요 |
|--------|------------|------|-----------|
| POST | `/api/users/register` | 회원가입 | ❌ |
| POST | `/api/users/login` | 로그인 | ❌ |
| POST | `/api/users/reissue` | Access Token 재발급 | ❌ |
| GET | `/api/users/check-nickname?nickname=홍길동` | 닉네임 중복 확인 | ❌ |

**회원가입 요청 예시:**
```json
POST /api/users/register
{
  "email": "user@example.com",
  "password": "password123",
  "nickname": "홍길동"
}
```

**로그인 응답 예시:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "nickname": "홍길동"
}
```

### 게시글 관련 API

| 메서드 | 엔드포인트 | 설명 | 인증 필요 |
|--------|------------|------|-----------|
| POST | `/api/create-posts` | 게시글 작성 | ✅ |
| GET | `/api/getPosts` | 전체 게시글 목록 조회 | ❌ |
| GET | `/api/posts/{id}` | 게시글 상세 조회 | ❌ |
| PUT | `/api/posts/{id}` | 게시글 수정 | ✅ |
| DELETE | `/api/posts/{id}` | 게시글 삭제 | ✅ |

**게시글 작성 요청 예시:**
```json
POST /api/create-posts
Authorization: Bearer {accessToken}
{
  "title": "게시글 제목",
  "content": "게시글 내용",
  "email": "user@example.com",
  "nickname": "홍길동"
}
```

### 댓글 관련 API

| 메서드 | 엔드포인트 | 설명 | 인증 필요 |
|--------|------------|------|-----------|
| GET | `/api/posts/{postId}/comments` | 댓글 목록 조회 | ❌ |
| POST | `/api/posts/{postId}/comments` | 댓글 작성 | ✅ |

**댓글 작성 요청 예시:**
```json
POST /api/posts/1/comments
Authorization: Bearer {accessToken}
{
  "content": "댓글 내용",
  "parentId": null  // 대댓글인 경우 부모 댓글 ID
}
```

### 기타 API

| 메서드 | 엔드포인트 | 설명 | 인증 필요 |
|--------|------------|------|-----------|
| POST | `/api/getTrackingDelivery` | 택배 배송 조회 | ❌ |
| GET | `/api/fixtures` | 축구 경기 일정 조회 | ❌ |

**택배 조회 요청 예시:**
```json
POST /api/getTrackingDelivery
{
  "carrier": "04",  // CJ대한통운
  "invoice": "1234567890123"
}
```

## ⚙️ 환경 설정

### JWT Secret Key 변경

`src/main/java/my_board/board/jwt/JwtTokenProvider.java` 파일에서 Secret Key를 변경할 수 있습니다.
**운영 환경에서는 반드시 환경변수나 설정 파일로 관리해야 합니다.**

```java
// 현재 (하드코딩, 개발용)
private final SecretKey key = Keys.hmacShaKeyFor(
    "my-very-secret-key-must-be-32-bytes-long!".getBytes(StandardCharsets.UTF_8)
);

// 권장 (환경변수 사용)
@Value("${jwt.secret}")
private String secretKey;

private SecretKey getKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
}
```

### CORS 설정 변경

프론트엔드 URL이 변경되면 `SecurityConfig.java`의 CORS 설정을 수정합니다:

```java
config.setAllowedOrigins(List.of("http://localhost:5173", "https://your-frontend.com"));
```

### 데이터베이스 DDL 전략

개발/운영 환경에 따라 `application.yml`의 DDL 전략을 변경합니다:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update  # 개발: update, 운영: validate 또는 none
```

## 📝 추가 개선 사항

1. **보안 강화**
   - JWT Secret Key를 환경변수로 관리
   - API Key들을 환경변수나 암호화된 설정 파일로 관리
   - 비밀번호 정책 강화 (최소 길이, 특수문자 포함 등)

2. **예외 처리**
   - 전역 예외 핸들러 추가 (@ControllerAdvice)
   - 커스텀 예외 클래스 정의
   - 일관된 에러 응답 형식

3. **로깅**
   - SLF4J + Logback을 사용한 구조화된 로깅
   - System.out.println을 로거로 대체

4. **테스트**
   - 단위 테스트 (JUnit 5)
   - 통합 테스트
   - API 테스트 (MockMvc)

5. **성능 최적화**
   - N+1 쿼리 문제 해결 (Fetch Join)
   - 캐싱 적용 (Redis, Ehcache 등)
   - 페이징 처리 추가

6. **기능 추가**
   - 게시글 검색 기능
   - 좋아요/조회수 기능
   - 파일 업로드 (이미지 등)
   - 이메일 인증

## 📄 라이센스

이 프로젝트는 개인 학습 및 포트폴리오 목적으로 작성되었습니다.

## 👤 작성자

프로젝트에 대한 문의사항이나 개선 제안이 있으시면 연락 주세요.
