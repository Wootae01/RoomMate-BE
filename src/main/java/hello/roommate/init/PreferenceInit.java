package hello.roommate.init;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
import hello.roommate.recommendation.domain.Option;
import hello.roommate.recommendation.domain.Preference;
import hello.roommate.recommendation.domain.enums.Category;
import hello.roommate.recommendation.repository.OptionRepository;
import hello.roommate.recommendation.repository.PreferenceRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PreferenceInit {
	private final PreferenceRepository preferenceRepository;
	private final OptionRepository optionRepository;
	private final MemberRepository memberRepository;

	public void createPreference() {
		if (preferenceRepository.count() != 0) {
			return;
		}
		List<Member> members = memberRepository.findAll();
		List<Option> options = optionRepository.findAll();

		List<Preference> preferences = new ArrayList<>();
		for (Member member : members) {
			List<Option> selected = selectOption(options);
			for (Option option : selected) {
				Preference preference = new Preference(member, option);
				preferences.add(preference);
			}
		}
		preferenceRepository.saveAll(preferences);
	}

	private List<Option> selectOption(List<Option> options) {
		Map<Category, List<Option>> collect = options.stream()
			.collect(Collectors.groupingBy(Option::getCategory));
		// Collectors.groupingBy=> 각 Option 객체를 분류기준 : Category 값에 따라 그룹으로 묶음
		// 각 Option 객체의 getCategory()를 통해 해당 옵션의 Category 값을 얻음 <- Key 역할
		// 인자로 받은 List<Option>의 모든 Option 객체들이 해당 카테고리에 맞춰 분류되어 저장.

		Random random = new Random();
		List<Option> result = collect.values().stream()
			.flatMap(list -> {
				if (list == null || list.isEmpty()) {
					return Stream.<Option>empty();
				}
				int n = new Random().nextInt(list.size() + 10); //상관 없음 항목 결정하기 위한 변수
				if (n >= list.size()) {
					return Stream.of(list.get(0));
				} else {
					int startIndex = random.nextInt(list.size());
					int count = random.nextInt(list.size()) + 1; // 최소 1개
					List<Option> sub = new ArrayList<>();
					for (int i = startIndex, k = 0; i < list.size() && k < count; i++, k++) {
						sub.add(list.get(i));
					}
					//모든 항목이 다 체크된 경우는 상관없음 항목 체크한 것과 동일하게 적용
					if (sub.size() == list.size()) {
						Stream.of(list.get(0));
					}
					return sub.stream();
				}
			}).toList();

		return result;
	}
}
