package hello.roommate.init;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Gender;
import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Component
public class MemberInit {
    private final MemberRepository memberRepository;

    //랜덤한 member 300개 생성해서 저장.
    public void createMember() {
        if (memberRepository.count() == 0) {
            List<Member> members = new ArrayList<>();
            for (long i = 1; i <= 2000; i++) {
                members.add(randomMember(i));
            }
            memberRepository.saveAll(members);
        }
    }

    //임의로 member 데이터 생성
    private Member randomMember(Long id) {
        Member member = new Member();
        member.setNickname("nick" + id);
        member.setDorm(Dormitory.INUI);
        member.setGender(Gender.MALE);
        member.setUsername("kakao@"+id);
        int year = LocalDate.now().getYear();
        year = year - (new Random().nextInt(17) + 17);
        member.setAge(year);
        member.setIntroduce("hello " + id);

        return member;
    }
}
