package hello.roommate.init;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import hello.roommate.member.domain.Dormitory;
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
				new Member("id1", "A", "hi", "img1", 21, Dormitory.INUI),
				new Member("id2", "B", "hi", "img2", 23, Dormitory.INUI),
				new Member("id3", "C", "hi", "img3", 24, Dormitory.INUI),
				new Member("id4", "D", "hi", "img4", 25, Dormitory.INUI),
				new Member("id5", "E", "hi", "img5", 21, Dormitory.YEJI)
			);

			memberRepository.saveAll(list);
		}
	}
	
}
