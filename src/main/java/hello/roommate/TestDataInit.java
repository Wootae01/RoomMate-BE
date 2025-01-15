package hello.roommate;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Member;
import hello.roommate.member.service.MemberService;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.enums.BedTime;
import hello.roommate.recommendation.domain.enums.Cleaning;
import hello.roommate.recommendation.domain.enums.Cooling;
import hello.roommate.recommendation.domain.enums.Drinking;
import hello.roommate.recommendation.domain.enums.Eating;
import hello.roommate.recommendation.domain.enums.Heating;
import hello.roommate.recommendation.domain.enums.Noise;
import hello.roommate.recommendation.domain.enums.Relationship;
import hello.roommate.recommendation.domain.enums.Scent;
import hello.roommate.recommendation.domain.enums.SleepHabit;
import hello.roommate.recommendation.domain.enums.Smoking;
import hello.roommate.recommendation.domain.enums.WakeUpTime;
import hello.roommate.recommendation.service.LifestyleService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestDataInit {
	private final LifestyleService lifestyleService;
	private final MemberService memberService;

	@EventListener(ApplicationReadyEvent.class)
	public void initData() {
		LifeStyle lifeStyleA = createLifeStyle(BedTime.AT_22, WakeUpTime.AT_08,
			SleepHabit.YES, Cleaning.TOGETHER, Cooling.FROM_21_TO_23,
			Heating.FROM_21_TO_23, Noise.SPEAKERS, Smoking.NON_SMOKER,
			Scent.NORMAL, Eating.SNACK, Relationship.NORMAL, Drinking.FREQUENT, 20);
		LifeStyle lifeStyleB = createLifeStyle(BedTime.AT_22, WakeUpTime.AT_07,
			SleepHabit.YES, Cleaning.TOGETHER, Cooling.FROM_21_TO_23,
			Heating.FROM_21_TO_23, Noise.FLEXIBLE, Smoking.NON_SMOKER,
			Scent.NORMAL, Eating.SNACK, Relationship.NORMAL, Drinking.FREQUENT, 27);
		LifeStyle lifeStyleC = createLifeStyle(BedTime.AT_22, WakeUpTime.AT_08,
			SleepHabit.YES, Cleaning.TOGETHER, Cooling.ABOVE_27,
			Heating.FROM_21_TO_23, Noise.SPEAKERS, Smoking.NON_SMOKER,
			Scent.SENSITIVE, Eating.SNACK, Relationship.NORMAL, Drinking.FREQUENT, 23);
		LifeStyle lifeStyleD = createLifeStyle(BedTime.AT_23, WakeUpTime.AT_08,
			SleepHabit.YES, Cleaning.INDIVIDUAL, Cooling.FROM_24_TO_26,
			Heating.FROM_21_TO_23, Noise.EARPHONES, Smoking.SMOKER,
			Scent.NOT_SENSITIVE, Eating.SNACK, Relationship.INVISIBLE, Drinking.FREQUENT, 22);
		lifestyleService.save(lifeStyleA);
		lifestyleService.save(lifeStyleB);
		lifestyleService.save(lifeStyleC);
		lifestyleService.save(lifeStyleD);

		Member memberA = createMember("A", Dormitory.INUI, lifeStyleA, "A", "img1", "hello");
		Member memberB = createMember("B", Dormitory.INUI, lifeStyleB, "B", "img2", "hello");
		Member memberC = createMember("C", Dormitory.INUI, lifeStyleC, "C", "img3", "hello");
		Member memberD = createMember("D", Dormitory.INUI, lifeStyleD, "D", "img4", "hello");
		memberService.save(memberA);
		memberService.save(memberB);
		memberService.save(memberC);
		memberService.save(memberD);
	}

	private Member createMember(String id, Dormitory dorm, LifeStyle lifeStyle, String nickname, String img,
		String intro) {
		Member member = new Member();
		member.setId(id);
		member.setDorm(dorm);
		member.setLifeStyle(lifeStyle);
		member.setNickname(nickname);
		member.setImg(img);
		member.setIntroduce(intro);
		return member;
	}

	private LifeStyle createLifeStyle(BedTime bedTime, WakeUpTime wakeupTime, SleepHabit sleepHabit, Cleaning cleaning,
		Cooling cooling, Heating heating, Noise noise, Smoking smoking,
		Scent scent, Eating eating, Relationship relationship,
		Drinking drinking, int age) {

		LifeStyle lifeStyle = new LifeStyle();
		lifeStyle.setBedTime(bedTime);
		lifeStyle.setWakeupTime(wakeupTime);
		lifeStyle.setSleepHabit(sleepHabit);
		lifeStyle.setCleaning(cleaning);
		lifeStyle.setCooling(cooling);
		lifeStyle.setHeater(heating);
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
