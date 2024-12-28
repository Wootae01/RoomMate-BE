package hello.roommate.recommendation.service;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.dto.RecommendationDto;
import hello.roommate.recommendation.repository.LifeStyleRepository;
import hello.roommate.recommendation.repository.RecommendationRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class RecommendationServiceTest {

    @Autowired private RecommendationService service;
    @Autowired private RecommendationRepository recommendationRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private LifeStyleRepository lifeStyleRepository;
    @Test
    void findLiveRecommendations() {
        //given
        LifeStyle lifeStyleA = createLifeStyle(10, 8, 7, 5, 6, 4, 3, 2, 1, 5, 3, 4, 20);
        LifeStyle lifeStyleB = createLifeStyle(5, 6, 5, 6, 7, 4, 3, 1, 2, 4, 6, 5, 21);
        LifeStyle lifeStyleC = createLifeStyle(8, 7, 6, 5, 4, 3, 5, 7, 6, 5, 4, 1, 19);
        LifeStyle lifeStyleD = createLifeStyle(4, 5, 4, 3, 2, 1, 6, 7, 8, 5, 3, 7, 22);
        lifeStyleRepository.save(lifeStyleA);
        lifeStyleRepository.save(lifeStyleB);
        lifeStyleRepository.save(lifeStyleC);
        lifeStyleRepository.save(lifeStyleD);

        Member memberA = createMember("A", Dormitory.INUI, lifeStyleA);
        Member memberB = createMember("B", Dormitory.INUI, lifeStyleB);
        Member memberC = createMember("C", Dormitory.INUI, lifeStyleC);
        Member memberD = createMember("D", Dormitory.INUI, lifeStyleD);
        memberRepository.save(memberA);
        memberRepository.save(memberB);
        memberRepository.save(memberC);
        memberRepository.save(memberD);

        //when
        List<RecommendationDto> recommendations = service.findLiveRecommendations("A");
        for (RecommendationDto recommendation : recommendations) {
            log.info("(id, score) = ({},{})", recommendation.getNickname(), recommendation.getScore());
        }

        //then
        assertThat(recommendations.size()).isEqualTo(3);

        //멤버정렬 확인
        assertThat(recommendations.get(0).getScore()).isGreaterThan(recommendations.get(1).getScore());
        assertThat(recommendations.get(1).getScore()).isGreaterThan(recommendations.get(2).getScore());


    }

    private Member createMember(String id, Dormitory dorm, LifeStyle lifeStyle) {
        Member member =  new Member();
        member.setId(id);
        member.setDorm(dorm);
        member.setLifeStyle(lifeStyle);
        return member;
    }

    private LifeStyle createLifeStyle(
            int bedTime, int wakeupTime, int sleepHabit, int cleaning, int aircon,
            int heater, int noise, int smoking, int scent, int eating,
            int relationship, int drinking, int age) {

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
}
