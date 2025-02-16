package hello.roommate.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.roommate.auth.dto.SignUpDTO;
import hello.roommate.auth.service.SignUpService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SignUpController {
	// 프론트에서 회원가입 폼 작성 후 제출 버튼을 누를시
	private final SignUpService signUpService;

	@PostMapping("/signup")	// 로그인 완료 후 회원가입 폼 작성한 후 Post로 전달
	public ResponseEntity<Map<String, Object>> signUp(@RequestBody SignUpDTO request) { // 프론트에서 받은 Json데이터를 SignUpDTO 객체로 변환
		signUpService.registerMember(request);

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);

		return ResponseEntity.ok(response);		//프론트에서 success 값 확인하여 해당 화면에 맞게 처리하도록
	}
}

