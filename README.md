# 기숙사 룸메이트 매칭 애플리케이션

Spring Boot 기반 기숙사 룸메이트 매칭 백엔드

## 프로젝트 소개

충북대학교 기숙사 룸메이트를 구할 때, 대학 커뮤니티 앱(에브리타임)에
생활 패턴과 선호 조건을 직접 글로 올리고 연락을 기다리는 과정이
번거롭다는 불편함에서 출발했습니다. 생활 방식과 선호 조건을
등록하면 조건에 맞는 룸메이트를 추천해주는 매칭 서비스입니다.

## 아키텍처

<img src="https://github.com/user-attachments/assets/4a16fd01-a09f-4613-a32c-fe639ed04718" width="800" alt="architecture"/>

## 기술 스택

- **Java 21** / **Spring Boot 3.3.3**
- **Spring Data JPA** + **MySQL 8.0**
- **Redis** (Refresh Token 저장, 알림 캐싱)
- **Spring Security** + **JWT** (Access / Refresh Token)
- **Spring WebSocket** (STOMP 기반 실시간 채팅)
- **Terraform** (AWS 인프라 관리)
- **Docker Compose** (로컬 개발 환경)

## 외부 연동

- **Expo Push Notifications** (푸시 알림)

## 주요 기능

### 인증

- 소셜 로그인(카카오, 구글)
- JWT Access / Refresh Token 발급 및 재발급 (Refresh Token Rotation)
- 로그인 상태 확인, 로그아웃, 회원 탈퇴

### 회원가입 및 프로필

- 닉네임·나이·성별·기숙사·자기소개 등록
- 생활 방식(LifeStyle) 설문 등록
- 룸메이트 선호 조건(Preference) 등록
- 프로필 / 라이프스타일 / 선호조건 개별 수정
- 닉네임 중복 확인

### 룸메이트 추천

- **기본 추천**: 내 Preference 조건에 맞는 같은 기숙사 멤버 필터링
- **필터 추천**: 사용자가 직접 조건 설정 후 검색
- **전체 조회**: 같은 기숙사 전체 멤버 조회

### 채팅

- WebSocket (STOMP) 실시간 채팅
- 채팅방 생성 (기존 채팅방 재사용)
- 채팅 메시지 저장 및 조회
- 내 채팅방 목록 조회

### 푸시 알림

- Expo Push Notifications 기반 채팅 푸시 알림
- 알림 허용 / 거부 설정

## 화면 구성

| 로그인                                                                                                      | 프로필 설정                                                                                                   |
|----------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|
| <img src="https://github.com/user-attachments/assets/a599f570-8aac-4f41-811a-52d17b5b08af" width="200"/> | <img src="https://github.com/user-attachments/assets/5d490584-6660-4369-984c-e9809114c6df" width="200"/> |

| 라이프스타일 설정                                                                                                | 선호 조건 설정                                                                                                 |
|----------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|
| <img src="https://github.com/user-attachments/assets/1b311768-bfc8-47d5-8376-1e9d5051797a" width="200"/> | <img src="https://github.com/user-attachments/assets/b76e9e86-6671-4cad-907e-6b1778727ab8" width="200"/> |

| 룸메이트 추천                                                                                                  | 필터 추천                                                                                                    |
|----------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|
| <img src="https://github.com/user-attachments/assets/7ccf9aad-c227-47a4-8cec-1966e9d5db9a" width="200"/> | <img src="https://github.com/user-attachments/assets/8de1f953-58e7-489b-888e-85bb370c5a26" width="200"/> |

| 상대 프로필                                                                                                   | 채팅 목록                                                                                                    |
|----------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|
| <img src="https://github.com/user-attachments/assets/f296c158-2359-43c7-8f50-ae3534912bb6" width="200"/> | <img src="https://github.com/user-attachments/assets/136107e1-b717-49d0-82c3-117dcfc5d7fb" width="200"/> |

## 트러블슈팅

### 1. 추천 N+1 쿼리 및 쿼리 실행 순서 최적화

