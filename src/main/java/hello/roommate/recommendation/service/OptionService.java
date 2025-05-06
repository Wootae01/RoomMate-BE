package hello.roommate.recommendation.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.recommendation.domain.Option;
import hello.roommate.recommendation.domain.enums.Category;
import hello.roommate.recommendation.repository.OptionRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OptionService {

	private final OptionRepository optionRepository;

	public Option findByCategoryAndValue(Category category, String value) {
		return optionRepository.findByCategoryAndValue(category, value);
	}

	/**
	 * 년도 바뀌면 age option 추가
	 */
	@Scheduled(cron = "0 0 0 1 1 *")
	protected void updateOption() {
		int year = LocalDate.now().getYear();
		Option option = new Option((long)year, Category.AGE, String.valueOf(year));
		optionRepository.save(option);
	}

	public List<Option> findAll() {
		return optionRepository.findAll();
	}

	public Map<Long, Integer> getOpionIdxMap(List<Option> options) {
		List<Option> sorted = options.stream()
			.filter(option -> option.getId() < 1900 && option.getId() > 100)
			.sorted(Comparator.comparingLong(Option::getId))
			.toList();

		Map<Long, Integer> optionIdx = new HashMap<>();
		int index = 0;
		for (Option option : sorted) {
			optionIdx.put(option.getId(), index++);
		}
		return optionIdx;
	}
}
