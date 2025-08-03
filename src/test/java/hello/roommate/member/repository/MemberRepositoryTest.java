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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.init.OptionInit;
import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Gender;
import hello.roommate.member.domain.Member;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Option;
import hello.roommate.recommendation.domain.enums.Category;
import hello.roommate.recommendation.repository.LifeStyleRepository;
import hello.roommate.recommendation.repository.OptionRepository;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Transactional
@Slf4j
class MemberRepositoryTest {
	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private OptionRepository optionRepository;

	@Autowired
	private LifeStyleRepository lifeStyleRepository;

	@Test
	void save() {
		Member member = createMember(Dormitory.INUI, "abc");

		//when
		Member save = memberRepository.save(member);
		Member find = memberRepository.findById(member.getId()).orElseThrow();

		//then
		assertThat(save).isEqualTo(find);
	}

	@Test
	void findById() {
		//given
		Member member = createMember(Dormitory.INUI, "abc");
		Member save = memberRepository.save(member);

		//when
		Member find = memberRepository.findById(member.getId()).orElseThrow();
		assertThat(find).isEqualTo(save);
	}

	@Test
	void findByDorm() {
		Member member1 = createMember(Dormitory.INUI, "abc");
		Member member2 = createMember(Dormitory.INUI, "abcd");
		Member member3 = createMember(Dormitory.YEJI, "abce");
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
		Member member = createMember(Dormitory.INUI, "abc");
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
		member.setDorm(Dormitory.INUI);
		member.setAge(1999);
		member.setGender(Gender.MALE);
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
		member1.setDorm(Dormitory.INUI);
		member1.setAge(1999);
		member1.setGender(Gender.MALE);
		Member save1 = memberRepository.save(member1);

		List<LifeStyle> lifeStyles = new ArrayList<>();
		lifeStyles.add(new LifeStyle(save1, options.get(0))); //101
		lifeStyles.add(new LifeStyle(save1, options.get(3))); //201
		lifeStyles.add(new LifeStyle(save1, options.get(4))); //301
		lifeStyleRepository.saveAll(lifeStyles);

		//상대방2
		Member member2 = new Member();
		member2.setId(22222L);
		member2.setDorm(Dormitory.INUI);
		member2.setAge(2000);
		member2.setGender(Gender.MALE);
		Member save2 = memberRepository.save(member2);

		List<LifeStyle> lifeStyles2 = new ArrayList<>();
		lifeStyles2.add(new LifeStyle(save2, options.get(0))); //101
		lifeStyles2.add(new LifeStyle(save2, options.get(3))); //201
		lifeStyles2.add(new LifeStyle(save2, options.get(4))); //301
		lifeStyleRepository.saveAll(lifeStyles2);

		//상대방3
		Member member3 = new Member();
		member3.setId(3333L);
		member3.setDorm(Dormitory.INUI);
		member3.setAge(1999);
		member3.setGender(Gender.MALE);
		Member save3 = memberRepository.save(member3);

		List<LifeStyle> lifeStyles3 = new ArrayList<>();
		lifeStyles3.add(new LifeStyle(save3, options.get(0))); //101
		lifeStyles3.add(new LifeStyle(save3, options.get(4))); //301
		lifeStyleRepository.saveAll(lifeStyles3);

		//상대방4
		Member member4 = new Member();
		member4.setId(4444L);
		member4.setDorm(Dormitory.INUI);
		member4.setAge(1999);
		member4.setGender(Gender.MALE);
		Member save4 = memberRepository.save(member4);

		List<LifeStyle> lifeStyles4 = new ArrayList<>();
		lifeStyles4.add(new LifeStyle(save4, options.get(1))); //102
		lifeStyles4.add(new LifeStyle(save4, options.get(3))); //201
		lifeStyles4.add(new LifeStyle(save4, options.get(4))); //301
		lifeStyles4.add(new LifeStyle(save4, options.get(5))); //302
		lifeStyleRepository.saveAll(lifeStyles4);

		//상대방5
		Member member5 = new Member();
		member5.setId(5555L);
		member5.setDorm(Dormitory.INUI);
		member5.setAge(1999);
		member5.setGender(Gender.FEMALE);
		Member save5 = memberRepository.save(member5);

		List<LifeStyle> lifeStyles5 = new ArrayList<>();
		lifeStyles5.add(new LifeStyle(save5, options.get(1))); //102
		lifeStyles5.add(new LifeStyle(save5, options.get(3))); //201
		lifeStyles5.add(new LifeStyle(save5, options.get(4))); //301
		lifeStyles5.add(new LifeStyle(save5, options.get(5))); //302
		lifeStyleRepository.saveAll(lifeStyles5);

		//내 preference
		Map<Category, List<Long>> listMap = new HashMap<>();
		listMap.put(Category.BED_TIME, new ArrayList<>(List.of(101L, 102L, 103L)));
		listMap.put(Category.WAKEUP_TIME, new ArrayList<>(List.of(201L)));
		listMap.put(Category.HEATING, new ArrayList<>(List.of(301L)));
		List<Integer> ages = new ArrayList<>();
		ages.add(1999);
		ages.add(2000);
		//when

		List<Member> search = memberRepository.search(save.getId(), listMap, ages);

		Assertions.assertThat(search).contains(save1);
		Assertions.assertThat(search).contains(save4);
		Assertions.assertThat(search).contains(save2);

		Assertions.assertThat(search.size()).isEqualTo(3);

	}

	private Member createMember(Dormitory dorm, String nickname) {
		Member member = new Member();
		member.setDorm(dorm);
		member.setNickname(nickname);
		return member;
	}
}
