package hello.roommate.member.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Gender;
import hello.roommate.member.domain.Member;
import hello.roommate.member.domain.MemberChatRoom;
import hello.roommate.member.dto.RecommendMemberDTO;
import hello.roommate.member.repository.MemberRepository;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Option;
import hello.roommate.recommendation.domain.Preference;
import hello.roommate.recommendation.domain.enums.Category;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
	private final MemberRepository memberRepository;

	public Member save(Member member) {
		return memberRepository.save(member);
	}

	public Member findById(Long id) {
		return memberRepository.findById(id).orElseThrow();
	}

	public List<Member> findAllByIds(List<Long> ids) {
		return memberRepository.findAllById(ids);
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

	public List<ChatRoom> findAllChatRooms(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NoSuchElementException("등록되지 않은 사용자 입니다."));
		List<MemberChatRoom> memberChatRooms = member.getMemberChatRooms();
		List<ChatRoom> chatRooms = new ArrayList<>();
		for (MemberChatRoom memberChatRoom : memberChatRooms) {
			chatRooms.add(memberChatRoom.getChatRoom());
		}

		return chatRooms;
	}

	public Optional<Member> findByNickname(String nickname) {
		return memberRepository.findByNickname(nickname);
	}

	public void delete(Long id) {
		memberRepository.deleteById(id);
	}

	public List<Member> search(Long memberId, Map<Category, List<Long>> cond, List<Integer> intAges) {
		return memberRepository.search(memberId, cond, intAges);
	}


}
