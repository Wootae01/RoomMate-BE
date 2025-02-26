package hello.roommate.member.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

import hello.roommate.member.domain.Member;
import hello.roommate.member.domain.QMember;
import hello.roommate.member.dto.FilterCond;
import hello.roommate.recommendation.domain.QLifeStyle;
import hello.roommate.recommendation.domain.enums.Category;

public class MemberRepositoryImpl extends QuerydslRepositorySupport implements MemberCustomRepository {

	public MemberRepositoryImpl() {
		super(Member.class);
	}

	/**
	 * 검색조건에 맞는 사용자를 찾아 반환한다.
	 * 검색 조건에서 각 카테고리별로 체크한 항목들 중 1개 이상이 LifeStyle 과 일치한 멤버 반환
	 * @param memberId 사용자 id
	 * @param cond 검색 조건
	 * @return
	 */
	@Override
	public List<Member> search(Long memberId, FilterCond cond) {
		Map<Category, List<Long>> map = cond.getCond();

		QMember member = QMember.member;
		QLifeStyle lifeStyle = QLifeStyle.lifeStyle;

		JPQLQuery<Member> query = from(member);
		query.where(member.id.ne(memberId));

		map.forEach((category, optionIds) -> {
			query.where(
				JPAExpressions
					.selectOne()
					.from(lifeStyle)
					.where(lifeStyle.member.eq(member))
					.where(lifeStyle.option.id.in(optionIds))
					.exists()
			);
		});

		return query.fetch();

	}
}
