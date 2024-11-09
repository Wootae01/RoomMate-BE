package hello.roommate.recommendation.service;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
import hello.roommate.profile.domain.Profile;
import hello.roommate.profile.repository.ProfileRepository;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Recommendation;
import hello.roommate.recommendation.repository.LifeStyleRepository;
import hello.roommate.recommendation.repository.RecommendationRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class RecommendationServiceTest {

    @Autowired private RecommendationService service;
    @Autowired private RecommendationRepository recommendationRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private LifeStyleRepository lifeStyleRepository;
    @Autowired private ProfileRepository profileRepository;
    @Test
    void findLiveRecommendations() {
        //given
        LifeStyle lifeStyleA = createLifeStyle(10, 8, 7, 5, 6, 4, 3, 2, 1, 5, 3, 7, 4, 20, 6);
        LifeStyle lifeStyleB = createLifeStyle(5, 6, 5, 6, 7, 4, 3, 1, 2, 4, 6, 8, 5, 21, 7);
        LifeStyle lifeStyleC = createLifeStyle(8, 7, 6, 5, 4, 3, 5, 7, 6, 5, 4, 2, 1, 19, 5);
        LifeStyle lifeStyleD = createLifeStyle(4, 5, 4, 3, 2, 1, 6, 7, 8, 5, 3, 6, 7, 22, 8);
        lifeStyleRepository.save(lifeStyleA);
        lifeStyleRepository.save(lifeStyleB);
        lifeStyleRepository.save(lifeStyleC);
        lifeStyleRepository.save(lifeStyleD);

        Profile profileA = createProfile(lifeStyleA);
        Profile profileB = createProfile(lifeStyleB);
        Profile profileC = createProfile(lifeStyleC);
        Profile profileD = createProfile(lifeStyleD);
        profileRepository.save(profileA);
        profileRepository.save(profileB);
        profileRepository.save(profileC);
        profileRepository.save(profileD);

        Member memberA = createMember("A", "aaaa", "a@naver.com", Dormitory.INUI, profileA, lifeStyleA);
        Member memberB = createMember("B", "bbbb", "b@naver.com", Dormitory.INUI, profileB, lifeStyleB);
        Member memberC = createMember("C", "cccc", "c@naver.com", Dormitory.INUI, profileC, lifeStyleC);
        Member memberD = createMember("D", "dddd", "d@naver.com", Dormitory.INUI, profileD, lifeStyleD);
        memberRepository.save(memberA);
        memberRepository.save(memberB);
        memberRepository.save(memberC);
        memberRepository.save(memberD);

        //when
        List<Recommendation> recommendations = service.findLiveRecommendations("A");
        for (Recommendation recommendation : recommendations) {
            log.info("(id, score) = ({},{})", recommendation.getMatchedMember().getId(), recommendation.getScore());
        }

        //then
        assertThat(recommendations.size()).isEqualTo(3);

        //멤버정렬 확인
        assertThat(recommendations.get(0).getScore()).isGreaterThan(recommendations.get(1).getScore());
        assertThat(recommendations.get(1).getScore()).isGreaterThan(recommendations.get(2).getScore());

        assertThat(recommendations.get(0).getMatchedMember()).isEqualTo(memberB);
        assertThat(recommendations.get(1).getMatchedMember()).isEqualTo(memberC);
        assertThat(recommendations.get(2).getMatchedMember()).isEqualTo(memberD);

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

    private LifeStyle createLifeStyle(
            int bedTime, int wakeupTime, int sleepHabit, int cleaning, int aircon,
            int heater, int noise, int smoking, int scent, int eating,
            int relationship, int home, int drinking, int age, int dormHour) {

        LifeStyle lifeStyle = new LifeStyle();
        lifeStyle.setBedTime(bedTime);
        lifeStyle.setWakeupTime(wakeupTime);
        lifeStyle.setSleepHabit(sleepHabit);
        lifeStyle.setCleaning(cleaning);
        lifeStyle.setAircon(aircon);
        lifeStyle.setHeater(heater);
        lifeStyle.setNoise(noise);
        lifeStyle.setSmoking(smoking);
        lifeStyle.setScent(scent);
        lifeStyle.setEating(eating);
        lifeStyle.setRelationship(relationship);
        lifeStyle.setHome(home);
        lifeStyle.setDrinking(drinking);
        lifeStyle.setAge(age);
        lifeStyle.setDormHour(dormHour);

        return lifeStyle;
    }


    private int randomValue() {
        return (int)(Math.random()*5) + 1;
    }
}