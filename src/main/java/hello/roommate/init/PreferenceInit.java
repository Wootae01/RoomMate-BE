package hello.roommate.init;

import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
import hello.roommate.recommendation.domain.Option;
import hello.roommate.recommendation.domain.Preference;
import hello.roommate.recommendation.domain.enums.Category;
import hello.roommate.recommendation.repository.LifeStyleRepository;
import hello.roommate.recommendation.repository.OptionRepository;
import hello.roommate.recommendation.repository.PreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

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
                Preference preference = new Preference(option, member);
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
                    if (list == null || list.isEmpty()) return null;
                    else {
                        return list.get(new Random().nextInt(list.size()));
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        return result;
    }
}
