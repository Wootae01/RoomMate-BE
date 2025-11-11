package hello.roommate.recommendation.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.roommate.mapper.MemberRecommendationMapper;
import hello.roommate.member.domain.Member;
import hello.roommate.member.dto.FilterCond;
import hello.roommate.member.dto.RecommendMemberDTO;
import hello.roommate.member.service.MemberService;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.service.RecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/recommendations")
public class RecommendController {
	private final MemberService memberService;
	private final RecommendService recommendService;
	private final MemberRecommendationMapper mapper;

	@GetMapping("/{memberId}/similarity")
	public List<RecommendMemberDTO> recommend(@PathVariable Long memberId) {

		//1. 요청한 사람의 lifestyle 정보
		Member requestMember = memberService.findWithLifeStyleById(memberId);
		List<LifeStyle> requestLifeStyle = requestMember.getLifeStyle();
		Map<String, List<Long>> requestLifeStyleMap = mapper.convertLifeStyleListToMap(requestLifeStyle);

		//2. 유사도 계산하여 반환
		Map<Long, Double> simMemberMap = recommendService.getSimilarityMap(memberId, requestLifeStyleMap);
		if (simMemberMap.isEmpty()) {
			return Collections.emptyList();
		}

		//3. 상위 30% 값 계산
		double top30 = recommendService.getThresholdValue(new ArrayList<>(simMemberMap.values()));
		List<Long> simMemberIds = simMemberMap.entrySet().stream()
			.filter(e -> e.getValue() >= top30)
			.map(Map.Entry::getKey)
			.toList();
		List<Member> simMembers = memberService.findAllWithPreferenceByIds(simMemberIds); //B
		List<Member> byDorms = memberService.findAllWithLifeStyleByDormAndGender(requestMember.getDorm(),
			requestMember.getGender()); //C

		//4. 추천 후보 가중 합산
		Map<Long, Double> recommendMap = recommendService.accumSimilarity(memberId, simMembers, byDorms, simMemberMap);
		//Map<Long, Double> recommendMap = recommendService.accumSimilarityAboveThreshold(memberId, simMemberMap, top30);

		//5. 유사도 순으로 정렬 후 추천 멤머 id 리스트 반환
		List<Long> recommendMemberIds = recommendMap.entrySet().stream()
			.sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
			.map(e -> e.getKey())
			.toList();

		List<Member> recommendMember = memberService.findAllByIds(recommendMemberIds);
		Map<Long, Member> memberMap = recommendMember.stream()
			.collect(Collectors.toMap(Member::getId, member -> member));

		//6. dto로 변환
		return recommendMemberIds.stream()
			.map(id -> {
				Member member = memberMap.get(id);
				RecommendMemberDTO dto = new RecommendMemberDTO(member.getId(), member.getNickname(),
					member.getIntroduce());
				return dto;
			}).toList();
	}

	/**
	 * 기본 추천 목록 반환
	 * 요청자가 선택한 선호하는 룸메 조건을 기준으로 필터링
	 *
	 * @param memberId 사용자 id
	 * @return 추천 목록 멤버 반환
	 */
	@GetMapping("/{memberId}/basic")
	public List<RecommendMemberDTO> recommendMembers(@PathVariable Long memberId) {
		log.info("추천 목록 반환 요청, id={}", memberId);
		List<Member> members = recommendService.searchMembersByPreference(memberId);

		//dto로 변환
		List<RecommendMemberDTO> dtoList = members.stream()
			.map(mapper::convertToDTO)
			.collect(Collectors.toList());
		return dtoList;
	}

	/**
	 * 필터 적용하여 추천목록 반환
	 *
	 * @param memberId 사용자 id
	 * @param filterCond 사용자가 적용한 필터 항목들
	 * @return 필터 적용된 추천 목록 멤버 반환
	 */
	@PostMapping("/{memberId}/filter")
	public List<RecommendMemberDTO> searchMembers(@PathVariable Long memberId,
		@RequestBody @Validated FilterCond filterCond) {
		Instant t0 = Instant.now();
		List<Member> members = recommendService.searchMembersByFilter(memberId, filterCond);
		Instant t1 = Instant.now();
		List<RecommendMemberDTO> dtoList = members.stream()
			.map(mapper::convertToDTO)
			.collect(Collectors.toList());
		Instant t2 = Instant.now();

		log.info("fetch={}ms, convert={}ms", Duration.between(t0, t1).toMillis(), Duration.between(t1, t2).toMillis());

		return dtoList;
	}

	/**
	 * 나와 같은 기숙사인 모든 사람 찾기
	 * @param memberId
	 * @return
	 */
	@GetMapping("/{memberId}/all")
	public List<RecommendMemberDTO> findAllByDorm(@PathVariable Long memberId) {
		List<Member> members = memberService.findAllByDorm(memberId);

		List<RecommendMemberDTO> dtoList = members.stream()
			.map(mapper::convertToDTO)
			.collect(Collectors.toList());

		return dtoList;
	}
}
