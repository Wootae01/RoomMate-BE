package hello.roommate.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthErrorResult {
	private String code;
	private String message;
}
