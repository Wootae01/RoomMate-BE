package hello.roommate.member.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

import hello.roommate.member.domain.Member;
import hello.roommate.member.domain.QMember;
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
	public List<Member> search(Long memberId, Map<Category, List<Long>> cond, List<Integer> ages) {

		QMember member = QMember.member;
		QLifeStyle lifeStyle = QLifeStyle.lifeStyle;

		//내 id 제외, 나와 같은 기숙사, 성별 사람 찾기
		JPQLQuery<Member> query = from(member);
		query.where(member.id.ne(memberId))
			.where(member.dorm.eq(
				JPAExpressions.select(member.dorm)
					.from(member)
					.where(member.id.eq(memberId))
			))
			.where(member.gender.eq(
				JPAExpressions.select(member.gender)
					.from(member)
					.where(member.id.eq(memberId))
			));

		//나이 비교
		if (!ages.isEmpty()) {
			query.where(member.age.in(ages));
		}

		cond.forEach((category, optionIds) -> {
			if (!optionIds.isEmpty()) {
				query.where(
					JPAExpressions
						.selectOne()
						.from(lifeStyle)
						.where(lifeStyle.member.eq(member))
						.where(lifeStyle.option.id.in(optionIds))
						.exists()
				);
			}
		});

		return query.fetch();

	}
}
