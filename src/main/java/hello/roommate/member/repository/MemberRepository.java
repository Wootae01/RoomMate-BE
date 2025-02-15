package hello.roommate.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	List<Member> findByDorm(Dormitory dormitory);

	/*
	 * 1. 나와 동일한 기숙사 정보를 가진 사람을 찾는다.
	 * 2. 내가 선호하는 사람의 특성과 그 상대방의 Lifestyle을 비교한다.(원하는 사람이 없으면 모든 사람 다 가능)
	 * 3. 그 상대방이 선호하는 사람의 특성과 나의 LifeStyle을 비교한다.(그 상대방이 원하는 사람 없으면 그 사람 가능)
	 * 4. 나의 id는 제외한다.
	 * */
	@Query(value = """
		    select m
		    from Member m
		    left join  LifeStyle ls on m.id = ls.member.id
		    left join  Preference p on p.member.id = m.id
		    where m.dorm = (
		        select m2.dorm from Member m2 where m2.id = :myId
		    )
		    and (
		        ls.option.id in (
		            select p2.option.id from Preference p2 where p2.member.id = :myId
		        )
		        or not exists (
		            select p3.option.id from Preference p3 where p3.member.id = :myId
		        )
		    )
		    and (
		        p.option.id in (
		            select ls2.option.id from LifeStyle ls2 where ls2.member.id = :myId
		        )
		        or p.option.id is null
		    )
		    and m.id <> :myId
		""")
	List<Member> recommendMembers(@Param("myId") Long myId);

	@Query(value = """
		    select m
		    from Member m
		    left join  LifeStyle ls on m.id = ls.member.id
		    left join  Preference p on p.member.id = m.id
		    where m.dorm = (
		        select m2.dorm from Member m2 where m2.id = :myId
		    )
		    and (
		        ls.option.id in :cond
		    )
		    and (
		        p.option.id in (
		            select ls2.option.id from LifeStyle ls2 where ls2.member.id = :myId
		        )
		        or p.option.id is null
		    )
		    and m.id <> :myId
		""")
	List<Member> searchMembers(@Param("myId") Long myId, @Param("cond") List<Long> cond);

}
