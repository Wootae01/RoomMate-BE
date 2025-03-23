package hello.roommate.auth.exception;

/**
 * 토큰이 만료되었을 때 발생하는 에러
 */
public class ExpiredTokenException extends RuntimeException {
	public ExpiredTokenException() {
		super();
	}

	public ExpiredTokenException(String message) {
		super(message);
	}
}
