package hello.roommate.auth.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.roommate.auth.dto.JWTTokenDTO;
import hello.roommate.auth.dto.LoginRequestDTO;
import hello.roommate.auth.dto.LoginResponseDTO;
import hello.roommate.auth.exception.ExpiredTokenException;
import hello.roommate.auth.exception.JWTErrorCode;
import hello.roommate.auth.exception.RefreshTokenException;
import hello.roommate.auth.jwt.JWTConstants;
import hello.roommate.auth.jwt.JWTUtil;
import hello.roommate.auth.service.RefreshEntityService;
import hello.roommate.auth.service.SignUpService;
import hello.roommate.member.domain.Member;
import hello.roommate.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthController {
	// 프론트에서 회원가입 폼 작성 후 제출 버튼을 누를시
	private final SignUpService signUpService;
	private final MemberService memberService;
	private final RefreshEntityService refreshEntityService;
	private final JWTUtil jwtUtil;

	/**
	 * refresh 토큰을 이용하여 access 토큰을 재생성하는 엔드포인트
	 * 기존 refresh 토큰을 검증 후 새로운 access 토큰과  refresh 토큰을 생성한다.
	 *
	 * @param request
	 * @return 새로운 access 토큰과 refresh 토큰
	 */
	@PostMapping("/reissue")
	public ResponseEntity<?> reissue(HttpServletRequest request) {
		log.info("reissue");
		String header = request.getHeader("Authorization");

		if (header == null || !header.startsWith("Bearer ")) {
			return new ResponseEntity<>(
				Map.of("code", JWTErrorCode.MISSING_TOKEN.getCode(), "message",
					JWTErrorCode.MISSING_TOKEN.getMessage()),
				HttpStatus.BAD_REQUEST);
		}

		String[] split = header.split(" ");
		String refresh = split[1];
		try {
			refreshEntityService.validateRefresh(refresh);
		} catch (RefreshTokenException e) {
			return new ResponseEntity<>(Map.of("code", JWTErrorCode.INVALID_TOKEN.getCode(), "message", e.getMessage()),
				HttpStatus.BAD_REQUEST);

		} catch (ExpiredTokenException e) {
			return new ResponseEntity<>(Map.of("code", JWTErrorCode.EXPIRED_TOKEN.getCode(), "message", e.getMessage()),
				HttpStatus.UNAUTHORIZED);
		}

		//새로운 토큰 생성 후 저장, 기존 refresh 토큰 삭제
		JWTTokenDTO newToken = refreshEntityService.createNewToken(refresh);

		refreshEntityService.saveEntity(jwtUtil.getUsername(refresh), newToken.getRefreshToken(),
			JWTConstants.REFRESH_TOKEN_EXPIRATION);

		refreshEntityService.deleteByRefresh(refresh);

		return ResponseEntity.ok(newToken);
	}

	/**
	 * 서버 사용자 인증용 토큰을 발급하는 엔드포인트
	 * 현재 이 토큰은 /auth/login 요청 시에만 사용할 수 있다.
	 * @return jwt 토큰 반환
	 */
	@PostMapping("/token")
	public Map<String, Object> issueToken() {
		String token = jwtUtil.createJwt("serverUser", "ROLE_SERVER", "login", 3 * 60 * 1000L);
		HashMap<String, Object> response = new HashMap<>();

		response.put("accessToken", token);
		return response;
	}

	/**
	 * 로그인 요청 시 수행하는 엔드포인트.
	 * 이미 가입된 사용자인지 확인하고, 이를 응답에 담아 반환한다.
	 * @param dto
	 * @return
	 */
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {

		String username = dto.getUsername();
		Optional<Member> optional = memberService.findByUsername(username);

		if (optional.isEmpty()) {
			Member newMember = signUpService.createNewMember(username);

			LoginResponseDTO response = signUpService.createLoginResponse(newMember);
			refreshEntityService.saveEntity(username, response.getRefreshToken(),
				JWTConstants.REFRESH_TOKEN_EXPIRATION);
			return ResponseEntity.ok(response);
		} else {
			Member member = optional.get();

			LoginResponseDTO response = signUpService.createLoginResponse(member);
			refreshEntityService.saveEntity(username, response.getRefreshToken(),
				JWTConstants.REFRESH_TOKEN_EXPIRATION);
			return ResponseEntity.ok(response);
		}
	}
}
