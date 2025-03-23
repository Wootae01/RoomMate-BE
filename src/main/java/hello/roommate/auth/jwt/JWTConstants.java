package hello.roommate.auth.jwt;

public class JWTConstants {
	// 엑세스 토큰 만료 시간: 15분 (밀리초 단위)
	public static final Long ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000L;

	// 리프레시 토큰 만료 시간: 21일 (밀리초 단위)
	public static final Long REFRESH_TOKEN_EXPIRATION = 21 * 24 * 60 * 60 * 1000L;

}
