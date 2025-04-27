package hello.roommate.recommendation.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import hello.roommate.member.domain.Member;
import hello.roommate.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendService {
	private final MemberService memberService;

	/**
	 * 요청한 사람 A와 요청한 사람과 같은 기숙사 정보를 갖는 B와의 유사도를 계산해서 Map으로 반환(key: id, value: 유사도 값)
	 * 유사도는 코사인 유사도를 기반으로 계산한다.
	 *
	 * @param requestLifeStyleMap 요청한 사람의 LifeStyle (설문한 항목 별로 구분)
	 * @param byDorms 요청한 사람과 같은 기숙사를 갖는 사람들. (자신 제외)
	 * @return (memberId, 유사도)를 갖는 map 반환
	 */
	public Map<Long, Double> getSimilarityMap(Map<String, List<Long>> requestLifeStyleMap, List<Member> byDorms) {
		Map<Long, Double> simMemberMap = new HashMap<>();
		int totalGroups = requestLifeStyleMap.size();
		for (Member b : byDorms) {
			Map<String, List<Long>> bMap = memberService.convertLifeStyleListToMap(b.getLifeStyle());
			double sum = 0.0;
			for (String key : requestLifeStyleMap.keySet()) {
				List<Long> listA = requestLifeStyleMap.get(key);
				List<Long> listB = bMap.getOrDefault(key, Collections.emptyList());

				long count = listA.stream().filter(listB::contains).count();
				double normA = Math.sqrt(listA.size());
				double normB = Math.sqrt(listB.size());
				if (normA > 0 && normB > 0) {
					sum += count / (normA * normB);
				}
			}
			double sim = totalGroups > 0 ? sum / totalGroups : 0.0;
			simMemberMap.put(b.getId(), sim);
		}
		return simMemberMap;
	}

	/**
	 * 유사도 List를 입력 받은 후 상위 30% 값을 반환한다.
	 * @param similarities 유사도 값
	 * @return 상위 30% 값
	 */
	public double getTop30Value(List<Double> similarities) {
		similarities.sort(Comparator.reverseOrder());
		int idx = (int)Math.ceil(similarities.size() * 0.3) - 1;
		return similarities.get(Math.max(0, Math.min(idx, similarities.size() - 1)));
	}

	/**
	 * 가장 유사한 사람 상위 30% (B)가 선호하는 사람(C)을 찾아 반환한다.
	 * B가 선호하는 사람 C가 존재하면 매개변수 simMeberMap으로 받은 유사도 값을 더하여 맵으로 만들어 반환한다.
	 * @param simMemberMap key: memberId, value: 유사도 값
	 * @param threshold 상위 30% 유사도 값
	 * @return key: memberId, value: 유사도 누적 합
	 */
	public Map<Long, Double> accumSimilarityAboveThreshold(Map<Long, Double> simMemberMap, double threshold) {
		Map<Long, Double> resultMap = new HashMap<>();
		simMemberMap.entrySet().stream()
			.filter(e -> e.getValue() >= threshold)
			.forEach(e -> {
				double value = e.getValue();
				List<Member> candidates = memberService.recommendMembers(e.getKey());
				if (candidates == null || candidates.isEmpty()) {
					return;
				}
				for (Member c : candidates) {
					double updated = resultMap.getOrDefault(c.getId(), 0.0) + value;
					resultMap.put(c.getId(), updated);
				}
			});
		return resultMap;
	}
}
