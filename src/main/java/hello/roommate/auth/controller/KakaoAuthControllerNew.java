package hello.roommate.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import hello.roommate.auth.service.KakaoAuthService;
import hello.roommate.member.domain.Member;
import lombok.RequiredArgsConstructor;

// 엑세스 토큰을 이용해 회원번호를 가져오고, Member_ID에 회원번호를 Set하여 DB에 저장.
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") //임시로 설정
public class KakaoAuthControllerNew {

	private final KakaoAuthService kakaoAuthService;
	private final RestTemplate restTemplate;

	@PostMapping("/kakao/callback")
	public ResponseEntity<Map<String, Object>> kakaoCallback(@RequestBody Map<String, String> request) {
		String code = request.get("code");
		System.out.println("받은 인가 코드: " + code);

		// 프론트에서 넘겨받은 인가코드로 액세스 토큰 요청
		String accessToken = getAccessTokenFromKakao(code);	    // 아래에 있음

		System.out.println("카카오 액세스 토큰: " + accessToken);

		// 액세스 토큰을 이용해 카카오 사용자 정보 : 회원번호 가져오기
		Long kakaoId = kakaoAuthService.getKakaoUserId(accessToken);

		// 기존 회원 여부 확인 && 저장
		// 기존회원이면 해당 member, 첫 로그인이라면 새로운 Member생성 후 Member_id에 kakaoId 저장
		Member member = kakaoAuthService.saveOrUpdateMember(kakaoId);

		Map<String, Object> response = new HashMap<>();
		response.put("userId", member.getId().toString()); // Long → String 변환(Json이 Long을 안받음)
		response.put("isFirstLogin", member.getNickname() == null);  // 닉네임아직 입력하지 않았으므로 첫 로그인인 경우 True(회원가입 폼 화면으로 이동), False -> 홈 화면으로 이동
		response.put("accessToken", accessToken);

		return ResponseEntity.ok(response); //ResponseEntity : Http응답을 만들어 반환
		//  .ok(response)는 response데이터를 프론트로 JSON형식으로 반환
		// 프론트로 로그인 결과를 받아서 다음화면으로 이동하기 위해...
		// UserId와 FirstLogin(T or F), accessToken을 넘겨줌. 이건 나중에 로그아웃도 구현하게 되면 AccessToken 필요해서 넘김
		// true면 회원가입 이동, False라면 홈 화면으로 이동
	}

	// 인가코드를 포함시켜 아래 URL로 POST요청해서 액세스 토큰 겟
	private String getAccessTokenFromKakao(String code) {

		String tokenRequestUrl = "https://kauth.kakao.com/oauth/token";

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();		// 그냥 Map사용해도 될꺼같은데 chatgpt가 multivaluemap권장해서 일단 이렇게 함. 근데 Map해도 문제있을 확률은 적다네요
		params.add("grant_type", "authorization_code");
		params.add("client_id", "YOUR_CLIENT_ID");
		params.add("redirect_uri", "https://localhost:8080/auth/kakao/callback");
		params.add("code", code);

		// 요청 헤더 설정 : 개발자문서의 인가코드 받기
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		// 요청 Body & 헤더 포함한 HTTPEntity필요. Body도 존재하기에
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

		// 위 URL로 Post요청 보내서 JSON 응답을 받아온다.
		ResponseEntity<Map> response = restTemplate.exchange(tokenRequestUrl, HttpMethod.POST, requestEntity, Map.class);

		// response에 저장된 ResponseEntity<Map>를 Map<String, Object>로 변환
		Map<String, Object> responseBody = response.getBody();

		if (responseBody == null || !responseBody.containsKey("access_token")) {
			throw new RuntimeException("카카오 액세스 토큰을 가져올 수 없습니다.");
		}

		return (String) responseBody.get("access_token");
	}
}

