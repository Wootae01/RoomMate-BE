package hello.roommate.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	List<Member> findByDorm(Dormitory dormitory);

	Optional<Member> findByNickname(String nickname);

	/*
	 * 1. 나와 동일한 기숙사 정보를 가진 사람을 찾는다.
	 * 2. 내가 선호하는 사람의 특성과 그 상대방의 Lifestyle을 비교한다.(원하는 사람이 없으면 모든 사람 다 가능)
	 * 3. 나의 id는 제외한다.
	 * */
	@Query(value = """
		    select distinct m
		   	from Member m
		    where m.dorm = (
		    	select m2.dorm from Member m2 where m2.id = :myId
		   	)
		   	
		    and (
		    	not exists (
		    		select p from Preference p where p.member.id = :myId
		   		)
		    	or not exists (
		    		select p from Preference p
		        	where p.member.id = :myId
		        	and not exists (
		        		select ls from LifeStyle ls
		        		where ls.member.id = m.id
		        		and ls.option.id = p.option.id
		       		)
		    	)
			)
			
			and m.id <> :myId
		""")
	List<Member> recommendMembers(@Param("myId") Long myId);

	@Query(value = """
		    select distinct  m
		    from Member m
		    where m.dorm = (
		        select m2.dorm from Member m2 where m2.id = :myId
		    )
		    and (
		    		select count(distinct ls) from LifeStyle ls
		    		where ls.member.id = m.id
		    		and ls.option.id in :cond
		    ) = :condSize
		    
		    and m.id <> :myId
		""")
	List<Member> searchMembers(@Param("myId") Long myId, @Param("cond") List<Long> cond,
		@Param("condSize") int condSize);

}
