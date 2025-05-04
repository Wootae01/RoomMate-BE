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
		if (lifeStyleRepository.count() != 0) {
			return;
		}
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
			.filter(option -> option.getId() > 100) //상관 없음 항목 제외
			.collect(Collectors.groupingBy(Option::getCategory));

		Random random = new Random();
		List<Option> selectOption = groupOption.entrySet().stream()
			.flatMap(e -> {
				Category key = e.getKey();
				List<Option> list = e.getValue();
				if (list == null || list.isEmpty()) {
					// 빈 스트림 반환
					return Stream.<Option>empty();
				}

				//중복 체크 가능한 경우
				if (key == Category.BED_TIME
					|| key == Category.WAKEUP_TIME
					|| key == Category.COOLING
					|| key == Category.HEATING) {

					int startIndex = random.nextInt(list.size());
					int count = random.nextInt(list.size()) + 1; // 최소 1개
					List<Option> sub = new ArrayList<>();
					for (int i = startIndex, k = 0; i < list.size() && k < count; i++, k++) {
						sub.add(list.get(i));
					}
					return sub.stream();
				} else {
					// 단일 선택인 경우
					Option pick = list.get(random.nextInt(list.size()));
					return Stream.of(pick);
				}
			})
			.toList();

		return selectOption;
	}
}
