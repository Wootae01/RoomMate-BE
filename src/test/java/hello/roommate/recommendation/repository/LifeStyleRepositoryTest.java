package hello.roommate.recommendation.repository;

import hello.roommate.recommendation.domain.LifeStyle;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.hibernate.query.sqm.tree.SqmNode.log;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class LifeStyleRepositoryTest {

    @Autowired LifeStyleRepository repository;
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
        LifeStyle find = repository.findById(save.getId());
        //then
        Assertions.assertThat(find).isEqualTo(save);

    }

    @Test
    void update() {
        //given
        LifeStyle lifeStyle = createLifeStyle();

        repository.save(lifeStyle);
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
        dto.setHome(1);
        dto.setDrinking(5);
        dto.setAge(4);
        dto.setDormHour(3);

        //when
        repository.update(lifeStyle.getId(), dto);
        LifeStyle update = repository.findById(lifeStyle.getId());

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
        Assertions.assertThat(update.getHome()).isEqualTo(dto.getHome());
        Assertions.assertThat(update.getDrinking()).isEqualTo(dto.getDrinking());
        Assertions.assertThat(update.getAge()).isEqualTo(dto.getAge());
        Assertions.assertThat(update.getDormHour()).isEqualTo(dto.getDormHour());

    }

    @Test
    void delete() {
        //given
        LifeStyle lifeStyle = createLifeStyle();
        repository.save(lifeStyle);

        //when
        repository.delete(lifeStyle.getId());

        // then
        LifeStyle find = repository.findById(lifeStyle.getId());
        Assertions.assertThat(find).isNull();
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
        lifeStyle.setHome(5);
        lifeStyle.setDrinking(5);
        lifeStyle.setAge(4);
        lifeStyle.setDormHour(3);
        return lifeStyle;
    }
}