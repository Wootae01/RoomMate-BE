package hello.roommate.chat.service;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberChatRoomRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberChatRoomService {

	private final MemberChatRoomRepository memberChatRoomRepository;

	@Transactional
	public Member findByMemberExceptMyId(Long chatRoomId, Long myId) {
		return memberChatRoomRepository.findByMemberExceptMyId(chatRoomId, myId)
			.orElseThrow(() -> new NoSuchElementException("채팅 상대를 찾을 수 없습니다."));
	}
}
