package hello.roommate.recommendation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hello.roommate.recommendation.domain.Preference;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {

	@Query("select p from Preference p where p.member.id =:memberId")
	List<Preference> findByMemberId(@Param("memberId") Long memberId);

	@Modifying
	@Query("delete from Preference p where p.member.id = :memberId")
	void deleteByMemberId(@Param("memberId") Long memberId);
}
