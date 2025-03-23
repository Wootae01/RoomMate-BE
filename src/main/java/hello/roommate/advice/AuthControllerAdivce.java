package hello.roommate.advice;

import static hello.roommate.auth.exception.JWTErrorCode.*;

import java.security.SignatureException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import hello.roommate.auth.exception.ExpiredTokenException;
import hello.roommate.auth.exception.InvalidTokenException;
import hello.roommate.auth.exception.MissingTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class AuthControllerAdivce {

	/**
	 * 토큰이 만료되었을 때 발생한 커스텀 에러를 잡아 AuthErrorResult 객체를 반환하는 메서드
	 *
	 * @param e ExpiredTokenException 토큰 만료 시 발생하는 예외
	 * @return Http 400 코드를 담은 AuthErrorResult 객체
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ExpiredTokenException.class)
	public AuthErrorResult handleExpiredTokenException(ExpiredTokenException e) {
		log.error("ExpiredTokenException");

		return new AuthErrorResult(EXPIRED_TOKEN.getCode(), EXPIRED_TOKEN.getMessage());
	}

	/**
	 * 토큰이 만료되었을 때 발생한 에러를 잡아 AuthErrorResult 객체를 반환하는 메서드
	 *
	 * @param e ExpiredJwtException 토큰 만료 시 발생하는 예외
	 * @return Http 400 코드를 담은 AuthErrorResult 객체
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ExpiredJwtException.class)
	public AuthErrorResult handleExpiredJwtException(ExpiredTokenException e) {
		log.error("ExpiredJwtException");

		return new AuthErrorResult(EXPIRED_TOKEN.getCode(), EXPIRED_TOKEN.getMessage());
	}

	/**
	 * 부적절한 토큰을 갖고 요청 했을 때 발생하는 에러를 잡아 AuthErrorResult 객체를 반환하는 메서드
	 *
	 * @param e InvalidTokenException
	 * @return Http 400 코드를 담은 AuthErrorResult 객체
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidTokenException.class)
	public AuthErrorResult handleInvalidTokenException(InvalidTokenException e) {
		log.error("InvalidTokenException");
		return new AuthErrorResult(INVALID_TOKEN.getCode(), INVALID_TOKEN.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MissingTokenException.class)
	public AuthErrorResult handleMissingTokenException(MissingTokenException e) {
		log.error("MissingTokenException");
		return new AuthErrorResult(MISSING_TOKEN.getCode(), MISSING_TOKEN.getMessage());
	}

	/**
	 * 잘못된 jwt 토큰 정보를 갖고 요청했을 때 발생하는 에러를 잡아  AuthErrorResult 객체를 반환하는 메서드
	 * @param e SignatureException 잘못된 토큰 정보를 갖을 때 발생하는 에러
	 * @return Http 400 코드를 담은 AuthErrorResult 객체
	 */
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(SignatureException.class)
	public AuthErrorResult handleSignatureException(SignatureException e) {
		log.error("SignatureException, message={}", e.getMessage());

		return new AuthErrorResult(INVALID_TOKEN.getCode(),
			INVALID_TOKEN.getMessage());
	}

}
