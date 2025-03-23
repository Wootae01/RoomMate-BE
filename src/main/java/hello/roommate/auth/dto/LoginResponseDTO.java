package hello.roommate.auth.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 로그인 응답 보내는 DTO 클래스
 */
@Getter
@Setter
public class LoginResponseDTO {
	private Long memberId;
	private Boolean isFirstLogin;
	private String accessToken;
	private String refreshToken;
}
