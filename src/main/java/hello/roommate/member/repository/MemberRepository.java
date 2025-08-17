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

	@Query("SELECT m FROM Member m WHERE m.id IN :memberIds "
		+ "AND m.age IN :ages "
		+ "AND m.gender =:gender "
		+ "AND m.dorm =:dorm "
		+ "And m.id <> :myId")
	List<Member> findEligibleMember(Long myId, List<Long> memberIds, Dormitory dorm, Gender gender, List<Integer> ages);

	@Query("SELECT m FROM Member m WHERE m.id IN :memberIds "
		+ "AND m.gender =:gender "
		+ "AND m.dorm =:dorm "
		+ "And m.id <> :myId")
	List<Member> findEligibleMemberExceptAge(Long myId, List<Long> memberIds, Dormitory dorm, Gender gender);

	@Query("SELECT m.id FROM Member m")
	List<Long> findAllIds();
}
