package hello.roommate.mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.chat.domain.Message;
import hello.roommate.chat.dto.MessageDTO;
import hello.roommate.chat.dto.MessageReceiveDTO;
import hello.roommate.chat.service.ChatRoomService;
import hello.roommate.member.domain.Member;
import hello.roommate.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MessageMapper {

	private final MemberService memberService;
	private final ChatRoomService chatRoomService;

	/**
	 * MessageReceiveDTO 를 MessageDTO 로 변환하는 메서드
	 * @param messageReceiveDTO receiveDTO
	 * @return MessageDTO
	 */
	public MessageDTO convertReceiveToMessageDTO(MessageReceiveDTO messageReceiveDTO) {
		Member sender = memberService.findById(messageReceiveDTO.getMemberId());

		MessageDTO sendDTO = new MessageDTO();
		sendDTO.setSendTime(messageReceiveDTO.getSendTime());
		sendDTO.setNickname(sender.getNickname());
		sendDTO.setContent(messageReceiveDTO.getContent());
		sendDTO.setMemberId(messageReceiveDTO.getMemberId());
		sendDTO.setChatRoomId(messageReceiveDTO.getChatRoomId());
		return sendDTO;
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
	 * MessageReceiveDTO 를 Message 로 전환하는 메서드
	 *
	 * @param dto MessageReceiveDTO
	 * @return Message
	 */
	public Message convertToEntity(MessageReceiveDTO dto) {
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
