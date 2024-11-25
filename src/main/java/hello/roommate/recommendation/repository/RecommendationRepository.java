package hello.roommate.recommendation.repository;

import hello.roommate.recommendation.domain.Recommendation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class RecommendationRepository {

    private final EntityManager em;

    public Recommendation save(Recommendation recommendation) {
        em.persist(recommendation);
        return recommendation;
    }

    public List<Recommendation> findByMemberId(String memberId) {
        return em.createQuery("SELECT r from Recommendation r " +
                        "WHERE r.member1.id =:memberId OR r.member2.id =:memberId", Recommendation.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public void update(RecommendationUpdateDto updateDto) {
        Recommendation recommendation = em.createQuery("SELECT r from Recommendation r " +
                        "WHERE (r.member1.id =:member1Id AND r.member2.id =:member2Id) " +
                        "OR (r.member2.id =:member1Id AND r.member1.id =:member2Id)", Recommendation.class)
                .setParameter("member1Id", updateDto.getMember1Id())
                .setParameter("member2Id", updateDto.getMember2Id())
                .getSingleResult();

        recommendation.setScore(updateDto.getScore());
    }

    public void delete(String memberId) {
        em.createQuery("DELETE FROM Recommendation r WHERE r.member1.id =: memberId OR r.member2.id =:memberId")
                        .setParameter("memberId", memberId)
                        .executeUpdate();

    }
}
