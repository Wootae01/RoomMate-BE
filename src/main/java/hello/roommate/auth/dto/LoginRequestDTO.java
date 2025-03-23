package hello.roommate.auth.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 로그인 요청
 */
@Getter
@Setter
public class LoginRequestDTO {
	private String username;
}
