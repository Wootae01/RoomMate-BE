package hello.roommate.init;

import java.util.Arrays;
import java.util.List;

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

	public void createMember() {
		if (memberRepository.count() == 0) {
			List<Member> list = Arrays.asList(
				new Member(1L, "A", "hi", "img1", 21, Dormitory.INUI, Gender.MALE),
				new Member(2L, "B", "hi2", "img2", 23, Dormitory.INUI, Gender.MALE),
				new Member(3L, "C", "hi3", "img3", 24, Dormitory.INUI, Gender.MALE),
				new Member(4L, "D", "hi4", "img4", 25, Dormitory.INUI, Gender.MALE),
				new Member(5L, "E", "hi5", "img5", 21, Dormitory.YEJI, Gender.FEMALE)
			);

			memberRepository.saveAll(list);
		}
	}
	
}
