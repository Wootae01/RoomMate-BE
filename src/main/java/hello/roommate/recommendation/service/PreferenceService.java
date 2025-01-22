package hello.roommate.recommendation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
import hello.roommate.recommendation.domain.Option;
import hello.roommate.recommendation.domain.Preference;
import hello.roommate.recommendation.domain.enums.Category;
import hello.roommate.recommendation.dto.PreferenceDto;
import hello.roommate.recommendation.repository.OptionRepository;
import hello.roommate.recommendation.repository.PreferenceRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PreferenceService {
	private final PreferenceRepository preferenceRepository;
	private final MemberRepository memberRepository;
	private final OptionRepository optionRepository;

	public Preference save(Preference preference) {
		return preferenceRepository.save(preference);
	}

	public List<Preference> findByMemberId(String memberId) {
		return preferenceRepository.findByMemberId(memberId);
	}

	public void saveAll(List<Preference> preferences) {
		preferenceRepository.saveAll(preferences);
	}

	public void update(String memberId, PreferenceDto dto) {
		List<Preference> preferences = preferenceRepository.findByMemberId(memberId);
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NoSuchElementException("Invalid Id"));
		List<Preference> update = new ArrayList<>();

		Map<Category, List<Preference>> map = preferences.stream()
			.collect(Collectors.groupingBy(preference -> preference.getOption().getCategory()));

		if (!dto.getBedTimes().isEmpty()) {
			addUpdateLifeStyle(member, Category.BED_TIME, dto.getBedTimes(), update);
		}
		if (!dto.getWakeUpTimes().isEmpty()) {
			addUpdateLifeStyle(member, Category.WAKEUP_TIME, dto.getWakeUpTimes(), update);
		}
		if (!dto.getCoolings().isEmpty()) {
			addUpdateLifeStyle(member, Category.COOLING, dto.getCoolings(), update);
		}
		if (!dto.getHeatings().isEmpty()) {
			addUpdateLifeStyle(member, Category.HEATING, dto.getHeatings(), update);
		}

		if (!dto.getNoises().isEmpty()) {
			addUpdateLifeStyle(member, Category.NOISE, dto.getNoises(), update);
		}
		if (!dto.getIndoorCalls().isEmpty()) {
			addUpdateLifeStyle(member, Category.INDOOR_CALL, dto.getIndoorCalls(), update);
		}
		if (!dto.getScents().isEmpty()) {
			addUpdateLifeStyle(member, Category.SCENT, dto.getScents(), update);
		}
		if (!dto.getDrinkings().isEmpty()) {
			addUpdateLifeStyle(member, Category.DRINKING, dto.getDrinkings(), update);
		}
		if (!dto.getEatings().isEmpty()) {
			addUpdateLifeStyle(member, Category.EATING, dto.getEatings(), update);
		}
		if (!dto.getCleanings().isEmpty()) {
			addUpdateLifeStyle(member, Category.CLEANING, dto.getCleanings(), update);
		}
		if (!dto.getRelationships().isEmpty()) {
			addUpdateLifeStyle(member, Category.RELATIONSHIP, dto.getRelationships(), update);
		}
		if (dto.getSleepHabit() != null) {
			updateLifeStyle(Category.SLEEP_HABIT, preferences, dto.getSleepHabit());
		}
		if (dto.getSmoking() != null) {
			updateLifeStyle(Category.SMOKING, preferences, dto.getSmoking());
		}

		if (!update.isEmpty()) {
			preferenceRepository.saveAll(update);
		}
	}

	private void updateLifeStyle(Category category, List<Preference> preferences, String dto) {
		Option option = optionRepository.findByCategoryAndValue(category, dto);
		Preference preference = preferences.stream().filter(p -> p.getOption().getCategory().equals(category))
			.findFirst().orElseThrow();
		preference.setOption(option);
	}

	private void addUpdateLifeStyle(Member member, Category category, List<String> dto, List<Preference> update) {
		preferenceRepository.deleteByMemberAndOption(member.getId(), category);
		for (String optionValue : dto) {
			Option option = optionRepository.findByCategoryAndValue(category, optionValue);
			update.add(new Preference(member, option));
		}
	}
}
