package hello.roommate.auth.exception;

public class MissingTokenException extends RuntimeException {
	public MissingTokenException() {
		super();
	}

	public MissingTokenException(String message) {
		super(message);
	}
}
