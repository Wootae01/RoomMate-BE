package hello.roommate.recommendation.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import hello.roommate.member.domain.Member;
import hello.roommate.member.dto.RecommendMemberDTO;
import hello.roommate.member.service.MemberService;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.service.RecommendService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RecommendController {
	private final MemberService memberService;
	private final RecommendService recommendService;

	@GetMapping("/recommendation/{memberId}")
	public List<RecommendMemberDTO> recommend(@PathVariable Long memberId) {

		//1. 요청한 사람의 lifestyle 정보
		Member requestMember = memberService.findById(memberId);
		List<LifeStyle> requestLifeStyle = requestMember.getLifeStyle();
		Map<String, List<Long>> requestLifeStyleMap = memberService.convertLifeStyleListToMap(requestLifeStyle);

		//2. 동일 기숙사 내 모든 사용자 (자신 제외)
		List<Member> byDorms = memberService.findByDorm(requestMember.getDorm());
		byDorms.remove(requestMember);

		//3. 유사도 계산하여 반환
		Map<Long, Double> simMemberMap = recommendService.getSimilarityMap(requestLifeStyleMap, byDorms);
		if (simMemberMap.isEmpty()) {
			return Collections.emptyList();
		}

		//4. 상위 30% 값 계산
		double top30 = recommendService.getTop30Value(new ArrayList<>(simMemberMap.values()));

		//5. 추천 후보 가중 합산
		Map<Long, Double> recommendMap = recommendService.accumSimilarityAboveThreshold(simMemberMap, top30);

		//6. 상위 5개 반환
		List<Long> recommendMemberIds = recommendMap.entrySet().stream()
			.sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
			.limit(5)
			.map(e -> e.getKey())
			.toList();

		List<Member> recommendMember = memberService.findAllByIds(recommendMemberIds);

		return recommendMember.stream()
			.map(e -> {
				return new RecommendMemberDTO(e.getId(), e.getNickname(), e.getIntroduce());
			})
			.toList();
	}

}
