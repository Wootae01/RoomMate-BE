package hello.roommate.member.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.init.OptionInit;
import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Member;
import hello.roommate.member.dto.FilterCond;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Option;
import hello.roommate.recommendation.domain.enums.Category;
import hello.roommate.recommendation.repository.LifeStyleRepository;
import hello.roommate.recommendation.repository.OptionRepository;
import hello.roommate.recommendation.repository.PreferenceRepository;
import lombok.extern.slf4j.Slf4j;

@DataJpaTest
@Transactional
@Slf4j
class MemberRepositoryTest {
	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private OptionRepository optionRepository;

	@Autowired
	private PreferenceRepository preferenceRepository;

	@Autowired
	private LifeStyleRepository lifeStyleRepository;

	@Test
	void save() {
		Member member = createMember(1L, Dormitory.INUI, "abc");

		//when
		Member save = memberRepository.save(member);
		Member find = memberRepository.findById(member.getId()).orElseThrow();

		//then
		assertThat(save).isEqualTo(find);
	}

	@Test
	void findById() {
		//given
		Member member = createMember(1L, Dormitory.INUI, "abc");
		Member save = memberRepository.save(member);

		//when
		Member find = memberRepository.findById(member.getId()).orElseThrow();
		assertThat(find).isEqualTo(save);
	}

	@Test
	void findByDorm() {
		Member member1 = createMember(1L, Dormitory.INUI, "abc");
		Member member2 = createMember(2L, Dormitory.INUI, "abcd");
		Member member3 = createMember(3L, Dormitory.YEJI, "abce");
		Member save1 = memberRepository.save(member1);
		Member save2 = memberRepository.save(member2);
		Member save3 = memberRepository.save(member3);

		//when
		List<Member> inui = memberRepository.findByDorm(Dormitory.INUI);
		List<Member> yeji = memberRepository.findByDorm(Dormitory.YEJI);

		//then
		assertThat(inui.size()).isEqualTo(2);
		assertThat(yeji.size()).isEqualTo(1);
		assertThat(inui).contains(save1, save2);
		assertThat(yeji).contains(save3);
	}

	@Test
	void delete() {
		//given
		Member member = createMember(1L, Dormitory.INUI, "abc");
		memberRepository.save(member);

		//when
		memberRepository.deleteById(member.getId());
		Optional<Member> find = memberRepository.findById(member.getId());

		//then
		assertThatThrownBy(() -> find.orElseThrow())
			.isInstanceOf(NoSuchElementException.class);
	}

	@Test
	void search() {

		OptionInit optionInit = new OptionInit(optionRepository);
		optionInit.createOption();

		//given

		//내 정보
		Member member = new Member();
		member.setId(1111L);
		Member save = memberRepository.save(member);

		//사용할 option
		List<Option> options = new ArrayList<>();
		options.add(optionRepository.findById(101L).get());
		options.add(optionRepository.findById(102L).get());
		options.add(optionRepository.findById(103L).get());
		options.add(optionRepository.findById(201L).get());
		options.add(optionRepository.findById(301L).get());
		options.add(optionRepository.findById(302L).get());

		//상대방1
		Member member1 = new Member();
		member1.setId(2222L);
		Member save1 = memberRepository.save(member1);

		List<LifeStyle> lifeStyles = new ArrayList<>();
		lifeStyles.add(new LifeStyle(save1, options.get(0)));
		lifeStyles.add(new LifeStyle(save1, options.get(3)));
		lifeStyles.add(new LifeStyle(save1, options.get(4)));
		lifeStyleRepository.saveAll(lifeStyles);

		//상대방3
		Member member3 = new Member();
		member3.setId(3333L);
		Member save3 = memberRepository.save(member3);

		List<LifeStyle> lifeStyles3 = new ArrayList<>();
		lifeStyles3.add(new LifeStyle(save3, options.get(0)));
		lifeStyles3.add(new LifeStyle(save3, options.get(4)));
		lifeStyleRepository.saveAll(lifeStyles3);

		//상대방4
		Member member4 = new Member();
		member4.setId(4444L);
		Member save4 = memberRepository.save(member4);

		List<LifeStyle> lifeStyles4 = new ArrayList<>();
		lifeStyles4.add(new LifeStyle(save4, options.get(1)));
		lifeStyles4.add(new LifeStyle(save4, options.get(3)));
		lifeStyles4.add(new LifeStyle(save4, options.get(4)));
		lifeStyles4.add(new LifeStyle(save4, options.get(5)));
		lifeStyleRepository.saveAll(lifeStyles4);

		//when
		Map<Category, List<Long>> listMap = new HashMap<>();
		listMap.put(Category.BED_TIME, new ArrayList<>(List.of(101L, 102L, 103L)));
		listMap.put(Category.WAKEUP_TIME, new ArrayList<>(List.of(201L)));
		listMap.put(Category.HEATING, new ArrayList<>(List.of(301L)));

		FilterCond filterCond = new FilterCond();
		filterCond.setCond(listMap);

		log.info("검색 시작");
		List<Member> search = memberRepository.search(member.getId(), filterCond);
		log.info("검색 종료");
		//then
		Assertions.assertThat(search).contains(save1);
		Assertions.assertThat(search).contains(save4);

		Assertions.assertThat(search.size()).isEqualTo(2);

	}

	private Member createMember(Long id, Dormitory dorm, String nickname) {
		Member member = new Member();
		member.setId(id);
		member.setDorm(dorm);
		member.setNickname(nickname);
		return member;
	}
}
