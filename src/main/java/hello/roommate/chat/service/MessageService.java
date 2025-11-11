package hello.roommate.chat.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.chat.domain.Message;
import hello.roommate.chat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;

/**
 * 메시지를 저장, 조회, 변환하는 서비스 클래스
 *
 * @author Wootae
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {

	private final MessageRepository messageRepository;

	/**
	 * 메시지 저장하는 메서드
	 *
	 * @param message 메시지 정보
	 * @return Message
	 */
	public Message save(Message message) {
		return messageRepository.save(message);
	}

	/**
	 * 해당 채팅방의 가장 최근 메시지를 조회한다.
	 *
	 * @param chatRoomId 채팅방 id
	 * @return Message
	 */
	public Optional<Message> findLatestMessage(Long chatRoomId) {
		return messageRepository.findFirstByChatRoomIdOrderBySendTimeDesc(chatRoomId);
	}

	/**
	 * 특정 채팅방의 모든 메시지를 조회한다.
	 *
	 * @param chatRoomId 채팅방 id
	 * @return 모든 메시지
	 */
	public List<Message> findAllByChatRoomId(Long chatRoomId) {
		return messageRepository.findAllByChatRoomId(chatRoomId);
	}

}
