package hello.roommate.profile.repository;

import hello.roommate.profile.domain.Profile;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.repository.LifeStyleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ProfileRepositoryTest {

    @Autowired private ProfileRepository profileRepository;
    @Autowired private LifeStyleRepository lifeStyleRepository;
    @Test
    void save() {
        //given
        LifeStyle lifeStyle = createLifeStyle();
        lifeStyleRepository.save(lifeStyle);
        Profile profile = createProfile(lifeStyle);

        //when
        Profile save = profileRepository.save(profile);


        //then
        assertThat(save).isEqualTo(profile);
    }

    @Test
    void findById() {
        //given
        LifeStyle lifeStyle = createLifeStyle();
        lifeStyleRepository.save(lifeStyle);

        Profile profile = createProfile(lifeStyle);
        Profile save = profileRepository.save(profile);

        //when
        Profile find = profileRepository.findById(profile.getId());

        //then
        assertThat(find).isEqualTo(profile);
        assertThat(find.getLifeStyle()).isEqualTo(lifeStyle);
    }

    @Test
    void findByNickname() {
        //given
        LifeStyle lifeStyle = createLifeStyle();
        lifeStyleRepository.save(lifeStyle);

        Profile profile = createProfile(lifeStyle);
        Profile save = profileRepository.save(profile);

        //when
        Profile find = profileRepository.findByNickname(profile.getNickname());

        //then
        assertThat(find).isEqualTo(profile);
        assertThat(find.getLifeStyle()).isEqualTo(lifeStyle);
    }

    @Test
    void update() {
        //given
        LifeStyle lifeStyle = createLifeStyle();
        lifeStyleRepository.save(lifeStyle);

        Profile profile = createProfile(lifeStyle);
        profileRepository.save(profile);
        ProfileUpdateDto updateDto = new ProfileUpdateDto();
        updateDto.setImg("www.img2.com");
        updateDto.setIntroduce("introduce myself");

        //when
        profileRepository.update(profile.getNickname(), updateDto);

        //then
        Profile updated = profileRepository.findByNickname(profile.getNickname());
        assertThat(updated.getImg()).isEqualTo(updateDto.getImg());
        assertThat(updated.getIntroduce()).isEqualTo(updateDto.getIntroduce());
    }

    @Test
    void deleteById() {
        //given
        LifeStyle lifeStyle = createLifeStyle();
        lifeStyleRepository.save(lifeStyle);

        Profile profile = createProfile(lifeStyle);
        profileRepository.save(profile);

        //when
        profileRepository.deleteById(profile.getId());

        //then
        assertThatThrownBy(() -> {
            profileRepository.findById(profile.getId());
        }).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void deleteByNickname() {
        //given
        LifeStyle lifeStyle = createLifeStyle();
        lifeStyleRepository.save(lifeStyle);

        Profile profile = createProfile(lifeStyle);
        profileRepository.save(profile);

        //when
        profileRepository.deleteByNickname(profile.getNickname());

        //then
        assertThatThrownBy(() -> {
            profileRepository.findByNickname(profile.getNickname());
        }).isInstanceOf(EmptyResultDataAccessException.class);
    }

    private Profile createProfile(LifeStyle lifeStyle) {
        Profile profile = new Profile();
        profile.setLifeStyle(lifeStyle);
        profile.setImg("www.img.com");
        profile.setIntroduce("Hello 안녕 ㅎㅇ ");
        profile.setNickname("Kim");
        return profile;
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