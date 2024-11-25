package hello.roommate.member.repository;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;

    public Member save(Member member) {
        em.persist(member);

        return member;
    }

    public Member findById(String id) {
        return em.find(Member.class, id);
    }

    public List<Member> findByDorm(Dormitory dorm) {

        return em.createQuery("SELECT m FROM Member m WHERE m.dorm =:dorm")
                .setParameter("dorm", dorm)
                .getResultList();
    }

    public void delete(String id) {
        Member member = em.find(Member.class, id);
        em.remove(member);
    }
}

