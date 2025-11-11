package hello.roommate.advice;

public class DuplicatedNicknameException extends RuntimeException {
	public DuplicatedNicknameException() {
		super();
	}

	public DuplicatedNicknameException(String message) {
		super(message);
	}

	public DuplicatedNicknameException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicatedNicknameException(Throwable cause) {
		super(cause);
	}

}
