package hello.roommate.auth.exception;

public class RefreshTokenException extends RuntimeException {
	public RefreshTokenException() {
		super();
	}

	public RefreshTokenException(String message) {
		super(message);
	}

}
