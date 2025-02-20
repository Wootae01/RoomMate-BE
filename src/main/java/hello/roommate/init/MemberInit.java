package hello.roommate.init;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Gender;
import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MemberInit {
	private final MemberRepository memberRepository;

	//랜덤한 member 100개 생성해서 저장.
	public void createMember() {
		if (memberRepository.count() == 0) {
			List<Member> members = new ArrayList<>();
			for (long i = 1; i <= 100; i++) {
				members.add(randomMember(i));
			}
			memberRepository.saveAll(members);
		}
	}

	//임의로 member 데이터 생성
	private Member randomMember(Long id) {
		Member member = new Member();
		member.setId(id);
		member.setNickname(String.valueOf(id));
		member.setImg("img");
		member.setDorm(Dormitory.INUI);
		member.setGender(Gender.MALE);
		member.setAge(new Random().nextInt(5) + 20);
		member.setIntroduce("hello " + id);
		return member;
	}
}
