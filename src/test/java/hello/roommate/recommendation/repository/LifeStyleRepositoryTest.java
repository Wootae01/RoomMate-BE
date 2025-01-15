package hello.roommate.recommendation.repository;

import java.util.NoSuchElementException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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

@Transactional
@DataJpaTest
class LifeStyleRepositoryTest {

	@Autowired
	LifeStyleRepository repository;

	@Test
	void save() {
		//given
		LifeStyle lifeStyle = createLifeStyle();

		//when
		LifeStyle save = repository.save(lifeStyle);

		//then
		Assertions.assertThat(save).isEqualTo(lifeStyle);
	}

	@Test
	void findById() {
		//given
		LifeStyle lifeStyle = createLifeStyle();

		LifeStyle save = repository.save(lifeStyle);
		LifeStyle find = repository.findById(save.getId()).orElseThrow();
		//then
		Assertions.assertThat(find).isEqualTo(save);

	}

	@Test
	void delete() {
		//given
		LifeStyle lifeStyle = createLifeStyle();
		repository.save(lifeStyle);

		//when
		repository.deleteById(lifeStyle.getId());

		// then
		Assertions.assertThatThrownBy(() -> repository.findById(lifeStyle.getId()).orElseThrow())
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