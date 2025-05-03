package hello.roommate.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Gender;
import hello.roommate.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

	List<Member> findByDorm(Dormitory dormitory);

	Optional<Member> findByNickname(String nickname);

	Optional<Member> findByUsername(String username);

	@Query("SELECT DISTINCT m FROM Member m JOIN FETCH m.lifeStyle")
	List<Member> findAllWithLifeStyle();

	@Query("SELECT DISTINCT m FROM Member m JOIN FETCH m.lifeStyle WHERE m.id =:id")
	Member findWithLifeStyleById(Long id);

	@Query("SELECT DISTINCT m FROM Member m JOIN FETCH m.preference WHERE m.id in :ids")
	List<Member> findAllWithPreferenceByIds(List<Long> ids);

	@Query("SELECT DISTINCT m FROM Member m JOIN FETCH m.lifeStyle WHERE m.dorm =:dorm and m.gender = :gender")
	List<Member> findAllWithLifeStyleByDormAndGender(Dormitory dorm, Gender gender);

}
