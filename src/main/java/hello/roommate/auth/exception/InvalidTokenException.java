package hello.roommate.auth.exception;

public class InvalidTokenException extends RuntimeException {
	public InvalidTokenException() {
		super();
	}

	public InvalidTokenException(String message) {
		super(message);
	}
}
