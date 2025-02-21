package hello.roommate.recommendation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.enums.Category;

public interface LifeStyleRepository extends JpaRepository<LifeStyle, Long> {

	List<LifeStyle> findByMemberId(@Param("memberId") Long memberId);

	@Modifying
	@Query("delete from LifeStyle l where l.member.id = :memberId and l.option.category = :category")
	void deleteByMemberAndOption(@Param("memberId") Long memberId, @Param("category") Category category);

	@Modifying
	@Query("delete from LifeStyle l where l.member.id = :memberId")
	void deleteByMemberId(@Param("memberId") Long memberId);

}
