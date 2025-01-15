package hello.roommate.recommendation.service;

import java.util.NoSuchElementException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.enums.BedTime;
import hello.roommate.recommendation.domain.enums.Cleaning;
import hello.roommate.recommendation.domain.enums.Cooling;
import hello.roommate.recommendation.domain.enums.Drinking;
import hello.roommate.recommendation.domain.enums.Eating;
import hello.roommate.recommendation.domain.enums.Heating;
import hello.roommate.recommendation.domain.enums.Noise;
import hello.roommate.recommendation.domain.enums.Relationship;
import hello.roommate.recommendation.domain.enums.Scent;
import hello.roommate.recommendation.domain.enums.SleepHabit;
import hello.roommate.recommendation.domain.enums.Smoking;
import hello.roommate.recommendation.domain.enums.WakeUpTime;
import hello.roommate.recommendation.dto.LifeStyleUpdateDto;

@Transactional
@SpringBootTest
class LifeStyleServiceTest {

	@Autowired
	LifestyleService service;

	@Test
	void save() {
		//given
		LifeStyle lifeStyle = createLifeStyle();

		//when
		LifeStyle save = service.save(lifeStyle);

		//then
		Assertions.assertThat(save).isEqualTo(lifeStyle);
	}

	@Test
	void findById() {
		//given
		LifeStyle lifeStyle = createLifeStyle();

		LifeStyle save = service.save(lifeStyle);
		LifeStyle find = service.findById(save.getId());
		//then
		Assertions.assertThat(find).isEqualTo(save);

	}

	@Test
	void update() {
		//given
		LifeStyle lifeStyle = createLifeStyle();

		service.save(lifeStyle);
		LifeStyleUpdateDto dto = new LifeStyleUpdateDto();
		dto.setBedTime(BedTime.AT_02);              // 예: 취침 시간이 2시 이후
		dto.setWakeupTime(WakeUpTime.AT_07);           // 예: 기상 시간이 7시
		dto.setSleepHabit(SleepHabit.YES);            // 예: 잠버릇 있음
		dto.setCleaning(Cleaning.ROTATION);           // 예: 청소 교대
		dto.setCooling(Cooling.BELOW_20);              // 예: 냉방 온도 20도 이하
		dto.setHeater(Heating.FROM_21_TO_23);         // 예: 난방 온도 21~23도
		dto.setNoise(Noise.EARPHONES);                // 예: 이어폰 사용
		dto.setSmoking(Smoking.NON_SMOKER);           // 예: 비흡연자
		dto.setScent(Scent.NORMAL);                   // 예: 향에 민감하지 않음
		dto.setEating(Eating.FOOD);    // 예: 배달 음식 허용
		dto.setRelationship(Relationship.NORMAL);   // 예: 룸메이트와 적당히 친밀한 관계
		dto.setDrinking(Drinking.OCCASIONAL);         // 예: 가끔 음주
		dto.setAge(25);                               // 예: 나이 25

		//when
		service.update(lifeStyle.getId(), dto);
		LifeStyle update = service.findById(lifeStyle.getId());

		//then
		Assertions.assertThat(update.getBedTime()).isEqualTo(dto.getBedTime());
		Assertions.assertThat(update.getWakeupTime()).isEqualTo(dto.getWakeupTime());
		Assertions.assertThat(update.getSleepHabit()).isEqualTo(dto.getSleepHabit());
		Assertions.assertThat(update.getCleaning()).isEqualTo(dto.getCleaning());
		Assertions.assertThat(update.getCooling()).isEqualTo(dto.getCooling());
		Assertions.assertThat(update.getHeater()).isEqualTo(dto.getHeater());
		Assertions.assertThat(update.getNoise()).isEqualTo(dto.getNoise());
		Assertions.assertThat(update.getSmoking()).isEqualTo(dto.getSmoking());
		Assertions.assertThat(update.getScent()).isEqualTo(dto.getScent());
		Assertions.assertThat(update.getEating()).isEqualTo(dto.getEating());
		Assertions.assertThat(update.getRelationship()).isEqualTo(dto.getRelationship());
		Assertions.assertThat(update.getDrinking()).isEqualTo(dto.getDrinking());
		Assertions.assertThat(update.getAge()).isEqualTo(dto.getAge());

	}

	@Test
	void delete() {
		//given
		LifeStyle lifeStyle = createLifeStyle();
		service.save(lifeStyle);

		//when
		service.delete(lifeStyle.getId());

		// then
		Assertions.assertThatThrownBy(() -> service.findById(lifeStyle.getId()))
			.isInstanceOf(NoSuchElementException.class);
	}

	private LifeStyle createLifeStyle() {
		LifeStyle lifeStyle = new LifeStyle();
		lifeStyle.setBedTime(BedTime.AT_02);
		lifeStyle.setWakeupTime(WakeUpTime.AT_09);
		lifeStyle.setSleepHabit(SleepHabit.YES);
		lifeStyle.setCleaning(Cleaning.TOGETHER);
		lifeStyle.setCooling(Cooling.FROM_24_TO_26);
		lifeStyle.setHeater(Heating.FROM_21_TO_23);
		lifeStyle.setNoise(Noise.SPEAKERS);
		lifeStyle.setSmoking(Smoking.NON_SMOKER);
		lifeStyle.setScent(Scent.SENSITIVE);
		lifeStyle.setEating(Eating.FOOD);
		lifeStyle.setRelationship(Relationship.CLOSE);
		lifeStyle.setDrinking(Drinking.OCCASIONAL);
		lifeStyle.setAge(22);
		return lifeStyle;
	}
}