package hello.roommate.chat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.chat.domain.Message;
import hello.roommate.chat.repository.ChatRoomRepository;
import hello.roommate.chat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {
	private final ChatRoomRepository chatRoomRepository;
	private final MessageRepository messageRepository;

	//특정 채팅방 찾기
	public ChatRoom findRoomById(Long chatRoomId) {
		return chatRoomRepository.findById(chatRoomId).orElseThrow();
	}

	//새로운 채팅방 만듬
	public void createChatRoom() {

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
