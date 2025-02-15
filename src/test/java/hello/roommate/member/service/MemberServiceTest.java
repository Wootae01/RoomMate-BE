package hello.roommate.member.service;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.init.OptionInit;
import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Gender;
import hello.roommate.member.domain.Member;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Option;
import hello.roommate.recommendation.domain.Preference;
import hello.roommate.recommendation.domain.enums.BedTime;
import hello.roommate.recommendation.domain.enums.Category;
import hello.roommate.recommendation.dto.OptionDto;
import hello.roommate.recommendation.service.LifestyleService;
import hello.roommate.recommendation.service.OptionService;
import hello.roommate.recommendation.service.PreferenceService;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Transactional
@Slf4j
class MemberServiceTest {
	@Autowired
	OptionInit optionInit;
	@Autowired
	MemberService memberService;
	@Autowired
	LifestyleService lifestyleService;
	@Autowired
	OptionService optionService;
	@Autowired
	PreferenceService preferenceService;

	@BeforeEach
	void init() {
		optionInit.createOption();
	}

	@Test
	void recommendMembers() {
		//given
		Member member1 = new Member(1L, "A", "hi", "img1", 21, Dormitory.INUI, Gender.MALE);
		Member member2 = new Member(2L, "B", "hi", "img2", 23, Dormitory.INUI, Gender.MALE);

		Option bedTime_22 = optionService.findByCategoryAndValue(Category.BED_TIME, BedTime.AT_22.name());
		Option bedTime_23 = optionService.findByCategoryAndValue(Category.BED_TIME, BedTime.AT_23.name());

		Member save1 = memberService.save(member1);
		Member save2 = memberService.save(member2);

		LifeStyle lifeStyle1 = new LifeStyle(save1, bedTime_22);
		LifeStyle lifeStyle2 = new LifeStyle(save2, bedTime_23);

		lifestyleService.save(lifeStyle1);
		lifestyleService.save(lifeStyle2);

		Preference preference = new Preference(save1, bedTime_23);
		preferenceService.save(preference);

		List<Member> recommended1 = memberService.recommendMembers(1L);
		Assertions.assertThat(recommended1).contains(save2);
	}

	@Test
	void searchMembers() {
		//given
		Member member1 = new Member(1L, "A", "hi", "img1", 21, Dormitory.INUI, Gender.MALE);
		Member member2 = new Member(2L, "B", "hi", "img2", 23, Dormitory.INUI, Gender.MALE);

		Option bedTime_22 = optionService.findByCategoryAndValue(Category.BED_TIME, BedTime.AT_22.name());
		Option bedTime_23 = optionService.findByCategoryAndValue(Category.BED_TIME, BedTime.AT_23.name());

		Member save1 = memberService.save(member1);
		Member save2 = memberService.save(member2);

		LifeStyle lifeStyle1 = new LifeStyle(save1, bedTime_22);
		LifeStyle lifeStyle2 = new LifeStyle(save2, bedTime_23);

		lifestyleService.save(lifeStyle1);
		lifestyleService.save(lifeStyle2);

		Preference preference = new Preference(save1, bedTime_23);
		preferenceService.save(preference);

		OptionDto optionDto = new OptionDto(Category.BED_TIME.name(), BedTime.AT_23.name());
		List<OptionDto> list = List.of(optionDto);
		List<Member> recommended1 = memberService.searchMembers(1L, list);

		Assertions.assertThat(recommended1).contains(save2);
	}
}