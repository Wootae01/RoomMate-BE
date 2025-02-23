package hello.roommate.chat.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.chat.dto.CreateChatRoomDTO;
import hello.roommate.chat.repository.ChatRoomRepository;
import hello.roommate.member.domain.Member;
import hello.roommate.member.domain.MemberChatRoom;
import hello.roommate.member.repository.MemberChatRoomRepository;
import hello.roommate.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

/**
 * 채팅방 생성, 조회 하는 서비스 클래스
 *
 * @author Wootae
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {
	private final ChatRoomRepository chatRoomRepository;
	private final MemberRepository memberRepository;
	private final MemberChatRoomRepository memberChatRoomRepository;

	/**
	 * 채팅방 id로 특정 채팅방 찾아서 반환
	 *
	 * @param chatRoomId 채팅방 id
	 * @return ChatRoom
	 */
	public ChatRoom findRoomById(Long chatRoomId) {
		return chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new NoSuchElementException("ChatRoom not found"));
	}

	/**
	 * 채팅방 생성 하는 메서드.
	 * 기존 채팅방이 있으면 그 채팅방 반환, 없으면 새로운 채팅방 생성 후 반환
	 *
	 * @param dto 채팅방 생성에 필요한 dto
	 * @return ChatRoom
	 */
	public ChatRoom createChatRoom(CreateChatRoomDTO dto) {

		///이미 있으면 기존 채팅방 반환
		Optional<ChatRoom> existChatRoom = memberChatRoomRepository
			.findExistingChatRoomByMembersId(dto.getMember1Id(), dto.getMember2Id());
		if (existChatRoom.isPresent()) {
			return existChatRoom.get();
		}

		//채팅방 없으면 새로 만듬
		Member member1 = memberRepository.findById(dto.getMember1Id())
			.orElseThrow(() -> new NoSuchElementException("id dose not exist"));
		Member member2 = memberRepository.findById(dto.getMember2Id())
			.orElseThrow(() -> new NoSuchElementException("id dose not exist"));

		MemberChatRoom memberChatRoom1 = new MemberChatRoom();
		MemberChatRoom memberChatRoom2 = new MemberChatRoom();
		memberChatRoom1.setMember(member1);
		memberChatRoom2.setMember(member2);

		ChatRoom chatRoom = new ChatRoom();
		chatRoom.addMemberChatRooms(memberChatRoom1);
		chatRoom.addMemberChatRooms(memberChatRoom2);
		return chatRoomRepository.save(chatRoom);
	}

}
