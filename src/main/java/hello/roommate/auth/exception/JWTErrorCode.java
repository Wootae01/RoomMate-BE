package hello.roommate.auth.exception;

public enum JWTErrorCode {

	//유효기간 만료 에러 코드
	EXPIRED_TOKEN("EXPIRED_TOKEN", "Token expired"),

	// 유효하지 않은 토큰 에러 코드
	INVALID_TOKEN("INVALID_TOKEN", "Invalid token"),

	// 토큰이 존재하지 않을 때 에러 코드
	MISSING_TOKEN("MISSING_TOKEN", "Token not provided");

	private final String code;
	private final String message;

	JWTErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
