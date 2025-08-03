package hello.roommate.auth.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hello.roommate.auth.dto.JWTTokenDTO;
import hello.roommate.auth.dto.LoginRequestDTO;
import hello.roommate.auth.dto.LoginResponseDTO;
import hello.roommate.auth.dto.ServerTokenRequestDTO;
import hello.roommate.auth.exception.MissingTokenException;
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

	@Value("${android.hashkey}")
	private String hashKey;

	/**
	 * refresh 토큰을 이용하여 access 토큰을 재생성하는 엔드포인트
	 * 기존 refresh 토큰을 검증 후 새로운 access 토큰과  refresh 토큰을 생성한다.
	 *
	 * @param request
	 * @return 새로운 access 토큰과 refresh 토큰
	 */
	@PostMapping("/reissue")
	public ResponseEntity<JWTTokenDTO> reissue(HttpServletRequest request) {
		log.info("reissue");
		String header = request.getHeader("Authorization");

		if (header == null || !header.startsWith("Bearer ")) {
			throw new MissingTokenException();
		}

		String[] split = header.split(" ");
		String refresh = split[1];

		refreshEntityService.validateRefresh(refresh);

		//새로운 토큰 생성 후 저장, 기존 refresh 토큰 삭제
		JWTTokenDTO newToken = refreshEntityService.createNewToken(refresh);

		refreshEntityService.saveEntity(jwtUtil.getUsername(refresh), newToken.getRefreshToken(),
			JWTConstants.REFRESH_TOKEN_EXPIRATION);

		refreshEntityService.deleteByRefresh(refresh);

		return ResponseEntity.ok(newToken);
	}

	/**
	 * 사용자 검증용 토큰을 발급하는 엔드포인트
	 * 임의의 사용자 요청인지, 내 앱에서 발생한 요청인지 확인한 후 토큰을 발급한다.
	 * 현재 이 토큰은 /auth/login 요청 시에만 사용할 수 있다.
	 *
	 * @param dto 안드로이드 해시값. 추후 ios bundle id도 입력 하도록 변경
	 * @return jwt 토큰 반환
	 */
	@PostMapping("/token")
	public Map<String, Object> issueToken(@RequestBody ServerTokenRequestDTO dto) {
		String identifier = dto.getIdentifier();
		if (!identifier.equals(hashKey)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid identifier");
		}
		String token = jwtUtil.createJwt("serverUser", "ROLE_SERVER", "login", 2 * 60 * 1000L);
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
		log.info("로그인 요청 username={}", dto.getUsername());
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

	/**
	 * 로그인 상태 여부를 확인하는 엔드포인트. 앱 시작시 사용된다.
	 * @param dto username 을 담고있는 dto 객체
	 * @return memberId
	 */
	@PostMapping("/check-login")
	public Map<String, Long> checkLogin(@RequestBody LoginRequestDTO dto) {
		log.info("로그인 여부 요청");
		String username = dto.getUsername();
		Optional<Member> optional = memberService.findByUsername(username);
		if (optional.isEmpty()) {
			return Map.of("memberId", -1L); //회원 가입 하지 않는 사용자 의미
		}

		Member member = optional.get();
		if (member.getNickname() == null) {
			return Map.of("memberId", -1L); //회원 가입 하지 않는 사용자 의미
		} else {
			return Map.of("memberId", member.getId());
		}

	}

}
