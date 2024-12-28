package hello.roommate.member.repository;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, String> {

    List<Member> findByDorm(Dormitory dormitory);
}
