package hello.roommate.recommendation.repository;

import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
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
    @Autowired private RecommendationRepository recommendationRepository;

    @Test
    void save() {
        //given
        Recommendation recommendation = new Recommendation();
        Member member = new Member();
        recommendation.setScore(0.986);
        //when
        Recommendation save = recommendationRepository.save(recommendation);

        //then
        Assertions.assertThat(save).isEqualTo(recommendation);
    }

    @Test
    void findByMemberId() {
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        Member member3 = new Member();
        member1.setId("1");
        member2.setId("2");
        member3.setId("3");
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        Recommendation recommendation = new Recommendation();
        recommendation.setScore(0.88);
        recommendation.setMember1(member1);
        recommendation.setMember2(member2);
        Recommendation recommendation2 = new Recommendation();
        recommendation2.setMember1(member3);
        recommendation2.setMember2(member1);
        recommendationRepository.save(recommendation);
        recommendationRepository.save(recommendation2);
        //when
        List<Recommendation> find = recommendationRepository.findByMemberId("1");

        //then
        assertThat(find.size()).isEqualTo(2);
        assertThat(find).contains(recommendation, recommendation2);
    }

    @Test
    void update() {
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        member1.setId("1");
        member2.setId("2");
        memberRepository.save(member1);
        memberRepository.save(member2);
        Recommendation recommendation = new Recommendation();
        recommendation.setScore(0.88);
        recommendation.setMember1(member1);
        recommendation.setMember2(member2);
        recommendationRepository.save(recommendation);

        //when
        RecommendationUpdateDto dto = new RecommendationUpdateDto();
        dto.setMember2Id(member1.getId());
        dto.setMember1Id(member2.getId());
        dto.setScore(0.99);
        recommendationRepository.update(dto);

        //then
        List<Recommendation> find = recommendationRepository.findByMemberId("1");
        assertThat(find.get(0).getScore()).isEqualTo(0.99);

    }

    @Test
    void delete() {
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        Member member3 = new Member();
        member1.setId("1");
        member2.setId("2");
        member3.setId("3");
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        Recommendation recommendation = new Recommendation();
        recommendation.setScore(0.88);
        recommendation.setMember1(member1);
        recommendation.setMember2(member2);
        Recommendation recommendation2 = new Recommendation();
        recommendation2.setMember1(member3);
        recommendation2.setMember2(member1);
        recommendation2.setScore(0.99);
        recommendationRepository.save(recommendation);
        recommendationRepository.save(recommendation2);

        //when
        recommendationRepository.delete("1");

        //then
        List<Recommendation> find = recommendationRepository.findByMemberId("1");
        assertThat(find.size()).isEqualTo(0);

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
        lifeStyle.setDrinking(drinking);
        lifeStyle.setAge(age);

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
        lifeStyle.setDrinking(5);
        lifeStyle.setAge(4);
        return lifeStyle;
    }
}