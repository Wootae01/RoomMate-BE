package hello.roommate.member.service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.auth.jwt.JWTUtil;
import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.chat.repository.ChatRoomRepository;
import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Gender;
import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
import hello.roommate.recommendation.domain.enums.Category;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
	private final MemberRepository memberRepository;
	private final JWTUtil jwtUtil;
	private final ChatRoomRepository chatRoomRepository;

	public Member save(Member member) {
		return memberRepository.save(member);
	}

	public Member findById(Long id) {
		return memberRepository.findById(id).orElseThrow();
	}

	public List<Member> findAllByIds(List<Long> ids) {
		return memberRepository.findAllById(ids);
	}

	public List<Member> findAllByDorm(Long id) {
		Member member = memberRepository.findById(id)
			.orElseThrow();
		Dormitory dorm = member.getDorm();
		return memberRepository.findAllByDorm(dorm, id);
	}

	public List<Member> findAllWithLifeStyle() {
		return memberRepository.findAllWithLifeStyle();
	}

	public Member findWithLifeStyleById(Long id) {
		return memberRepository.findWithLifeStyleById(id);
	}

	public List<Member> findAllWithPreferenceByIds(List<Long> ids) {
		return memberRepository.findAllWithPreferenceByIds(ids);
	}

	public List<Member> findAllWithLifeStyleByDormAndGender(Dormitory dorm, Gender gender) {
		return memberRepository.findAllWithLifeStyleByDormAndGender(dorm, gender);
	}

	public Optional<Member> findByUsername(String username) {
		return memberRepository.findByUsername(username);
	}

	// Authorization 헤더에서 JWT를 파싱해 해당 사용자 Member 반환
	public Member findByRequest(HttpServletRequest request) {
		String username = jwtUtil.getUsername(request.getHeader("Authorization").split(" ")[1]);
		return memberRepository.findByUsername(username)
			.orElseThrow(() -> new NoSuchElementException("등록되지 않은 사용자입니다."));
	}

	public List<ChatRoom> findAllChatRooms(Long memberId) {
		memberRepository.findById(memberId)
			.orElseThrow(() -> new NoSuchElementException("등록되지 않은 사용자 입니다."));
		return memberRepository.findAllChatRoomsWithDetails(memberId);
	}

	public boolean existByNickname(String nickname) {
		return memberRepository.existsByNickname(nickname);
	}

	public void delete(Long id) {
		// 해당 멤버의 ChatRoom 먼저 삭제 (Message, MemberChatRoom cascade 삭제)
		List<ChatRoom> chatRooms = memberRepository.findAllChatRoomsWithDetails(id);
		chatRoomRepository.deleteAll(chatRooms);
		memberRepository.deleteById(id);
	}

	public List<Member> search(Long memberId, Map<Category, List<Long>> cond, List<Integer> intAges) {
		return memberRepository.search(memberId, cond, intAges);
	}

	public List<Long> findEligibleMember(Long myId, Dormitory dorm, Gender gender,
		List<Integer> ages) {
		if (ages.isEmpty()) {
			return memberRepository.findEligibleMemberExceptAge(myId, dorm, gender);
		} else {
			return memberRepository.findEligibleMember(myId, dorm, gender, ages);
		}

	}

}
