package hello.roommate.init;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import hello.roommate.recommendation.domain.Option;
import hello.roommate.recommendation.domain.enums.Age;
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
				new Option(101L, Category.BED_TIME, BedTime.AT_22.name()),
				new Option(102L, Category.BED_TIME, BedTime.AT_23.name()),
				new Option(103L, Category.BED_TIME, BedTime.AT_00.name()),
				new Option(104L, Category.BED_TIME, BedTime.AT_01.name()),
				new Option(105L, Category.BED_TIME, BedTime.AT_02.name()),
				new Option(106L, Category.BED_TIME, BedTime.AT_03.name()),

				// 기상시간
				new Option(201L, Category.WAKEUP_TIME, WakeUpTime.AT_06.name()),
				new Option(202L, Category.WAKEUP_TIME, WakeUpTime.AT_07.name()),
				new Option(203L, Category.WAKEUP_TIME, WakeUpTime.AT_08.name()),
				new Option(204L, Category.WAKEUP_TIME, WakeUpTime.AT_09.name()),
				new Option(205L, Category.WAKEUP_TIME, WakeUpTime.AT_10.name()),
				new Option(206L, Category.WAKEUP_TIME, WakeUpTime.AT_11.name()),

				// 난방 설정
				new Option(301L, Category.HEATING, Heating.BELOW_20.name()),
				new Option(302L, Category.HEATING, Heating.FROM_21_TO_23.name()),
				new Option(303L, Category.HEATING, Heating.FROM_24_TO_26.name()),
				new Option(304L, Category.HEATING, Heating.ABOVE_27.name()),

				// 냉방 설정
				new Option(401L, Category.COOLING, Cooling.BELOW_20.name()),
				new Option(402L, Category.COOLING, Cooling.FROM_21_TO_23.name()),
				new Option(403L, Category.COOLING, Cooling.FROM_24_TO_26.name()),
				new Option(404L, Category.COOLING, Cooling.ABOVE_27.name()),

				// 잠버릇
				new Option(501L, Category.SLEEP_HABIT, SleepHabit.YES.name()),
				new Option(502L, Category.SLEEP_HABIT, SleepHabit.NO.name()),

				// 흡연 여부
				new Option(601L, Category.SMOKING, Smoking.NON_SMOKER.name()),
				new Option(603L, Category.SMOKING, Smoking.SMOKER.name()),

				// 소음 민감도
				new Option(701L, Category.NOISE, Noise.EARPHONES.name()),
				new Option(702L, Category.NOISE, Noise.FLEXIBLE.name()),
				new Option(703L, Category.NOISE, Noise.SPEAKERS.name()),

				// 실내 통화
				new Option(801L, Category.INDOOR_CALL, IndoorCall.BAN.name()),
				new Option(802L, Category.INDOOR_CALL, IndoorCall.SIMPLE.name()),
				new Option(803L, Category.INDOOR_CALL, IndoorCall.FREE.name()),

				// 실내 취식
				new Option(901L, Category.EATING, Eating.BAN.name()),
				new Option(902L, Category.EATING, Eating.SNACK.name()),
				new Option(903L, Category.EATING, Eating.FOOD.name()),

				// 음주 빈도
				new Option(1001L, Category.DRINKING, Drinking.NON_DRINKER.name()),
				new Option(1002L, Category.DRINKING, Drinking.OCCASIONAL.name()),
				new Option(1003L, Category.DRINKING, Drinking.FREQUENT.name()),

				// 향 민감도
				new Option(1101L, Category.SCENT, Scent.SENSITIVE.name()),
				new Option(1102L, Category.SCENT, Scent.NORMAL.name()),
				new Option(1103L, Category.SCENT, Scent.NOT_SENSITIVE.name()),

				// 청소 방식
				new Option(1201L, Category.CLEANING, Cleaning.INDIVIDUAL.name()),
				new Option(1202L, Category.CLEANING, Cleaning.ROTATION.name()),
				new Option(1203L, Category.CLEANING, Cleaning.TOGETHER.name()),

				// 룸메 관계
				new Option(1301L, Category.RELATIONSHIP, Relationship.INVISIBLE.name()),
				new Option(1302L, Category.RELATIONSHIP, Relationship.NORMAL.name()),
				new Option(1303L, Category.RELATIONSHIP, Relationship.CLOSE.name()),

				//나이
				new Option(1401L, Category.AGE, Age.TWENTY.name()),
				new Option(1402L, Category.AGE, Age.TWENTY_ONE.name()),
				new Option(1403L, Category.AGE, Age.TWENTY_TWO.name()),
				new Option(1404L, Category.AGE, Age.TWENTY_THREE.name()),
				new Option(1405L, Category.AGE, Age.TWENTY_FOUR.name()),
				new Option(1406L, Category.AGE, Age.TWENTY_FIVE.name()),
				new Option(1407L, Category.AGE, Age.TWENTY_SIX.name()),
				new Option(1408L, Category.AGE, Age.TWENTY_SEVEN.name()),
				new Option(1409L, Category.AGE, Age.TWENTY_EIGHT.name())
			);

			optionRepository.saveAll(options);
		}
	}
}
