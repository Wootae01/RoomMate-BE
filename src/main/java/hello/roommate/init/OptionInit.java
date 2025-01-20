package hello.roommate.init;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import hello.roommate.recommendation.domain.Option;
import hello.roommate.recommendation.domain.enums.BedTime;
import hello.roommate.recommendation.domain.enums.Category;
import hello.roommate.recommendation.domain.enums.Cleaning;
import hello.roommate.recommendation.domain.enums.Cooling;
import hello.roommate.recommendation.domain.enums.Drinking;
import hello.roommate.recommendation.domain.enums.Eating;
import hello.roommate.recommendation.domain.enums.Heating;
import hello.roommate.recommendation.domain.enums.IndoorCall;
import hello.roommate.recommendation.domain.enums.Noise;
import hello.roommate.recommendation.domain.enums.Relationship;
import hello.roommate.recommendation.domain.enums.Scent;
import hello.roommate.recommendation.domain.enums.SleepHabit;
import hello.roommate.recommendation.domain.enums.Smoking;
import hello.roommate.recommendation.domain.enums.WakeUpTime;
import hello.roommate.recommendation.repository.OptionRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OptionInit {
	private final OptionRepository optionRepository;

	public void createOption() {
		if (optionRepository.count() == 0) { // 중복 삽입 방지
			List<Option> options = Arrays.asList(
				// 취침시간
				new Option(Category.BED_TIME, BedTime.AT_22.name()),
				new Option(Category.BED_TIME, BedTime.AT_23.name()),
				new Option(Category.BED_TIME, BedTime.AT_00.name()),
				new Option(Category.BED_TIME, BedTime.AT_01.name()),
				new Option(Category.BED_TIME, BedTime.AT_02.name()),
				new Option(Category.BED_TIME, BedTime.AT_03.name()),

				// 기상시간
				new Option(Category.WAKEUP_TIME, WakeUpTime.AT_06.name()),
				new Option(Category.WAKEUP_TIME, WakeUpTime.AT_07.name()),
				new Option(Category.WAKEUP_TIME, WakeUpTime.AT_08.name()),
				new Option(Category.WAKEUP_TIME, WakeUpTime.AT_09.name()),
				new Option(Category.WAKEUP_TIME, WakeUpTime.AT_10.name()),
				new Option(Category.WAKEUP_TIME, WakeUpTime.AT_11.name()),

				// 청소 방식
				new Option(Category.CLEANING, Cleaning.INDIVIDUAL.name()),
				new Option(Category.CLEANING, Cleaning.ROTATION.name()),
				new Option(Category.CLEANING, Cleaning.TOGETHER.name()),

				// 냉방 설정
				new Option(Category.COOLING, Cooling.BELOW_20.name()),
				new Option(Category.COOLING, Cooling.FROM_21_TO_23.name()),
				new Option(Category.COOLING, Cooling.FROM_24_TO_26.name()),
				new Option(Category.COOLING, Cooling.ABOVE_27.name()),

				// 난방 설정
				new Option(Category.HEATING, Heating.BELOW_20.name()),
				new Option(Category.HEATING, Heating.FROM_21_TO_23.name()),
				new Option(Category.HEATING, Heating.FROM_24_TO_26.name()),
				new Option(Category.HEATING, Heating.ABOVE_27.name()),

				// 소음 민감도
				new Option(Category.NOISE, Noise.EARPHONES.name()),
				new Option(Category.NOISE, Noise.FLEXIBLE.name()),
				new Option(Category.NOISE, Noise.SPEAKERS.name()),

				// 흡연 여부
				new Option(Category.SMOKING, Smoking.NON_SMOKER.name()),
				new Option(Category.SMOKING, Smoking.SMOKER.name()),

				// 향 민감도
				new Option(Category.SCENT, Scent.SENSITIVE.name()),
				new Option(Category.SCENT, Scent.NORMAL.name()),
				new Option(Category.SCENT, Scent.NOT_SENSITIVE.name()),

				// 실내 통화
				new Option(Category.INDOOR_CALL, IndoorCall.BAN.name()),
				new Option(Category.INDOOR_CALL, IndoorCall.SIMPLE.name()),
				new Option(Category.INDOOR_CALL, IndoorCall.FREE.name()),

				// 실내 취식
				new Option(Category.EATING, Eating.BAN.name()),
				new Option(Category.EATING, Eating.SNACK.name()),
				new Option(Category.EATING, Eating.FOOD.name()),

				// 음주 여부
				new Option(Category.DRINKING, Drinking.NON_DRINKER.name()),
				new Option(Category.DRINKING, Drinking.OCCASIONAL.name()),
				new Option(Category.DRINKING, Drinking.FREQUENT.name()),

				// 룸메 관계
				new Option(Category.RELATIONSHIP, Relationship.INVISIBLE.name()),
				new Option(Category.RELATIONSHIP, Relationship.NORMAL.name()),
				new Option(Category.RELATIONSHIP, Relationship.CLOSE.name()),

				// 잠버릇
				new Option(Category.SLEEP_HABIT, SleepHabit.YES.name()),
				new Option(Category.SLEEP_HABIT, SleepHabit.NO.name())
			);

			optionRepository.saveAll(options);
		}
	}
}
