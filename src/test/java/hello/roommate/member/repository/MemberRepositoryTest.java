/*
package hello.roommate.member.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Member;
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
import hello.roommate.recommendation.repository.LifeStyleRepository;
import jakarta.persistence.EntityManager;

@DataJpaTest
@Transactional
class MemberRepositoryTest {
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private LifeStyleRepository lifeStyleRepository;
	@Autowired
	private EntityManager em;

	@Test
	void save() {
		//given
		LifeStyle lifeStyle = createLifeStyle();
		Member member = createMember(lifeStyle);

		//when
		Member save = memberRepository.save(member);
		Member find = memberRepository.findById(member.getId()).orElseThrow();

		//then
		assertThat(save).isEqualTo(find);
		assertThat(save.getLifeStyle()).isEqualTo(find.getLifeStyle());
	}

	@Test
	void findById() {
		//given
		LifeStyle lifeStyle = createLifeStyle();
		lifeStyleRepository.save(lifeStyle);
		Member member = createMember(lifeStyle);
		memberRepository.save(member);

		//when
		Member find = memberRepository.findById(member.getId()).orElseThrow();
		assertThat(find).isEqualTo(member);
	}

	@Test
	void findByDorm() {
		Member member1 = createMember("1245", Dormitory.INUI, "abc", null);
		Member member2 = createMember("1345", Dormitory.INUI, "abcd", null);
		Member member3 = createMember("1347", Dormitory.YEJI, "abce", null);
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
		Member member = createMember(null);
		memberRepository.save(member);

		//when
		memberRepository.deleteById(member.getId());
		Optional<Member> find = memberRepository.findById(member.getId());

		//then
		assertThatThrownBy(() -> find.orElseThrow())
			.isInstanceOf(NoSuchElementException.class);
	}

	private Member createMember(String id, Dormitory dorm, String nickname, LifeStyle lifeStyle) {
		Member member = new Member();
		member.setId(id);
		member.setDorm(dorm);
		member.setLifeStyle(lifeStyle);
		member.setNickname(nickname);
		return member;
	}

	private Member createMember(LifeStyle lifeStyle) {
		Member member = new Member();
		member.setId("1234");
		member.setDorm(Dormitory.INUI);
		member.setLifeStyle(lifeStyle);
		member.setNickname("nickname");
		return member;
	}

	private LifeStyle createLifeStyle() {
		LifeStyle lifeStyle = new LifeStyle();
		lifeStyle.setBedTime(BedTime.AT_02);
		lifeStyle.setWakeupTime(WakeUpTime.AT_09);
		lifeStyle.setSleepHabit(SleepHabit.YES);
		lifeStyle.setCleaning(Cleaning.TOGETHER);
		lifeStyle.setCooling(Cooling.FROM_24_TO_26);
		lifeStyle.setHeater(Heating.FROM_21_TO_23);
		lifeStyle.setNoise(Noise.SPEAKERS);
		lifeStyle.setSmoking(Smoking.NON_SMOKER);
		lifeStyle.setScent(Scent.SENSITIVE);
		lifeStyle.setEating(Eating.FOOD);
		lifeStyle.setRelationship(Relationship.CLOSE);
		lifeStyle.setDrinking(Drinking.OCCASIONAL);
		lifeStyle.setAge(22);
		return lifeStyle;
	}
}
*/
