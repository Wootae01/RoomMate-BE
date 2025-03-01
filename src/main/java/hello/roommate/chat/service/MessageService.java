package hello.roommate.chat.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.chat.domain.Message;
import hello.roommate.chat.dto.MessageDTO;
import hello.roommate.chat.repository.MessageRepository;
import hello.roommate.member.domain.Member;
import hello.roommate.member.service.MemberService;
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
	private final ChatRoomService chatRoomService;
	private final MemberService memberService;

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
	public Message findLatestMessage(Long chatRoomId) {
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

	/**
	 * MessageDTO 를 Message 로 전환하는 메서드
	 *
	 * @param dto MessageDTO
	 * @return Message
	 */
	public Message convertToEntity(MessageDTO dto) {
		Message message = new Message();
		ChatRoom chatRoom = chatRoomService.findRoomById(dto.getChatRoomId());
		Member sender = memberService.findById(dto.getMemberId());

		message.setContent(dto.getContent());

		message.setSendTime(convertToLocalDateTime(dto.getSendTime()));
		message.setSender(sender);
		message.setChatRoom(chatRoom);
		return message;
	}

	/**
	 * Message 를 MessageDTO 로 전환하는 메서드
	 *
	 * @param message
	 * @return MessageDTO
	 */
	public MessageDTO convertToDTO(Message message) {
		MessageDTO dto = new MessageDTO();
		dto.setChatRoomId(message.getChatRoom().getId());
		Member member = message.getSender();
		dto.setMemberId(member.getId());
		dto.setNickname(member.getNickname());
		dto.setContent(message.getContent());
		dto.setSendTime(message.getSendTime().toString());

		return dto;
	}

	/**
	 * ISO 8601 형식의 날짜 문자열을 LocalDateTime 으로 전환하는 메서드
	 *
	 * @param date ISO 8601 형식의 날짜
	 * @return LocalDateTime
	 */
	private LocalDateTime convertToLocalDateTime(String date) {
		LocalDateTime result = LocalDateTime.from(

			Instant.from(
				DateTimeFormatter.ISO_DATE_TIME.parse(date)
			).atZone(ZoneId.of("Asia/Seoul"))
		);
		return result;
	}
}
