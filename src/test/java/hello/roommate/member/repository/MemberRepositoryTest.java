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
import hello.roommate.recommendation.repository.LifeStyleRepository;

@DataJpaTest
@Transactional
class MemberRepositoryTest {
	@Autowired
	private MemberRepository memberRepository;
	@Test
	void save() {
        Member member = createMember(12345L, Dormitory.INUI, "abc");

		//when
		Member save = memberRepository.save(member);
		Member find = memberRepository.findById(member.getId()).orElseThrow();

		//then
		assertThat(save).isEqualTo(find);
	}

	@Test
	void findById() {
		//given
        Member member = createMember(12345L, Dormitory.INUI, "abc");
        Member save = memberRepository.save(member);

        //when
		Member find = memberRepository.findById(member.getId()).orElseThrow();
		assertThat(find).isEqualTo(save);
	}

	@Test
	void findByDorm() {
		Member member1 = createMember(12345L, Dormitory.INUI, "abc");
		Member member2 = createMember(12345L, Dormitory.INUI, "abcd");
		Member member3 = createMember(12345L, Dormitory.YEJI, "abce");
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
        Member member = createMember(12345L, Dormitory.INUI, "abc");
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

    }

	private Member createMember(Long id, Dormitory dorm, String nickname) {
		Member member = new Member();
		member.setId(id);
		member.setDorm(dorm);
		member.setNickname(nickname);
		return member;
	}
}
