package hello.roommate.recommendation.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
import hello.roommate.recommendation.domain.Recommendation;
import lombok.extern.slf4j.Slf4j;

@DataJpaTest
@Transactional
@Slf4j
class RecommendationRepositoryTest {
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private RecommendationRepository recommendationRepository;

	@Test
	void save() {
		//given
		Recommendation recommendation = new Recommendation();
		recommendation.setScore(0.986);
		//when
		Recommendation save = recommendationRepository.save(recommendation);

		//then
		Assertions.assertThat(save).isEqualTo(recommendation);
	}

	@Test
	void findByMemberId() {
		//given
		Member member1 = new Member();
		Member member2 = new Member();
		Member member3 = new Member();
		member1.setId("1");
		member2.setId("2");
		member3.setId("3");
		Member save1 = memberRepository.save(member1);
		Member save2 = memberRepository.save(member2);
		Member save3 = memberRepository.save(member3);
		Recommendation recommendation = new Recommendation();
		recommendation.setScore(0.88);
		recommendation.setMember1(save1);
		recommendation.setMember2(save2);
		Recommendation recommendation2 = new Recommendation();
		recommendation2.setMember1(save3);
		recommendation2.setMember2(save1);
		recommendationRepository.save(recommendation);
		recommendationRepository.save(recommendation2);
		//when
		List<Recommendation> find = recommendationRepository.findByMemberId("1");

		//then
		assertThat(find.size()).isEqualTo(2);
		assertThat(find).contains(recommendation, recommendation2);
	}

	@Test
	void delete() {
		//given
		Member member1 = new Member();
		Member member2 = new Member();
		Member member3 = new Member();
		member1.setId("1");
		member2.setId("2");
		member3.setId("3");
		Member save1 = memberRepository.save(member1);
		Member save2 = memberRepository.save(member2);
		Member save3 = memberRepository.save(member3);
		Recommendation recommendation = new Recommendation();
		recommendation.setScore(0.88);
		recommendation.setMember1(save1);
		recommendation.setMember2(save2);
		Recommendation recommendation2 = new Recommendation();
		recommendation2.setMember1(save3);
		recommendation2.setMember2(save1);
		recommendation2.setScore(0.99);
		recommendationRepository.save(recommendation);
		recommendationRepository.save(recommendation2);

		//when
		recommendationRepository.delete("1");

		//then
		List<Recommendation> find = recommendationRepository.findByMemberId("1");
		assertThat(find.size()).isEqualTo(0);

	}

}