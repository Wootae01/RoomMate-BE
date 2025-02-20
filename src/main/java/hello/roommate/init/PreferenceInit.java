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

		List<Option> result = collect.values().stream()
			.map(list -> {
				if (list == null || list.isEmpty())
					return null;
				else {
					int n = new Random().nextInt(list.size() + 9);
					if (n >= list.size()) { //해당 카테고리 선택 x
						return null;
					} else {
						return list.get(n); //해당 카테고리 값 랜덤으로 선택
					}

				}
			})
			.filter(Objects::nonNull)
			.toList();

		return result;
	}
}
