package hello.roommate.recommendation.repository;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
import hello.roommate.profile.domain.Profile;
import hello.roommate.profile.repository.ProfileRepository;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Recommendation;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class RecommendationRepositoryTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private LifeStyleRepository lifeStyleRepository;
    @Autowired private ProfileRepository profileRepository;
    @Autowired private RecommendationRepository recommendationRepository;

    @Test
    void save() {
        //given
        LifeStyle lifeStyle = createLifeStyle();
        lifeStyleRepository.save(lifeStyle);

        Profile profile = createProfile(lifeStyle);
        profileRepository.save(profile);

        Member memberA = createMember("A", "1234", "email@A", Dormitory.INUI, profile, lifeStyle);
        Member memberB = createMember("B", "1234", "email@A", Dormitory.INUI, profile, lifeStyle);

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        Recommendation recommendation = createRecommendation(memberA, memberB, 0.96);

        //when
        Recommendation save = recommendationRepository.save(recommendation);

        //then
        assertThat(recommendation).isEqualTo(save);
    }

    @Test
    void findByMemberId() {
        //given
        LifeStyle lifeStyle = createLifeStyle();
        lifeStyleRepository.save(lifeStyle);

        Profile profile = createProfile(lifeStyle);
        profileRepository.save(profile);

        Member memberA = createMember("A", "1234", "email@A", Dormitory.INUI, profile, lifeStyle);
        Member memberB = createMember("B", "1234", "email@A", Dormitory.INUI, profile, lifeStyle);
        Member memberC = createMember("C", "1234", "email@C", Dormitory.INUI, profile, lifeStyle);

        memberRepository.save(memberA);
        memberRepository.save(memberB);
        memberRepository.save(memberC);

        Recommendation recommendation = createRecommendation(memberA, memberB, 0.96);
        Recommendation recommendation1 = createRecommendation(memberA, memberC, 0.97);
        recommendationRepository.save(recommendation);
        recommendationRepository.save(recommendation1);
        //when
        List<Recommendation> find = recommendationRepository.findByMemberId(memberA.getId());

        assertThat(find.size()).isEqualTo(2);
        assertThat(find).contains(recommendation, recommendation1);
        assertThat(find.getFirst()).isEqualTo(recommendation1);
    }

    @Test
    void update() {
        //given
        LifeStyle lifeStyle = createLifeStyle();
        lifeStyleRepository.save(lifeStyle);

        Profile profile = createProfile(lifeStyle);
        profileRepository.save(profile);

        Member memberA = createMember("A", "1234", "email@A", Dormitory.INUI, profile, lifeStyle);
        Member memberB = createMember("B", "1234", "email@A", Dormitory.INUI, profile, lifeStyle);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        Recommendation recommendation = createRecommendation(memberA, memberB, 0.96);
        recommendationRepository.save(recommendation);

        //when
        RecommendationUpdateDto dto = new RecommendationUpdateDto();
        dto.setMemberId(memberA.getId());
        dto.setMatchedMemberId(memberB.getId());
        dto.setScore(0.55);
        recommendationRepository.update(dto);

        //then
        List<Recommendation> find = recommendationRepository.findByMemberId(memberA.getId());
        Recommendation first = find.getFirst();
        assertThat(first.getScore()).isEqualTo(0.55);


    }

    @Test
    void delete() {
        //given
        LifeStyle lifeStyle = createLifeStyle();
        lifeStyleRepository.save(lifeStyle);

        Profile profile = createProfile(lifeStyle);
        profileRepository.save(profile);

        Member memberA = createMember("A", "1234", "email@A", Dormitory.INUI, profile, lifeStyle);
        Member memberB = createMember("B", "1234", "email@A", Dormitory.INUI, profile, lifeStyle);
        Member memberC = createMember("C", "1234", "email@C", Dormitory.INUI, profile, lifeStyle);
        memberRepository.save(memberA);
        memberRepository.save(memberB);
        memberRepository.save(memberC);

        Recommendation recommendation = createRecommendation(memberA, memberB, 0.96);
        Recommendation recommendation1 = createRecommendation(memberA, memberC, 0.97);
        recommendationRepository.save(recommendation);
        recommendationRepository.save(recommendation1);


    }

    private Recommendation createRecommendation(Member memberA, Member memberB, double score) {
        Recommendation recommendation = new Recommendation();
        recommendation.setMember(memberA);
        recommendation.setMatchedMember(memberB);
        recommendation.setScore(score);
        return recommendation;
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

    private LifeStyle createLifeStyle(int bedTime, int wakeupTime, int sleepHabit, int cleaning,
                                      int aircon, int heater, int noise, int smoking,
                                      int scent, int eating, int relationship,
                                      int home, int drinking, int age, int dormHour) {

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