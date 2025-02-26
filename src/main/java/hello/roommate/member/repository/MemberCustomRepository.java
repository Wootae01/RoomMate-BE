package hello.roommate.member.repository;

import java.util.List;

import hello.roommate.member.domain.Member;
import hello.roommate.member.dto.FilterCond;

public interface MemberCustomRepository {
	List<Member> search(Long memberId, FilterCond cond);
}
