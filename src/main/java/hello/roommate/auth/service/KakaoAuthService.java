package hello.roommate.auth.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {
	// 아래 URL로 액세스 토큰으로 요청해서 사용자 정보 : 회원번호 받아오기

	private final RestTemplate restTemplate;
	private final MemberRepository memberRepository;

	// GET : https://kapi.kakao.com/v2/user/me로 엑세스 토큰방식(액세스토큰을 헤더에 포함)으로 GET 요청보내서 회원번호 : ID 받아오기
	public Long getKakaoUserId(String accessToken) {
		String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + accessToken);  // 액세스 토큰방식의 헤더 설정 : 문서대로
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<Map> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, requestEntity, Map.class);
		//.exchange() 를 통해 먼저 위 URL로 먼저 GET요청을 보내서(헤더포함) 응답을 받아와(JSON->Map 자동변환) response에 저장
		Map<String, Object> responseBody = response.getBody();	// 서버에서 받아온 응답을 .getBody()를 통해 Map<String, ObJect>로 변환.

		if (responseBody == null || !responseBody.containsKey("id")) {
			throw new RuntimeException("카카오 사용자 정보를 가져올 수 없습니다.");
		}

		Long kakaoId = ((Number) responseBody.get("id")).longValue();
		System.out.println("카카오 회원번호(ID): " + kakaoId);

		return kakaoId;
	}

	public Member saveOrUpdateMember(Long kakaoId) {

		return memberRepository.findById(kakaoId)	//DB에 ID있으면 Optional<Member>반환
			.orElseGet(() -> {
				// 첫 로그인(기존회원 X)이라면 신규 회원 생성 후 저장
				Member newMember = new Member();
				newMember.setId(kakaoId);
				memberRepository.save(newMember);
				return newMember;
			});
	}
}

