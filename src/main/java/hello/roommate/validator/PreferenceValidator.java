package hello.roommate.validator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hello.roommate.recommendation.domain.enums.Category;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 생활 패턴 검증을 하기 위한 Validator
 * Preference에 있는 모든 카테고리에 하나 이상의 값이 존재해야 한다.
 */
public class PreferenceValidator implements ConstraintValidator<ValidPreference, Map<String, List<Long>>> {
	@Override
	public boolean isValid(Map<String, List<Long>> preferencMap,
		ConstraintValidatorContext constraintValidatorContext) {
		if (preferencMap == null) {
			return false;
		}

		//모든 카테고리가 포함되어 있는지 검증할 변수
		Map<String, Boolean> checkMap = Arrays.stream(Category.values())
			.collect(Collectors.toMap(Enum::name, category -> false));

		preferencMap.keySet()
			.stream()
			.forEach(key -> {
				List<Long> values = preferencMap.get(key);
				if (!values.isEmpty()) {
					checkMap.put(key, true);
				}
			});

		List<Boolean> list = checkMap.values().stream().toList();
		for (Boolean b : list) {
			if (b == false) {
				return false;
			}
		}

		return true;
	}
}
