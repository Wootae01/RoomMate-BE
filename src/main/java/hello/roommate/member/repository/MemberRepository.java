package hello.roommate.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, String> {

	List<Member> findByDorm(Dormitory dormitory);
}
