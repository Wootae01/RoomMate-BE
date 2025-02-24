package hello.roommate.recommendation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
import hello.roommate.member.service.MemberService;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Option;
import hello.roommate.recommendation.domain.enums.BedTime;
import hello.roommate.recommendation.domain.enums.Category;
import hello.roommate.recommendation.dto.LifeStyleDTO;
import lombok.extern.slf4j.Slf4j;

@Transactional
@SpringBootTest
@Slf4j
class LifeStyleServiceTest {
	@Autowired
	LifestyleService lifeStyleService;
	@Autowired
	OptionService optionService;

	@Autowired
	MemberService memberService;

	@Autowired
	OptionInit optionInit;

	@BeforeEach()
	void init() {
		optionInit.createOption();
	}

	@Test
	void save() {

		Member member = new Member(1L, "abc", "in", "img34", 21, Dormitory.INUI, Gender.MALE);
		memberService.save(member);
		Option option = optionService.findByCategoryAndValue(Category.BED_TIME, BedTime.AT_02.name());
		LifeStyle lifeStyle = new LifeStyle(member, option);

		LifeStyle save = lifeStyleService.save(lifeStyle);

		Assertions.assertThat(save).isEqualTo(lifeStyle);
	}

	@Test
	void findById() {
		//given
		LifeStyle lifeStyle = createLifeStyle();

		LifeStyle save = lifeStyleService.save(lifeStyle);
		LifeStyle find = lifeStyleService.findById(save.getId());
		//then
		Assertions.assertThat(find).isEqualTo(save);

	}

	@Test
	void update() {

		LifeStyle lifeStyle1 = createLifeStyle();
		lifeStyleService.save(lifeStyle1);

		List<Long> options = new ArrayList<>();
		options.add(1L);
		options.add(10L);
		options.add(15L);
		options.add(20L);

		LifeStyleDTO dto = new LifeStyleDTO();
		dto.setMemberId(1L);
		dto.setOptions(options);

		lifeStyleService.update(1L, dto);

		List<LifeStyle> lifeStyles = lifeStyleService.findByMemberId(1L);
		for (LifeStyle lifeStyle : lifeStyles) {
			Assertions.assertThat(lifeStyle.getOption().getId()).isIn(1L, 10L, 15L, 20L);
		}

	}

	@Test
	void delete() {
		//given
		LifeStyle lifeStyle = createLifeStyle();
		lifeStyleService.save(lifeStyle);

		//when
		lifeStyleService.delete(lifeStyle.getId());

		// then
		Assertions.assertThatThrownBy(() -> lifeStyleService.findById(lifeStyle.getId()))
			.isInstanceOf(NoSuchElementException.class);
	}

	private LifeStyle createLifeStyle() {
		Member member = new Member(1L, "abc", "in", "img34", 21, Dormitory.INUI, Gender.MALE);
		memberService.save(member);
		Option option = optionService.findByCategoryAndValue(Category.BED_TIME, BedTime.AT_02.name());
		return new LifeStyle(member, option);
	}

}