package hello.roommate.member.repository;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Member;
import hello.roommate.profile.domain.Profile;
import hello.roommate.profile.repository.ProfileRepository;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.repository.LifeStyleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private LifeStyleRepository lifeStyleRepository;
    @Autowired private ProfileRepository profileRepository;
    @Test
    void save() {
        //given
        LifeStyle lifeStyle = createLifeStyle();
        lifeStyleRepository.save(lifeStyle);

        Profile profile = createProfile(lifeStyle);
        profileRepository.save(profile);

        Member member = createMember(profile, lifeStyle);

        //when
        Member save = memberRepository.save(member);

        //then
        assertThat(save).isEqualTo(member);
        assertThat(save.getLifeStyle()).isEqualTo(member.getLifeStyle());
    }

    @Test
    void findById() {
        //given
        LifeStyle lifeStyle = createLifeStyle();
        lifeStyleRepository.save(lifeStyle);

        Profile profile = createProfile(lifeStyle);
        profileRepository.save(profile);

        Member member = createMember(profile, lifeStyle);
        memberRepository.save(member);

        //when
        Member find = memberRepository.findById(member.getId());

        //then
        assertThat(find).isEqualTo(member);

    }

    @Test
    void findByDorm() {
        //given
        LifeStyle lifeStyle = createLifeStyle();
        lifeStyleRepository.save(lifeStyle);

        Profile profile = createProfile(lifeStyle);
        profileRepository.save(profile);

        Member memberA = createMember("A", "1234", "1234@naver.com", Dormitory.INUI, profile, lifeStyle);
        Member memberB = createMember("B", "5678", "5678@naver.com", Dormitory.YEJI, profile, lifeStyle);
        Member memberC = createMember("C", "5678", "C@naver.com", Dormitory.INUI, profile, lifeStyle);

        memberRepository.save(memberA);
        memberRepository.save(memberB);
        memberRepository.save(memberC);

        //when
        List<Member> inui = memberRepository.findByDorm(Dormitory.INUI.name());
        List<Member> yeji = memberRepository.findByDorm(Dormitory.YEJI.name());

        //then
        assertThat(inui.size()).isEqualTo(2);
        assertThat(yeji.size()).isEqualTo(1);

        assertThat(inui).contains(memberA, memberC);
        assertThat(yeji).contains(memberB);
    }

    @Test
    void delete() {
        //given
        LifeStyle lifeStyle = createLifeStyle();
        lifeStyleRepository.save(lifeStyle);

        Profile profile = createProfile(lifeStyle);
        profileRepository.save(profile);

        Member member = createMember(profile, lifeStyle);

        //when
        memberRepository.delete(member.getId());

        //then
        assertThatThrownBy(() -> {
            memberRepository.findById(member.getId());
        }).isInstanceOf(EmptyResultDataAccessException.class);
    }

    private Member createMember(String id, String password, String email, Dormitory dorm, Profile profile, LifeStyle lifeStyle) {
        Member member =  new Member();
        member.setId(id);
        member.setPassword(password);
        member.setEmail(email);

        member.setDorm(dorm);

        member.setProfile(profile);
        member.setLifeStyle(lifeStyle);
        return member;
    }

    private Member createMember(Profile profile, LifeStyle lifeStyle) {
       Member member =  new Member();
        member.setPassword("1234");
        member.setEmail("1234@naver.com");
        member.setId("A");
        member.setDorm(Dormitory.INUI);

        member.setProfile(profile);
        member.setLifeStyle(lifeStyle);
        return member;
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