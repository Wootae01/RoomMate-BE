package hello.roommate.recommendation.repository;

import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LifeStyleRepository extends JpaRepository<LifeStyle, Long> {

    List<LifeStyle> findByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("delete from LifeStyle l where l.member.id = :memberId and l.option.category = :category")
    void deleteByMemberAndOption(@Param("memberId") Long memberId, @Param("category") Category category);

}
