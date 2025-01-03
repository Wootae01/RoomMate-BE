package hello.roommate.recommendation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hello.roommate.recommendation.domain.Recommendation;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

	@Query("select r from Recommendation r where r.member1.id =:memberId or r.member2.id =:memberId")
	List<Recommendation> findByMemberId(@Param("memberId") String memberId);

	@Query("select r from Recommendation r where " +
		"(r.member1.id = :member1Id and r.member2.id =:member2Id) " +
		"or (r.member1.id = :member2Id and r.member2.id = :member1Id)")
	Recommendation findByMember1AnAndMember2(@Param("member1Id") String memeber1Id,
		@Param("member2Id") String member2Id);

	@Modifying
	@Query("delete from Recommendation r where r.member1.id =:memberId or r.member2.id =:memberId")
	void delete(@Param("memberId") String memberId);
}
