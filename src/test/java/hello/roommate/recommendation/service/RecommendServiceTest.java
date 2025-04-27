package hello.roommate.recommendation.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import hello.roommate.init.OptionInit;
import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Gender;
import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
import hello.roommate.member.service.MemberService;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Option;
import hello.roommate.recommendation.domain.enums.Category;
import hello.roommate.recommendation.repository.LifeStyleRepository;
import hello.roommate.recommendation.repository.OptionRepository;

@DataJpaTest
@Import(value = {
	OptionInit.class,
	MemberService.class,
	RecommendService.class
})
class RecommendServiceTest {

	@Autowired
	private OptionRepository optionRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private LifeStyleRepository lifeStyleRepository;

	@Autowired
	private RecommendService recommendService;

	@Test
	void getSimilarityMap_perfectMatch_expectedSimilarity() {

		//옵션 초기화
		new OptionInit(optionRepository).createOption();

		//요청한 사람 lifeStyle
		Map<String, List<Long>> requestLifeStyleMap = new HashMap<>();
		requestLifeStyleMap.put(Category.BED_TIME.toString(), List.of(101L, 102L, 103L));
		requestLifeStyleMap.put(Category.WAKEUP_TIME.toString(), List.of(201L, 202L, 203L));
		requestLifeStyleMap.put(Category.EATING.toString(), List.of(901L));

		//상대방1
		Member member1 = new Member();
		member1.setDorm(Dormitory.INUI);
		member1.setAge(1999);
		member1.setGender(Gender.MALE);
		Member save1 = memberRepository.save(member1);

		List<LifeStyle> lifeStyles = new ArrayList<>();
		requestLifeStyleMap.forEach((key, value) -> {
			value.forEach(id -> {
				Option option = optionRepository.findById(id).orElseThrow();
				lifeStyles.add(new LifeStyle(save1, option));
			});
		});
		lifeStyleRepository.saveAll(lifeStyles);
		save1.setLifeStyle(lifeStyles);

		//when
		Map<Long, Double> map = recommendService.getSimilarityMap(requestLifeStyleMap, List.of(save1));
		Double result = map.get(save1.getId());
		//then
		Assertions.assertThat(result).isEqualTo(1.0, Offset.offset(1e-9));
	}

	@Test
	void getSimilarityMap_partialMatch_expectedSimilarity() {
		//옵션 초기화
		new OptionInit(optionRepository).createOption();

		//요청한 사람 lifeStyle
		Map<String, List<Long>> requestLifeStyleMap = new HashMap<>();
		requestLifeStyleMap.put(Category.BED_TIME.toString(), List.of(101L, 102L, 103L));
		requestLifeStyleMap.put(Category.WAKEUP_TIME.toString(), List.of(201L, 202L, 203L));
		requestLifeStyleMap.put(Category.EATING.toString(), List.of(901L));

		//상대방1
		Member member1 = new Member();
		member1.setDorm(Dormitory.INUI);
		member1.setAge(1999);
		member1.setGender(Gender.MALE);
		Member save1 = memberRepository.save(member1);

		List<LifeStyle> lifeStyles = List.of(
			new LifeStyle(member1, optionRepository.findById(102L).orElseThrow()),
			new LifeStyle(save1, optionRepository.findById(103L).orElseThrow()),
			new LifeStyle(save1, optionRepository.findById(104L).orElseThrow()),
			new LifeStyle(save1, optionRepository.findById(105L).orElseThrow()),
			new LifeStyle(save1, optionRepository.findById(201L).orElseThrow()),
			new LifeStyle(save1, optionRepository.findById(902L).orElseThrow())
		);

		lifeStyleRepository.saveAll(lifeStyles);
		save1.setLifeStyle(lifeStyles);

		Map<Long, Double> map = recommendService.getSimilarityMap(requestLifeStyleMap, List.of(save1));
		Double sim = map.get(save1.getId());

		Assertions.assertThat(sim).isEqualTo(0.384900179, Offset.offset(1e-9));
	}

	@Test
	void getTop30Value() {
		List<Double> list = new ArrayList<>(List.of(0.1, 0.2, 0.3, 0.4, 0.5, 0.6));
		double value = recommendService.getTop30Value(list);
		Assertions.assertThat(value).isEqualTo(0.5);
	}
}