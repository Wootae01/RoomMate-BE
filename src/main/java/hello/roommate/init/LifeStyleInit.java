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
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Option;
import hello.roommate.recommendation.domain.enums.Category;
import hello.roommate.recommendation.repository.LifeStyleRepository;
import hello.roommate.recommendation.repository.OptionRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LifeStyleInit {
	private final LifeStyleRepository lifeStyleRepository;
	private final OptionRepository optionRepository;
	private final MemberRepository memberRepository;

	public void createLifeStyle() {
		List<Member> members = memberRepository.findAll();
		List<Option> options = optionRepository.findAll();

		List<LifeStyle> lifeStyles = new ArrayList<>();

		for (Member member : members) {
			List<Option> selected = selectOption(options);
			for (Option option : selected) {
				LifeStyle lifeStyle = new LifeStyle(member, option);
				lifeStyles.add(lifeStyle);

			}
		}
		lifeStyleRepository.saveAll(lifeStyles);
	}

	private List<Option> selectOption(List<Option> options) {
		Map<Category, List<Option>> groupOption = options.stream()
			.filter(option -> option.getCategory() != Category.AGE)
			.collect(Collectors.groupingBy(Option::getCategory));

		List<Option> selectOption = groupOption
			.values().stream()
			.map(list -> {
				if (list == null || list.isEmpty()) {
					return null;
				}
				return list.get(new Random().nextInt(list.size()));
			})
			.filter(Objects::nonNull)
			.toList();

		return selectOption;
	}
}