#### N+1 쿼리 제거 (`GET /recommendations/{id}/basic`)

- **문제**: 추천 멤버 조회 시 멤버 수만큼 notification SELECT 추가 발생
    - `Member.notification`이 `@OneToOne` 비주인 쪽(FK가 상대 테이블)이라 `fetch = LAZY` 설정과 무관하게 Hibernate가 즉시 SELECT → null 여부를 알 수
      없어 프록시 생성 불가
- **해결**: 멤버 조회 쿼리에 `LEFT JOIN FETCH notification` 추가 → 단일 쿼리로 일괄 조회
- **결과**: GET /recommendations/basic **710ms → 90ms**

#### 쿼리 실행 순서 변경 (`GET /recommendations/{id}/basic`)

- **문제**: 같은 기숙사·성별 멤버만 추천 대상임에도, lifestyle 스캔(GROUP BY + HAVING)을 전체 멤버 대상으로 먼저 수행
- **해결**: 실행 순서 변경
    1. member 테이블에서 같은 기숙사·성별·나이로 먼저 필터링 (단순 컬럼 비교, 저비용)
    2. 좁혀진 member_id 범위 안에서만 lifestyle 스캔 (GROUP BY + HAVING 집계, 고비용)
- **결과**: **226ms → 51ms** (약 77% 개선)

### 2. 채팅방 인가 검증 쿼리 인덱스 최적화

- **문제**: 메시지 조회 시 요청자가 해당 채팅방 멤버인지 인가 검증하는 쿼리에서, 각 FK 인덱스를 별도 스캔 후 Intersect 수행 → 불필요한 row 탐색
- **해결**: 회원ID + 채팅방ID 복합 인덱스 추가 → Covering index lookup으로 단일 스캔
- **결과**: **0.108ms → 0.027ms** (약 75% 개선)

### 3. 채팅방 생성 중복 동시성 제어 (`POST /chat-rooms`)

- **문제**: A→B 채팅방 생성 요청이 동시에 들어올 경우 두 요청 모두 기존 채팅방 없음으로 판단 → 중복 채팅방 생성
- **해결**: Redis 분산락 적용 → 같은 두 멤버 간 채팅방 생성 요청을 순차 처리해 중복 생성 차단

### 4. Refresh Token 저장소 DB → Redis 전환

- **문제**: Refresh Token은 단순 조회·삭제와 TTL 기반 만료만 필요한데, RDB에 저장해 reissue 요청마다 불필요한 DB I/O 발생
- **해결**: Redis로 저장소 전환 (TTL 기반 만료 처리, DB 부하 제거)
- **결과**: 토큰 재발급 요청 p95 **76.92ms → 17.91ms** (약 77% 감소, 10 VUs 기준)

### 5. Notification 조회 Redis 캐싱

- **문제**: 메시지 전송마다 수신자 Notification(푸시 알림 설정)을 DB에서 조회
- **해결**: Notification 조회 결과를 Redis에 캐싱, 알림 설정 변경 시 캐시 evict

### 6. JWTFilter에서 예외 throw 시 ControllerAdvice 미처리

- **문제**: 토큰 유효성 오류 시 커스텀 예외를 throw하고 `@ControllerAdvice`에서 처리하려 했으나 500 응답 반환
    - `JWTFilter`는 DispatcherServlet **앞**에서 실행되는 Servlet Filter이므로 `@ControllerAdvice` 범위 밖
- **해결**: 예외 throw 대신 `HttpServletResponse`에 직접 JSON 에러 응답을 작성

## 환경 변수

| 변수                | 설명                      |
|-------------------|-------------------------|
| `REDIS_HOST`      | ElastiCache Redis 엔드포인트 |
| `RDS_URL`         | MySQL 접속 URL            |
| `RDS_PASSWORD`    | DB 비밀번호                 |
| `JWT_SECRET`      | JWT 서명 키                |
| `ANDROID_HASHKEY` | 앱-서버 간 토큰 발급용 공유 비밀키    |
