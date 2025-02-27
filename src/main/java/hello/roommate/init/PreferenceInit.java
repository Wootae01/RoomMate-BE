package hello.roommate.init;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

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
		List<Option> result = collect.values().stream()
			.map(list -> {			// list = collect.values()를 통해 나온 List<Option>
				if (list == null || list.isEmpty())
					return null;
				else {
					int n = new Random().nextInt(list.size() + 20);
					if (n >= list.size()) { //해당 카테고리에서 옵션 선택 x
						return null;
					} else {
						return list.get(n); //해당 카테고리 값 랜덤으로 선택
					}

				}
			})
			.filter(Objects::nonNull)    // null이 아니면 true 반환해 .filter() 다음꺼 실행
			.toList();                   // List로 만들어 반환

		return result;					 // null이 제거된 옵션들만 담은 List<Option> 반환
	}
}
