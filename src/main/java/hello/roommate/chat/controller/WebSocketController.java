package hello.roommate.chat.controller;

import java.time.LocalDateTime;

import hello.roommate.chat.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.chat.domain.Message;
import hello.roommate.chat.dto.MessageDTO;
import hello.roommate.chat.service.ChatRoomService;
import hello.roommate.member.domain.Member;
import hello.roommate.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

	private final MessageService messageService;

	//메시지 전송
	@MessageMapping("/{roomId}")
	@SendTo("/topic/chatroom/{roomId}")
	public MessageDTO sendMessage(MessageDTO messageDto) {
		Message message = messageService.convertToEntity(messageDto);
		messageService.save(message);

		return messageDto;
	}

}
