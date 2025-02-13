package hello.roommate.chat.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.chat.domain.Message;
import hello.roommate.chat.repository.ChatRoomRepository;
import hello.roommate.chat.repository.MessageRepository;
import hello.roommate.member.domain.Member;
import hello.roommate.member.domain.MemberChatRoom;
import hello.roommate.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {
	private final ChatRoomRepository chatRoomRepository;
	private final MessageRepository messageRepository;
	private final MemberRepository memberRepository;

	//특정 채팅방 찾기
	public ChatRoom findRoomById(Long chatRoomId) {
		return chatRoomRepository.findById(chatRoomId).orElseThrow();
	}

	//새로운 채팅방 만듬
	public ChatRoom createChatRoom(String member1Id, String member2Id) {
		Member member1 = memberRepository.findById(member1Id)
			.orElseThrow(() -> new NoSuchElementException("id dose not exist"));
		Member member2 = memberRepository.findById(member2Id)
			.orElseThrow(() -> new NoSuchElementException("id dose not exist"));

		MemberChatRoom memberChatRoom1 = new MemberChatRoom();
		MemberChatRoom memberChatRoom2 = new MemberChatRoom();
		memberChatRoom1.setMember(member1);
		memberChatRoom2.setMember(member2);

		ChatRoom chatRoom = new ChatRoom();
		chatRoom.setUpdatedTime(LocalDateTime.now());
		chatRoom.addMemberChatRooms(memberChatRoom1);
		chatRoom.addMemberChatRooms(memberChatRoom2);
		return chatRoomRepository.save(chatRoom);
	}

	//해당 채팅 방의 모든 메시지 내용 반환
	public List<Message> findAllByChatRoomId(Long chatRoomId) {
		return messageRepository.findAllByChatRoomId(chatRoomId);
	}

	//해당 채팅방의 가장 최근 메시지 반환

	//메시지 저장
	public Message save(Message message) {
		messageRepository.save(message);
		return message;
	}
}
