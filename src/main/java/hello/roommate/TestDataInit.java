package hello.roommate;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Member;
import hello.roommate.member.service.MemberService;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.service.LifestyleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.Date;

@RequiredArgsConstructor
public class TestDataInit {
    private final LifestyleService lifestyleService;
    private final MemberService memberService;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        LifeStyle lifeStyleA = createLifeStyle(10, 8, 7, 5, 6, 4, 3, 2, 1, 5, 3, 7, 4, 20, 6);
        LifeStyle lifeStyleB = createLifeStyle(5, 6, 5, 6, 7, 4, 3, 1, 2, 4, 6, 8, 5, 21, 7);
        LifeStyle lifeStyleC = createLifeStyle(8, 7, 6, 5, 4, 3, 5, 7, 6, 5, 4, 2, 1, 19, 5);
        LifeStyle lifeStyleD = createLifeStyle(4, 5, 4, 3, 2, 1, 6, 7, 8, 5, 3, 6, 7, 22, 8);
        lifestyleService.save(lifeStyleA);
        lifestyleService.save(lifeStyleB);
        lifestyleService.save(lifeStyleC);
        lifestyleService.save(lifeStyleD);


        Member memberA = createMember("A", Dormitory.INUI, lifeStyleA, "A", new Date(), "img1","hello");
        Member memberB = createMember("B", Dormitory.INUI, lifeStyleB, "B", new Date(), "img2", "hello");
        Member memberC = createMember("C", Dormitory.INUI,  lifeStyleC,"C", new Date(), "img3", "hello");
        Member memberD = createMember("D", Dormitory.INUI, lifeStyleD, "D", new Date(), "img4", "hello");
        memberService.save(memberA);
        memberService.save(memberB);
        memberService.save(memberC);
        memberService.save(memberD);
    }
    private Member createMember(String id, Dormitory dorm, LifeStyle lifeStyle, String nickname, Date timeStamp, String img, String intro) {
        Member member =  new Member();
        member.setId(id);
        member.setDorm(dorm);
        member.setLifeStyle(lifeStyle);
        member.setTimestamp(timeStamp);
        member.setNickname(nickname);
        member.setImg(img);
        member.setIntroduce(intro);
        return member;
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
}
