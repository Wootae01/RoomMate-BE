package hello.roommate.recommendation.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hello.roommate.mapper.MemberRecommendationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.member.domain.Member;
import hello.roommate.member.dto.FilterCond;
import hello.roommate.member.service.MemberService;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Option;
import hello.roommate.recommendation.domain.Preference;
import hello.roommate.recommendation.domain.enums.Category;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RecommendService {
	private final MemberService memberService;
	private final OptionService optionService;
	private final SimilarityUtils similarityUtils;
	private final MemberRecommendationMapper mapper;

	/**
	 * 요청한 사람 A와 요청한 사람과 같은 기숙사 정보를 갖는 B와의 유사도를 계산해서 Map으로 반환(key: id, value: 유사도 값)
	 * 유사도는 코사인 유사도를 기반으로 계산한다.
	 *
	 * @param requestLifeStyleMap 요청한 사람의 LifeStyle (설문한 항목 별로 구분)
	 * @return (memberId, 유사도)를 갖는 map 반환
	 */
	public Map<Long, Double> getSimilarityMap(Long requestId, Map<String, List<Long>> requestLifeStyleMap) {
		Map<Long, Double> simMemberMap = new HashMap<>();
		List<Option> options = optionService.findAll();
		Map<Long, Integer> opionIdxMap = optionService.getOpionIdxMap(options);

		double[] reqVec = similarityUtils.getVec(requestLifeStyleMap, opionIdxMap);

		List<Member> members = memberService.findAllWithLifeStyle();
		for (Member member : members) {
			if (member.getId() == requestId) {
				continue;
			}
			List<LifeStyle> lifeStyle = member.getLifeStyle();
			Map<String, List<Long>> lifeStyleMap = mapper.convertLifeStyleListToMap(lifeStyle);
			double[] vec = similarityUtils.getVec(lifeStyleMap, opionIdxMap);
			double sim = similarityUtils.cosSimilarity(reqVec, vec);
			simMemberMap.put(member.getId(), sim);
		}
		return simMemberMap;
	}

	/**
	 * B들이 선호하는 멤버 C를 찾아 반환한다.
	 * B가 선호하는 사람 C가 존재하면 매개변수 simMap으로 받은 유사도 값을 더하여 맵으로 만들어 반환한다.
	 * 메모리 기반으로 C를 찾는다.
	 * @param preMembers  preference 정보를 갖고 있는 멤버 리스트(B)
	 * @param lifeMembers lifeStyle 정보를 갖고있는 멤버 리스트(C)
	 * @param simMap 멤버별 유사도 값
	 * @return key:memberId, value: 누적된 유사도 값을 갖는 맵
	 */
	public Map<Long, Double> accumSimilarity(
		Long myId, List<Member> preMembers, List<Member> lifeMembers, Map<Long, Double> simMap) {
		Map<Long, Double> result = new HashMap<>();

		//각 멤버별 lifeStyle 조건 맵에 저장
		Map<Long, Map<String, List<Long>>> lifeMap = new HashMap<>();
		for (Member lifeMember : lifeMembers) {
			if (lifeMember.getId().equals(myId)) {
				continue;
			}
			List<LifeStyle> lifeStyles = lifeMember.getLifeStyle();
			Map<String, List<Long>> map = mapper.convertLifeStyleListToMap(lifeStyles);
			lifeMap.put(lifeMember.getId(), map);
		}

		for (Member preMember : preMembers) {
			List<Preference> preferences = preMember.getPreference();
			Map<Category, List<Long>> cond = mapper.convertPreferenceListToMapWithoutNone(preferences);
			List<Long> ageList = cond.remove(Category.AGE);
			List<Integer> intAges = getIntAges(ageList);
			for (Member lifeMember : lifeMembers) {
				if (lifeMember.getId().equals(myId)) {
					continue;
				}
				int age = lifeMember.getAge();
				if (!intAges.isEmpty() && !intAges.contains(age)) {
					continue;
				}
				Map<String, List<Long>> map = lifeMap.get(lifeMember.getId());
				if (isOverlap(cond, map)) {
					Long id = lifeMember.getId();
					Double updateValue = result.getOrDefault(id, 0.0) + simMap.getOrDefault(preMember.getId(), 0.0);
					result.put(id, updateValue);
				}
			}
		}
		return result;
	}

	/**
	 * 각 카테고리 별로 겹치는 값이 1개 이상 있으면 true, 없으면 false 반환
	 * @param cond 검색 조건
	 * @param lifeMap lifeStyle 정보 담긴 map
	 * @return boolean
	 */
	private boolean isOverlap(Map<Category, List<Long>> cond, Map<String, List<Long>> lifeMap) {
		for (Category key : cond.keySet()) {
			List<Long> pre = cond.get(key);
			List<Long> life = lifeMap.get(key.name());
			if (Collections.disjoint(pre, life)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 유사도 List를 입력 받은 후 상위 30% 값을 반환한다.
	 * @param similarities 유사도 값
	 * @return 상위 30% 값
	 */
	public double getThresholdValue(List<Double> similarities) {
		similarities.sort(Comparator.reverseOrder());
		int idx = (int)Math.ceil(similarities.size() * 0.3) - 1;
		return similarities.get(Math.max(0, Math.min(idx, similarities.size() - 1)));
	}

	/**
	 * 가장 유사한 사람 상위 30% (B)가 선호하는 사람(C)을 찾아 반환한다.
	 * B가 선호하는 사람 C가 존재하면 매개변수 simMeberMap으로 받은 유사도 값을 더하여 맵으로 만들어 반환한다.
	 * DB 쿼리를 통해 C를 찾는다.
	 * @param simMemberMap key: memberId, value: 유사도 값
	 * @param threshold 상위 30% 유사도 값
	 * @return key: memberId, value: 유사도 누적 합
	 */
	public Map<Long, Double> accumSimilarityAboveThreshold(Long myId, Map<Long, Double> simMemberMap,
		double threshold) {
		Map<Long, Double> resultMap = new HashMap<>();
		simMemberMap.entrySet().stream()
			.filter(e -> e.getValue() >= threshold)
			.forEach(e -> {
				double value = e.getValue();
				List<Member> candidates = recommendMembersBySimilarUsers(myId, e.getKey());
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

	/**
	 * 나와 비슷한 사용자의 Preference를 기준으로 추천 멤버를 찾는 메서드
	 *
	 * @param myId 나의 id
	 * @param similarUserId 나와 비슷한 사용자 id
	 * @return 추천 멤버 리스트
	 */
	public List<Member> recommendMembersBySimilarUsers(Long myId, Long similarUserId) {

		Member simMember = memberService.findById(similarUserId);

		List<Preference> preferences = simMember.getPreference();
		//상관 없음 체크한 항목 제외한 옵션 추출
		List<Option> options = preferences
			.stream()
			.filter(preference -> preference.getOption().getId() > 100)
			.map(Preference::getOption)
			.toList();

		Map<Category, List<Long>> cond = options.stream()
			.collect(
				Collectors.groupingBy(
					Option::getCategory,
					Collectors.mapping(Option::getId, Collectors.toList())
				));

		//나이 추출
		List<Long> ages = cond.remove(Category.AGE);
		List<Integer> intAges = getIntAges(ages);

		return memberService.search(myId, cond, intAges);
	}

	public List<Member> searchMembersByPreference(Long myId) {
		Member member = memberService.findById(myId);

		List<Preference> preferences = member.getPreference();

		//상관 없음 체크한 항목 제외한 옵션 추출
		List<Option> options = preferences
			.stream()
			.filter(preference -> preference.getOption().getId() > 100)
			.map(Preference::getOption)
			.toList();

		Map<Category, List<Long>> cond = options.stream()
			.collect(
				Collectors.groupingBy(
					Option::getCategory,
					Collectors.mapping(Option::getId, Collectors.toList())
				));

		//나이 추출
		List<Long> ages = cond.remove(Category.AGE);
		List<Integer> intAges = getIntAges(ages);

		return memberService.search(myId, cond, intAges);
	}

	/**
	 * 검색 조건을 입력받아 추천 멤버 검색
	 * @param myId 사용자 id
	 * @param filterCond 검색 조건
	 * @return 추천 멤버
	 */
	public List<Member> searchMembersByFilter(Long myId, FilterCond filterCond) {
		Map<Category, List<Long>> cond = filterCond.getCond();
		List<Long> ages = cond.remove(Category.AGE);
		List<Integer> intAges = getIntAges(ages);

		List<Member> search = memberService.search(myId, cond, intAges);
		return search;
	}

	//age Long 값 Integer 로 변경
	private static List<Integer> getIntAges(List<Long> ages) {

		List<Integer> intAges = ages == null ? new ArrayList<>() : ages.stream()
			.map(Long::intValue)
			.toList();
		return intAges;
	}
}
