package hello.roommate.auth.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 토큰 재생성 후 전송할 때 사용하는 DTO 클래스
 */
@Getter
@Setter
public class JWTTokenDTO {
	private String accessToken;
	private String refreshToken;
}
