package hello.roommate.member.repository;

import java.util.List;
import java.util.Map;

import hello.roommate.member.domain.Member;
import hello.roommate.recommendation.domain.enums.Category;

public interface MemberCustomRepository {
	List<Member> search(Long memberId, Map<Category, List<Long>> cond, List<Integer> ages);
}
