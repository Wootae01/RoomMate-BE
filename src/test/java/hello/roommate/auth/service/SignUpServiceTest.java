package hello.roommate.auth.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import hello.roommate.advice.DuplicatedNicknameException;
import hello.roommate.auth.dto.SignUpDTO;
import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Gender;
import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class SignUpServiceTest {

	@Autowired
	private SignUpService signUpService;
	@Autowired
	private MemberRepository memberRepository;

	@Test
	@DisplayName("중복 닉네임 동시성 테스트")
	void concurrentRegisterMember() throws InterruptedException, ExecutionException {
		int numberOfThread = 5;
		ExecutorService executorService = Executors.newFixedThreadPool(numberOfThread);
		CountDownLatch latch = new CountDownLatch(numberOfThread);

		String nickname = "testNick";

		List<Future<Boolean>> futures = new ArrayList<>();
		for (int i = 0; i < numberOfThread; i++) {
			Member member = new Member();
			member.setUsername("username" + i);
			Member save = memberRepository.save(member);
			Long id = save.getId();
			futures.add(executorService.submit(() -> {
				try {
					SignUpDTO dto = createSignUpDTO(id, nickname); // idx를 이용해 이메일/username 고유화
					try {
						signUpService.registerMember(dto); // 동시 요청
						return true; // 성공
					} catch (DataIntegrityViolationException | DuplicatedNicknameException ex) {
						// 중복으로 실패한 경우(서비스가 예외 변환해주면 해당 예외를 사용)
						return false;
					}
				} finally {
					latch.countDown();
				}
			}));
		}

		latch.await(10, TimeUnit.SECONDS);
		executorService.shutdown();

		int successCount = 0;
		for (Future<Boolean> f : futures) {
			Boolean b = f.get();
			if (Boolean.TRUE.equals(b)) {
				successCount++;
			}
		}
		Assertions.assertThat(successCount).isEqualTo(1);
	}

	private SignUpDTO createSignUpDTO(Long id, String nickname) {
		SignUpDTO signUpDTO = new SignUpDTO();

		signUpDTO.setUserId(id);
		signUpDTO.setNickname(nickname);
		signUpDTO.setDormitory(Dormitory.INUI);
		signUpDTO.setAge(2001);
		signUpDTO.setGender(Gender.MALE);
		signUpDTO.setIntroduce("hello");
		signUpDTO.setLifeStyle(new HashMap<>());
		signUpDTO.setPreference(new HashMap<>());

		return signUpDTO;
	}

	private static Member createMember(String nickname) {
		Member member = new Member();

		return member;
	}
}