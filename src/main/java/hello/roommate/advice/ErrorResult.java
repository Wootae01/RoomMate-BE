package hello.roommate.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 에러 정보를 담을 객체
 */
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ErrorResult {
	private int status;
	private String message;
}
