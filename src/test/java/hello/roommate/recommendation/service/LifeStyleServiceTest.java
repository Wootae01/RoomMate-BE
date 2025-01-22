package hello.roommate.recommendation.service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.init.OptionInit;
import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Member;
import hello.roommate.member.service.MemberService;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Option;
import hello.roommate.recommendation.domain.enums.BedTime;
import hello.roommate.recommendation.domain.enums.Category;
import hello.roommate.recommendation.dto.LifeStyleDto;
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

		Member member = new Member("1234", "abc", "in", "img34", 21, Dormitory.INUI);
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
		LifeStyleDto dto = new LifeStyleDto(
			"AT_22, AT_23",
			"AT_06, AT_07",
			"FROM_24_TO_26, BELOW_20",
			"FROM_21_TO_23, ABOVE_27",
			"INDIVIDUAL",
			"EARPHONES",
			"NORMAL",
			"OCCASIONAL",
			"NORMAL",
			"FOOD",
			"SIMPLE",
			"YES",
			"NON_SMOKER"
		);

		lifeStyleService.update("1234", dto);
		List<LifeStyle> lifeStyles = lifeStyleService.findByMemberId("1234");
		Map<Category, List<LifeStyle>> collect = lifeStyles.stream()
			.collect(Collectors.groupingBy(lifeStyle -> lifeStyle.getOption().getCategory()));

		// Assert that BED_TIME category contains expected options
		Assertions.assertThat(collect.get(Category.BED_TIME))
			.extracting(lifeStyle -> lifeStyle.getOption().getOptionValue())
			.containsExactlyInAnyOrder("AT_22", "AT_23");

		// Assert that WAKEUP_TIME category contains expected options
		Assertions.assertThat(collect.get(Category.WAKEUP_TIME))
			.extracting(lifeStyle -> lifeStyle.getOption().getOptionValue())
			.containsExactlyInAnyOrder("AT_06", "AT_07");

		// Assert that COOLING category contains expected options
		Assertions.assertThat(collect.get(Category.COOLING))
			.extracting(lifeStyle -> lifeStyle.getOption().getOptionValue())
			.containsExactlyInAnyOrder("FROM_24_TO_26", "BELOW_20");

		// Assert that HEATING category contains expected options
		Assertions.assertThat(collect.get(Category.HEATING))
			.extracting(lifeStyle -> lifeStyle.getOption().getOptionValue())
			.containsExactlyInAnyOrder("FROM_21_TO_23", "ABOVE_27");

		// Assert that CLEANING category matches the expected single option
		Assertions.assertThat(collect.get(Category.CLEANING))
			.extracting(lifeStyle -> lifeStyle.getOption().getOptionValue())
			.containsExactly("INDIVIDUAL");

		// Assert other single-option categories
		Assertions.assertThat(collect.get(Category.NOISE))
			.extracting(lifeStyle -> lifeStyle.getOption().getOptionValue())
			.containsExactly("EARPHONES");

		Assertions.assertThat(collect.get(Category.SCENT))
			.extracting(lifeStyle -> lifeStyle.getOption().getOptionValue())
			.containsExactly("NORMAL");

		Assertions.assertThat(collect.get(Category.DRINKING))
			.extracting(lifeStyle -> lifeStyle.getOption().getOptionValue())
			.containsExactly("OCCASIONAL");

		Assertions.assertThat(collect.get(Category.RELATIONSHIP))
			.extracting(lifeStyle -> lifeStyle.getOption().getOptionValue())
			.containsExactly("NORMAL");

		Assertions.assertThat(collect.get(Category.EATING))
			.extracting(lifeStyle -> lifeStyle.getOption().getOptionValue())
			.containsExactly("FOOD");

		Assertions.assertThat(collect.get(Category.INDOOR_CALL))
			.extracting(lifeStyle -> lifeStyle.getOption().getOptionValue())
			.containsExactly("SIMPLE");

		Assertions.assertThat(collect.get(Category.SLEEP_HABIT))
			.extracting(lifeStyle -> lifeStyle.getOption().getOptionValue())
			.containsExactly("YES");

		Assertions.assertThat(collect.get(Category.SMOKING))
			.extracting(lifeStyle -> lifeStyle.getOption().getOptionValue())
			.containsExactly("NON_SMOKER");

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
		Member member = new Member("1234", "abc", "in", "img34", 21, Dormitory.INUI);
		memberService.save(member);
		Option option = optionService.findByCategoryAndValue(Category.BED_TIME, BedTime.AT_02.name());
		return new LifeStyle(member, option);
	}

}