package hello.roommate.recommendation.service;

import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.dto.LifeStyleUpdateDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

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
        dto.setBedTime(5);
        dto.setWakeupTime(3);
        dto.setSleepHabit(4);
        dto.setCleaning(2);
        dto.setAircon(1);
        dto.setHeater(3);
        dto.setNoise(2);
        dto.setSmoking(4);
        dto.setScent(2);
        dto.setEating(4);
        dto.setRelationship(2);
        dto.setDrinking(5);
        dto.setAge(4);

        //when
        service.update(lifeStyle.getId(), dto);
        LifeStyle update = service.findById(lifeStyle.getId());

        //then
        Assertions.assertThat(update.getBedTime()).isEqualTo(dto.getBedTime());
        Assertions.assertThat(update.getWakeupTime()).isEqualTo(dto.getWakeupTime());
        Assertions.assertThat(update.getSleepHabit()).isEqualTo(dto.getSleepHabit());
        Assertions.assertThat(update.getCleaning()).isEqualTo(dto.getCleaning());
        Assertions.assertThat(update.getAircon()).isEqualTo(dto.getAircon());
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