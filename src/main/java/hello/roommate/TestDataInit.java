package hello.roommate;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
import hello.roommate.profile.domain.Profile;
import hello.roommate.profile.repository.ProfileRepository;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.repository.LifeStyleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
public class TestDataInit {
    private final LifeStyleRepository lifeStyleRepository;
    private final ProfileRepository profileRepository;
    private final MemberRepository memberRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
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
}
