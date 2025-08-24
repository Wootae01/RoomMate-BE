package hello.roommate.mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.chat.domain.Message;
import hello.roommate.chat.dto.ChatRoomDTO;
import hello.roommate.chat.dto.MessageReceiveDTO;
import hello.roommate.chat.service.MessageService;
import hello.roommate.member.domain.Member;
import hello.roommate.member.domain.MemberChatRoom;
import hello.roommate.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatRoomMapper {
	private final MessageService messageService;
	private final MemberService memberService;

	public List<ChatRoomDTO> convertToChatRoomDTO(Long memberId, List<ChatRoom> chatRooms) {
		List<ChatRoomDTO> result = new ArrayList<>();
		for (ChatRoom chatRoom : chatRooms) {
			List<MemberChatRoom> memberChatRooms = chatRoom.getMemberChatRooms();

			for (MemberChatRoom memberChatRoom : memberChatRooms) {
				if (!memberChatRoom.getMember().getId().equals(memberId)) { // 채팅방 중 내가 아닌 상대방 닉네임 찾고
					Member opponent = memberChatRoom.getMember();
					String nickname = opponent.getNickname();
					Optional<Message> optional = messageService.findLatestMessage(chatRoom.getId()); //최근 메시지 찾고
					if (optional.isEmpty()) {
						continue;
					}
					Message latestMessage = optional.get();
					ChatRoomDTO dto = new ChatRoomDTO(); //dto로 변환
					dto.setChatRoomId(chatRoom.getId());
					dto.setNickname(nickname);
					dto.setUpdatedTime(latestMessage.getSendTime());
					dto.setMessage(latestMessage.getContent());
					result.add(dto);
				}
			}
		}
		return result;
	}

	public ChatRoomDTO convertReceiveToChatRoomDTO(MessageReceiveDTO messageReceiveDTO) {
		Member sender = memberService.findById(messageReceiveDTO.getMemberId());

		ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
		chatRoomDTO.setChatRoomId(messageReceiveDTO.getChatRoomId());
		chatRoomDTO.setMessage(messageReceiveDTO.getContent());
		chatRoomDTO.setNickname(sender.getNickname());
		chatRoomDTO.setUpdatedTime(convertToLocalDateTime(messageReceiveDTO.getSendTime()));
		return chatRoomDTO;
	}

	private LocalDateTime convertToLocalDateTime(String date) {
		LocalDateTime result = LocalDateTime.from(

			Instant.from(
				DateTimeFormatter.ISO_DATE_TIME.parse(date)
			).atZone(ZoneId.of("Asia/Seoul"))
		);
		return result;
	}
}
