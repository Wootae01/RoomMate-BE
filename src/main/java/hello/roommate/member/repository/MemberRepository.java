package hello.roommate.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

	List<Member> findByDorm(Dormitory dormitory);

	Optional<Member> findByNickname(String nickname);

	Optional<Member> findByUsername(String username);
}
