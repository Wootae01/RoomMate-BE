package hello.roommate.recommendation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Option;
import hello.roommate.recommendation.domain.enums.Category;
import hello.roommate.recommendation.dto.LifeStyleDto;
import hello.roommate.recommendation.repository.LifeStyleRepository;
import hello.roommate.recommendation.repository.OptionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LifestyleService {
	private final LifeStyleRepository lifeStyleRepository;
	private final OptionRepository optionRepository;
	private final MemberRepository memberRepository;

	public LifeStyle save(LifeStyle lifeStyle) {
		lifeStyleRepository.save(lifeStyle);
		return lifeStyle;
	}

	public LifeStyle findById(Long id) {
		return lifeStyleRepository.findById(id).orElseThrow();
	}

	public List<LifeStyle> findByMemberId(String memberId) {
		return lifeStyleRepository.findByMemberId(memberId);
	}

	public void saveAll(List<LifeStyle> lifeStyles) {
		lifeStyleRepository.saveAll(lifeStyles);
	}

	public void update(String memberId, LifeStyleDto dto) {

		List<LifeStyle> lifeStyles = lifeStyleRepository.findByMemberId(memberId);
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NoSuchElementException("Invalid Id"));
		List<LifeStyle> update = new ArrayList<>();

		Map<Category, List<LifeStyle>> map = lifeStyles.stream()
			.collect(Collectors.groupingBy(lifeStyle -> lifeStyle.getOption().getCategory()));

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

		if (dto.getNoise() != null) {
			updateLifeStyle(member, Category.NOISE, lifeStyles, dto.getNoise());
		}
		if (dto.getIndoorCall() != null) {
			updateLifeStyle(member, Category.INDOOR_CALL, lifeStyles, dto.getIndoorCall());
		}
		if (dto.getScent() != null) {
			updateLifeStyle(member, Category.SCENT, lifeStyles, dto.getScent());
		}
		if (dto.getDrinking() != null) {
			updateLifeStyle(member, Category.DRINKING, lifeStyles, dto.getDrinking());
		}
		if (dto.getEating() != null) {
			updateLifeStyle(member, Category.EATING, lifeStyles, dto.getEating());
		}
		if (dto.getCleaning() != null) {
			updateLifeStyle(member, Category.CLEANING, lifeStyles, dto.getCleaning());
		}
		if (dto.getRelationship() != null) {
			updateLifeStyle(member, Category.RELATIONSHIP, lifeStyles, dto.getRelationship());
		}
		if (dto.getSleepHabit() != null) {
			updateLifeStyle(member, Category.SLEEP_HABIT, lifeStyles, dto.getSleepHabit());
		}
		if (dto.getSmoking() != null) {
			updateLifeStyle(member, Category.SMOKING, lifeStyles, dto.getSmoking());
		}

		if (!update.isEmpty()) {
			lifeStyleRepository.saveAll(update);
		}
	}

	private void updateLifeStyle(Member member, Category category, List<LifeStyle> lifeStyles, String dto) {
		Option option = optionRepository.findByCategoryAndValue(category, dto);
		Optional<LifeStyle> optional = lifeStyles.stream().filter(l -> l.getOption().getCategory().equals(category))
			.findFirst();
		if (optional.isEmpty()) {
			LifeStyle lifeStyle = new LifeStyle(member, option);
			lifeStyleRepository.save(lifeStyle);
		} else {
			optional.get().setOption(option);
		}

	}

	private void addUpdateLifeStyle(Member member, Category category, List<String> dto, List<LifeStyle> update) {
		lifeStyleRepository.deleteByMemberAndOption(member.getId(), category);
		for (String optionValue : dto) {
			Option option = optionRepository.findByCategoryAndValue(category, optionValue);
			update.add(new LifeStyle(member, option));
		}
	}

	public void delete(Long id) {
		lifeStyleRepository.deleteById(id);
	}
}
