package hello.roommate.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.roommate.auth.dto.EditMemberDTO;
import hello.roommate.auth.dto.SignUpDTO;
import hello.roommate.auth.service.SignUpService;
import hello.roommate.recommendation.dto.LifeStyleDTO;
import hello.roommate.recommendation.dto.PreferenceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class SignUpController {

	// 프론트에서 회원가입 폼 작성 후 제출 버튼을 누를시
	private final SignUpService signUpService;

	@PostMapping("/signup")    // 로그인 완료 후 회원가입 폼 작성한 후 Post로 전달
	public ResponseEntity<Map<String, Object>> signUp(
		@RequestBody SignUpDTO request) { // 프론트에서 받은 Json데이터를 SignUpDTO 객체로 변환\
		log.info("signup request= {}", request);
		signUpService.registerMember(request);

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);

		return ResponseEntity.ok(response);   //프론트에서 success 값 확인하여 해당 화면에 맞게 처리하도록
	}

	// 프론트에서 내정보 1단계만 수정할 시(Member만 수정시)
	@PostMapping("/EditProfile")
	public ResponseEntity<Map<String, Object>> editProfile(@RequestBody EditMemberDTO member) {
		signUpService.EditMember(member);

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);

		return ResponseEntity.ok(response);
	}

	// 프론트에서 LifeStyle 2단계만 수정할 시(LifeStyle만 수정시)
	@PostMapping("/EditLifeStyle")
	public ResponseEntity<Map<String, Object>> editLifeStyle(@RequestBody LifeStyleDTO member) {
		// Member LifeStyle만 수정할 경우
		signUpService.EditLifeStyle(member);

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);

		return ResponseEntity.ok(response);
	}

	// 프론트에서 Preference 1단계만 수정할 시(Preference만 수정시)
	@PostMapping("/EditPreference")
	public ResponseEntity<Map<String, Object>> editPreference(@RequestBody PreferenceDTO member) {
		// Member Preference만 수정할 경우
		signUpService.EditPreference(member);

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);

		return ResponseEntity.ok(response);
	}

}

