package hello.roommate.recommendation.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.member.repository.MemberRepository;
import hello.roommate.recommendation.repository.PreferenceRepository;

@Transactional
@SpringBootTest
public class PreferenceServiceTest {

	@Autowired
	private PreferenceService preferenceService;

	@Autowired
	private PreferenceRepository preferenceRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Test
	void update() {

	}
}
