package hello.roommate.member.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
	private final MemberRepository repository;

	public Member save(Member member) {
		repository.save(member);
		return member;
	}

	public Member findById(String id) {
		return repository.findById(id).orElseThrow();
	}

	public List<Member> findByDorm(Dormitory dorm) {
		return repository.findByDorm(dorm);
	}

	public void delete(String id) {
		repository.deleteById(id);
	}
}
