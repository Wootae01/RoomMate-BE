package hello.roommate.recommendation.repository;

import hello.roommate.recommendation.domain.Preference;
import hello.roommate.recommendation.domain.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {

    @Query("select p from Preference p where p.member.id =:memberId")
    List<Preference> findByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("delete from Preference p where p.member.id = :memberId and p.option.category = :category")
    void deleteByMemberAndOption(@Param("memberId") Long memberId, @Param("category") Category category);
}
