package hello.roommate.recommendation.repository;

import hello.roommate.recommendation.domain.LifeStyle;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Transactional
@SpringBootTest
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
        lifeStyle.setBedTime(5);
        lifeStyle.setWakeupTime(5);
        lifeStyle.setSleepHabit(4);
        lifeStyle.setCleaning(4);
        lifeStyle.setAircon(4);
        lifeStyle.setHeater(3);
        lifeStyle.setNoise(2);
        lifeStyle.setSmoking(4);
        lifeStyle.setScent(5);
        lifeStyle.setEating(4);
        lifeStyle.setRelationship(2);
        lifeStyle.setDrinking(5);
        lifeStyle.setAge(4);
        return lifeStyle;
    }
}